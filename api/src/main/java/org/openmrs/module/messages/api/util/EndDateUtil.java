package org.openmrs.module.messages.api.util;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.module.messages.api.model.TemplateFieldType;
import org.openmrs.module.messages.api.model.TemplateFieldValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.openmrs.module.messages.api.constants.MessagesConstants.DEFAULT_FRONT_END_DATE_FORMAT;
import static org.openmrs.module.messages.api.constants.MessagesConstants.DEFAULT_SERVER_SIDE_DATE_FORMAT;
import static org.openmrs.module.messages.api.model.TemplateFieldType.END_OF_MESSAGES;
import static org.openmrs.module.messages.api.model.TemplateFieldType.MESSAGING_FREQUENCY_DAILY_OR_WEEKLY_OR_MONTHLY;
import static org.openmrs.module.messages.api.model.TemplateFieldType.START_OF_MESSAGES;

public final class EndDateUtil {

    private static final String SEPARATOR = "|";
    private static final String AFTER_TIMES_TEXT = "after %s time(s)";
    private static final String NEVER_TEXT = "never";
    private static final String EMPTY_TEXT = "";
    private static final int OTHER_TYPE_MIN_PARTS = 3;

    public static Date getEndDate(List<TemplateFieldValue> templateFieldValues) {
        Date startDate = getStartDate(templateFieldValues);
        String[] incidence = getIncidence(templateFieldValues);
        Date result = parseEndDate(getEndDateValue(templateFieldValues), startDate, incidence);
        if (result == null) {
            result = parseEndDate(getDefaultEndDateValue(templateFieldValues), startDate, incidence);
        }
        return result;
    }

    public static String getEndDateText(String endDate) {
        if (isSpecialPhraseFormat(endDate, EndDateType.AFTER_TIMES)) {
            String value = StringUtils.split(endDate, SEPARATOR)[1];
            return String.format(AFTER_TIMES_TEXT, value);
        } else if (isSpecialPhraseFormat(endDate, EndDateType.NO_DATE)) {
            return NEVER_TEXT;
        } else if (isDateParsable(endDate)) {
            Date result = parseEndDate(endDate, null, null);
            SimpleDateFormat newDateFormat = new SimpleDateFormat(DEFAULT_FRONT_END_DATE_FORMAT);
            return newDateFormat.format(result);
        } else {
            return StringUtils.defaultString(endDate, EMPTY_TEXT);
        }
    }

    public static Date parseEndDate(String dbValue, Date startDate, String[] incidence) {
        Date result = null;
        if (!StringUtils.isBlank(dbValue)) {
            String[] chain = StringUtils.split(dbValue, SEPARATOR);
            if (chain.length >= 2) {
                String typeName = chain[0];
                EndDateType type = EndDateType.fromName(typeName);
                String value = getValue(type, chain);
                result = getDateFromType(type, value, startDate, incidence);
            }
        }
        return result;
    }

    private static String getValue(EndDateType type, String[] chain) {
        if (EndDateType.OTHER.equals(type) && chain.length >= OTHER_TYPE_MIN_PARTS) {
            return chain[1] + SEPARATOR + chain[2];
        }
        return chain[1];
    }

