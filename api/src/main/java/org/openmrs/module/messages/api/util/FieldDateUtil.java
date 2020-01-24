/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.util;

import static org.openmrs.module.messages.api.constants.MessagesConstants.DEFAULT_FRONT_END_DATE_FORMAT;
import static org.openmrs.module.messages.api.constants.MessagesConstants.DEFAULT_SERVER_SIDE_DATE_FORMAT;
import static org.openmrs.module.messages.api.model.TemplateFieldType.DAY_OF_WEEK;
import static org.openmrs.module.messages.api.model.TemplateFieldType.DAY_OF_WEEK_SINGLE;
import static org.openmrs.module.messages.api.model.TemplateFieldType.END_OF_MESSAGES;
import static org.openmrs.module.messages.api.model.TemplateFieldType.MESSAGING_FREQUENCY_DAILY_OR_WEEKLY_OR_MONTHLY;
import static org.openmrs.module.messages.api.model.TemplateFieldType.MESSAGING_FREQUENCY_WEEKLY_OR_MONTHLY;
import static org.openmrs.module.messages.api.model.TemplateFieldType.START_OF_MESSAGES;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.messages.api.model.TemplateFieldType;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.api.util.end.date.EndDate;
import org.openmrs.module.messages.api.util.end.date.EndDateFactory;
import org.openmrs.module.messages.api.util.end.date.EndDateParams;

public final class FieldDateUtil {

    public static final String SEPARATOR = "|";
    private static final String AFTER_TIMES_TEXT = "after %s time(s)";
    private static final String NEVER_TEXT = "never";
    private static final String EMPTY_TEXT = "";
    private static final List<TemplateFieldType> FREQUENCIES = Arrays.asList(MESSAGING_FREQUENCY_WEEKLY_OR_MONTHLY,
            MESSAGING_FREQUENCY_DAILY_OR_WEEKLY_OR_MONTHLY);
    private static final List<TemplateFieldType> DAYS_OF_WEEK = Arrays.asList(DAY_OF_WEEK,
        DAY_OF_WEEK_SINGLE);
    private static final int OTHER_TYPE_MIN_PARTS = 3;
    private static final String ADHERENCE_REPORT_DAILY = "Adherence report daily";
    private static final String ADHERENCE_REPORT_WEEKLY = "Adherence report weekly";
    private static final String VISIT_REMINDER = "Visit reminder";
    private static final FrequencyType DEFAULT_FREQUENCY_TYPE = FrequencyType.DAILY;

    private static final Log LOG = LogFactory.getLog(FieldDateUtil.class);

    public static Date getStartDate(List<TemplateFieldValue> templateFieldValues) {
        Date result = parseStringDbDateToServerSideFormat(getStartDateValue(templateFieldValues));
        if (result == null) {
            result = parseStringDbDateToServerSideFormat(getDefaultStartDateValue(templateFieldValues));
        }
        return result;
    }

    public static Date getEndDate(List<TemplateFieldValue> templateFieldValues,
                                  String templateName) {
        Date startDate = getStartDate(templateFieldValues);
        FrequencyType frequency = getFrequency(templateFieldValues, templateName);
        String[] daysOfWeek = getDaysOfWeek(templateFieldValues);
        Date result = parseEndDate(getEndDateValue(templateFieldValues), startDate, frequency, daysOfWeek);
        if (result == null) {
            result = parseEndDate(getDefaultEndDateValue(templateFieldValues), startDate,
                frequency, daysOfWeek);
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
        return parseEndDate(dbValue, null, null, null);
    }

    private static Date parseEndDate(String dbValue, Date startDate, FrequencyType frequency,
                                     String[] daysOfWeek) {
        Date result = null;
        if (!StringUtils.isBlank(dbValue)) {
            String[] chain = StringUtils.split(dbValue, SEPARATOR);
            if (chain.length >= 2) {
                String typeName = chain[0];
                EndDateType type = EndDateType.fromName(typeName);
                String value = parseEndDateTail(type, chain);
                result = getDateFromType(type, new EndDateParams(value, startDate,
                    frequency, nullableArrayToList(daysOfWeek)));
            }
        }
        return result;
    }

    private static List<String> nullableArrayToList(String[] array) {
        if (array != null && array.length > 0) {
            return Arrays.asList(array);
        }
        return null;
    }

    private static String parseEndDateTail(EndDateType type, String[] chain) {
        if (EndDateType.OTHER.equals(type) && chain.length >= OTHER_TYPE_MIN_PARTS) {
            return chain[1] + SEPARATOR + chain[2];
        }
        return chain[1];
    }

    private static FrequencyType getFrequency(List<TemplateFieldValue> templateFieldValues, String templateName) {
        switch (templateName) {
            case ADHERENCE_REPORT_DAILY:
            case VISIT_REMINDER:
                return FrequencyType.DAILY;
            case ADHERENCE_REPORT_WEEKLY:
                return FrequencyType.WEEKLY;
            default:
                String result = StringUtils.defaultString(
                    getFrequencyValue(templateFieldValues),
                    getDefaultFrequencyValue(templateFieldValues)
                );
                if (StringUtils.isBlank(result)) {
                    return DEFAULT_FREQUENCY_TYPE;
                }
                return FrequencyType.fromName(result);
        }
    }

    private static String[] getDaysOfWeek(List<TemplateFieldValue> templateFieldValues) {
        final String separator = ",";
        String[] result = StringUtils.split(getDaysOfWeekValue(templateFieldValues), separator);
        if (result == null) {
            result = StringUtils.split(getDefaultDaysOfWeekValue(templateFieldValues), separator);
        }
        return result;
    }

    private static String getEndDateValue(List<TemplateFieldValue> templateFieldValues) {
        return getValue(templateFieldValues, END_OF_MESSAGES);
    }

    private static String getStartDateValue(List<TemplateFieldValue> templateFieldValues) {
        return getValue(templateFieldValues, START_OF_MESSAGES);
    }

    private static String getFrequencyValue(List<TemplateFieldValue> templateFieldValues) {
        String value = null;
        for (TemplateFieldType type : FREQUENCIES) {
            value = getValue(templateFieldValues, type);
            if (value != null) {
                break;
            }
        }
        return value;
    }

    private static String getDaysOfWeekValue(List<TemplateFieldValue> templateFieldValues) {
        String value = null;
        for (TemplateFieldType type : DAYS_OF_WEEK) {
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

    private static String getDefaultFrequencyValue(List<TemplateFieldValue> templateFieldValues) {
        String value = null;
        for (TemplateFieldType type : FREQUENCIES) {
            value = getDefaultValue(templateFieldValues, type);
            if (value != null) {
                break;
            }
        }
        return value;
    }

    private static String getDefaultDaysOfWeekValue(List<TemplateFieldValue> templateFieldValues) {
        String value = null;
        for (TemplateFieldType type : DAYS_OF_WEEK) {
            value = getDefaultValue(templateFieldValues, type);
            if (value != null) {
                break;
            }
        }
        return value;
    }

    private static String getValue(List<TemplateFieldValue> templateFieldValues, TemplateFieldType type) {
        String value = null;
        for (TemplateFieldValue fieldValue : templateFieldValues) {
            if (fieldValue.getTemplateField().getTemplateFieldType().equals(type)) {
                if (value == null) {
                    value = fieldValue.getValue();
                } else {
                    LOG.warn(String.format("Found redundant field with type %s in the patient template with id %d. "
                                    + "However, only one expected",
                            type.name(),
                            fieldValue.getPatientTemplate().getId()));
                }
            }
        }
        return value;
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

    private FieldDateUtil() {
    }
}
