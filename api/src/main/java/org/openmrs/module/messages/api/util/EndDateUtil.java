package org.openmrs.module.messages.api.util;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.module.messages.api.model.TemplateFieldType;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.api.util.end.date.EndDate;
import org.openmrs.module.messages.api.util.end.date.EndDateFactory;
import org.openmrs.module.messages.api.util.end.date.EndDateParams;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.openmrs.module.messages.api.constants.MessagesConstants.DEFAULT_FRONT_END_DATE_FORMAT;
import static org.openmrs.module.messages.api.constants.MessagesConstants.DEFAULT_SERVER_SIDE_DATE_FORMAT;
import static org.openmrs.module.messages.api.model.TemplateFieldType.START_OF_MESSAGES;
import static org.openmrs.module.messages.api.model.TemplateFieldType.END_OF_MESSAGES;
import static org.openmrs.module.messages.api.model.TemplateFieldType.MESSAGING_FREQUENCY_DAILY_OR_WEEKLY_OR_MONTHLY;
import static org.openmrs.module.messages.api.model.TemplateFieldType.MESSAGING_FREQUENCY_WEEKLY_OR_MONTHLY;
import static org.openmrs.module.messages.api.model.TemplateFieldType.DAY_OF_WEEK;

public final class EndDateUtil {

    public static final String SEPARATOR = "|";
    private static final String AFTER_TIMES_TEXT = "after %s time(s)";
    private static final String NEVER_TEXT = "never";
    private static final String EMPTY_TEXT = "";
    private static final List<TemplateFieldType> FREQUENCIES = Arrays.asList(MESSAGING_FREQUENCY_WEEKLY_OR_MONTHLY,
            MESSAGING_FREQUENCY_DAILY_OR_WEEKLY_OR_MONTHLY, DAY_OF_WEEK);
    private static final int OTHER_TYPE_MIN_PARTS = 3;

    public static Date getEndDate(List<TemplateFieldValue> templateFieldValues) {
        Date startDate = getStartDate(templateFieldValues);
        String[] frequency = getIncidence(templateFieldValues);
        Date result = parseEndDate(getEndDateValue(templateFieldValues), startDate, frequency);
        if (result == null) {
            result = parseEndDate(getDefaultEndDateValue(templateFieldValues), startDate, frequency);
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
            Date result = parseEndDate(endDate);
            SimpleDateFormat newDateFormat = new SimpleDateFormat(DEFAULT_FRONT_END_DATE_FORMAT);
            return newDateFormat.format(result);
        } else {
            return StringUtils.defaultString(endDate, EMPTY_TEXT);
        }
    }

    private static Date parseEndDate(String dbValue) {
        return parseEndDate(dbValue, null, null);
    }

    private static Date parseEndDate(String dbValue, Date startDate, String[] frequency) {
        Date result = null;
        if (!StringUtils.isBlank(dbValue)) {
            String[] chain = StringUtils.split(dbValue, SEPARATOR);
            if (chain.length >= 2) {
                String typeName = chain[0];
                EndDateType type = EndDateType.fromName(typeName);
                String value = parseEndDateTail(type, chain);
                result = getDateFromType(type, new EndDateParams(value, startDate, nullableArrayToList(frequency)));
            }
        }
        return result;
    }

    private static List<String> nullableArrayToList(String[] frequency) {
        if (frequency != null && frequency.length > 0) {
            return Arrays.asList(frequency);
        }
        return null;
    }

    private static String parseEndDateTail(EndDateType type, String[] chain) {
        if (EndDateType.OTHER.equals(type) && chain.length >= OTHER_TYPE_MIN_PARTS) {
            return chain[1] + SEPARATOR + chain[2];
        }
        return chain[1];
    }

    private static Date getStartDate(List<TemplateFieldValue> templateFieldValues) {
        Date result = parseStringDbDateToServerSideFormat(getStartDateValue(templateFieldValues));
        if (result == null) {
            result = parseStringDbDateToServerSideFormat(getDefaultStartDateValue(templateFieldValues));
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

    private static String getEndDateValue(List<TemplateFieldValue> templateFieldValues) {
        return getValue(templateFieldValues, END_OF_MESSAGES);
    }

    private static String getStartDateValue(List<TemplateFieldValue> templateFieldValues) {
        return getValue(templateFieldValues, START_OF_MESSAGES);
    }

    private static String getIncidenceValue(List<TemplateFieldValue> templateFieldValues) {
        String value = null;
        for (TemplateFieldType type : FREQUENCIES) {
            value = getValue(templateFieldValues, type);
            if (value != null) {
                break;
            }
        }
        return value;
    }

    private static String getDefaultStartDateValue(List<TemplateFieldValue> templateFieldValues) {
        return getDefaultValue(templateFieldValues, START_OF_MESSAGES);
    }

    private static String getDefaultEndDateValue(List<TemplateFieldValue> templateFieldValues) {
        return getDefaultValue(templateFieldValues, END_OF_MESSAGES);
    }

    private static String getDefaultIncidenceValue(List<TemplateFieldValue> templateFieldValues) {
        String value = null;
        for (TemplateFieldType type : FREQUENCIES) {
            value = getDefaultValue(templateFieldValues, type);
            if (value != null) {
                break;
            }
        }
        return value;
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

    private static Date getDateFromType(EndDateType type, EndDateParams params) {
        EndDate builder = new EndDateFactory().create(type, params);
        return builder.getDate();
    }

    private static Date parseStringDbDateToServerSideFormat(String value) {
        try {
            SimpleDateFormat newDateFormat = new SimpleDateFormat(DEFAULT_SERVER_SIDE_DATE_FORMAT);
            return newDateFormat.parse(value);
        } catch (ParseException | NullPointerException e) {
            return null;
        }
    }

    private static boolean isDateParsable(String endDate) {
        try {
            Date result = parseEndDate(endDate);
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