    private static Date getEndDate(Date startDate, TimeType timeType, Integer units) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        try {
            calculateDate(calendar, timeType, units);
        } catch (IllegalArgumentException e) {
            return null;
        }
        return calendar.getTime();
    }

    private static void calculateDate(Calendar calendar, TimeType timeType, Integer units)
            throws IllegalArgumentException {
        switch (timeType) {
            case DAY:
                calendar.add(Calendar.DAY_OF_MONTH, units);
                break;
            case MONTH:
                calendar.add(Calendar.MONTH, units);
                break;
            case YEAR:
                calendar.add(Calendar.YEAR, units);
                break;
            default:
                throw new IllegalArgumentException(String.format("Invalid Time Type provided '%s'.",
                        timeType));
        }
    }

    private static Date getStartDate(List<TemplateFieldValue> templateFieldValues) {
        Date result = getDate(getStartDateValue(templateFieldValues));
        if (result == null) {
            result = getDate(getDefaultStartDateValue(templateFieldValues));
        }
        return result;
    }

    private static String[] getIncidence(List<TemplateFieldValue> templateFieldValues) {
        final String separator = ",";
        String[] result = StringUtils.split(getIncidenceValue(templateFieldValues), separator);
        if (result == null) {
            result = StringUtils.split(getDefaultIncidenceValue(templateFieldValues), separator);
        }
        return result;
    }

    private static String getStartDateValue(List<TemplateFieldValue> templateFieldValues) {
        return getValue(templateFieldValues, START_OF_MESSAGES);
    }

    private static String getEndDateValue(List<TemplateFieldValue> templateFieldValues) {
        return getValue(templateFieldValues, END_OF_MESSAGES);
    }

    // TODO: 16.01.2020 CFLM-459: it changed from MESSAGING_FREQUENCY 
    private static String getIncidenceValue(List<TemplateFieldValue> templateFieldValues) {
        return getValue(templateFieldValues, MESSAGING_FREQUENCY_DAILY_OR_WEEKLY_OR_MONTHLY);
    }

    private static String getDefaultStartDateValue(List<TemplateFieldValue> templateFieldValues) {
        return getDefaultValue(templateFieldValues, START_OF_MESSAGES);
    }

    private static String getDefaultEndDateValue(List<TemplateFieldValue> templateFieldValues) {
        return getDefaultValue(templateFieldValues, END_OF_MESSAGES);
    }

    // TODO: 16.01.2020 CFLM-459: it changed from MESSAGING_FREQUENCY
    private static String getDefaultIncidenceValue(List<TemplateFieldValue> templateFieldValues) {
        return getDefaultValue(templateFieldValues, MESSAGING_FREQUENCY_DAILY_OR_WEEKLY_OR_MONTHLY);
    }

    private static String getValue(List<TemplateFieldValue> templateFieldValues, TemplateFieldType type) {
        for (TemplateFieldValue fieldValue : templateFieldValues) {
            if (fieldValue.getTemplateField().getTemplateFieldType().equals(type)) {
                return fieldValue.getValue();
            }
        }
        return null;
    }

    private static String getDefaultValue(List<TemplateFieldValue> templateFieldValues, TemplateFieldType type) {
        for (TemplateFieldValue fieldValue : templateFieldValues) {
            if (fieldValue.getTemplateField().getTemplateFieldType().equals(type)) {
                return fieldValue.getTemplateField().getDefaultValue();
            }
        }
        return null;
    }

    private static Date getDateFromType(EndDateType type, String value, Date startDate, String[] incidence) {
        switch (type) {
            case DATE_PICKER:
                return getDate(value);
            case AFTER_TIMES:
                return getAfterTimes(value, startDate, incidence);
            case OTHER:
                return getDate(value, startDate);
            case NO_DATE:
            default:
                return null;
        }
    }

    private static Date getDate(String value) {
        try {
            SimpleDateFormat newDateFormat = new SimpleDateFormat(DEFAULT_SERVER_SIDE_DATE_FORMAT);
            return newDateFormat.parse(value);
        } catch (ParseException | NullPointerException e) {
            return null;
        }
    }

    private static Date getDate(String value, Date startDate) {
        String[] chain = StringUtils.split(value, SEPARATOR);
        TimeType timeType;
        Integer units;
        try {
            timeType = TimeType.valueOf(chain[0]);
            units = Integer.valueOf(chain[1]);
        } catch (IllegalArgumentException e) {
            return null;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
        return getEndDate(startDate, timeType, units);
    }

    private static Date getAfterTimes(String value, Date startDate, String[] incidence) {
//        AfterTimes afterTimes = new AfterTimes();
        // TODO: 16.01.2020 CFLM-459 remove it - this is only to stop PMD from complaining
        String string = value + startDate + incidence[0];
        return null;
    }

    private static boolean isDateParsable(String endDate) {
        try {
            Date result = parseEndDate(endDate, null, null);
            SimpleDateFormat newDateFormat = new SimpleDateFormat(DEFAULT_FRONT_END_DATE_FORMAT);
            String text = newDateFormat.format(result);
            return StringUtils.isNotBlank(text);
        } catch (Exception ex) {
            return false;
        }
    }

    private static boolean isSpecialPhraseFormat(String endDate, EndDateType specialPhrase) {
        String[] chain = StringUtils.split(endDate, SEPARATOR);
        return chain != null
            && chain.length > 1
            && StringUtils.equals(chain[0], specialPhrase.getName());
    }

    private EndDateUtil() {
    }
}
