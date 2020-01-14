package org.openmrs.module.messages.api.util;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.module.messages.api.model.TemplateFieldType;
import org.openmrs.module.messages.api.model.TemplateFieldValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.openmrs.module.messages.api.constants.MessagesConstants.DEFAULT_FRONT_END_DATE_FORMAT;
import static org.openmrs.module.messages.api.constants.MessagesConstants.DEFAULT_SERVER_SIDE_DATE_FORMAT;
import static org.openmrs.module.messages.api.model.TemplateFieldType.END_OF_MESSAGES;

public final class EndDateUtil {

    private static final String SEPARATOR = "|";
    private static final String AFTER_TIMES_TEXT = "after %s time(s)";
    private static final String NEVER_TEXT = "never";
    private static final String EMPTY_TEXT = "";

    public static Date getEndDate(List<TemplateFieldValue> templateFieldValues) {

        Date result = parseEndDate(getEndDateValue(templateFieldValues));
        if (result == null) {
            result = parseEndDate(getDefaultEndDateValue(templateFieldValues));
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

    public static Date parseEndDate(String dbValue) {
        Date result = null;
        if (!StringUtils.isBlank(dbValue)) {
            String[] chain = StringUtils.split(dbValue, SEPARATOR);
            if (chain.length >= 2) {
                String typeName = chain[0];
                EndDateType type = EndDateType.fromName(typeName);
                String value = chain[1];
                result = getDateFromType(type, value);
            }
        }

        return result;
    }

    private static String getEndDateValue(List<TemplateFieldValue> templateFieldValues) {
        return getValue(templateFieldValues, END_OF_MESSAGES);
    }

    private static String getDefaultEndDateValue(List<TemplateFieldValue> templateFieldValues) {
        return getDefaultValue(templateFieldValues, END_OF_MESSAGES);
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

    private static Date getDateFromType(EndDateType type, String value) {
        switch (type) {
            case DATE_PICKER:
                return getDate(value);
            case AFTER_TIMES:
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
