/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.messages.api.model.TemplateFieldType;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.api.util.end.date.EndDateFactory;
import org.openmrs.module.messages.api.util.end.date.EndDateParams;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.openmrs.module.messages.api.constants.MessagesConstants.DEFAULT_SERVER_SIDE_DATE_FORMAT;
import static org.openmrs.module.messages.api.model.TemplateFieldType.DAY_OF_WEEK;
import static org.openmrs.module.messages.api.model.TemplateFieldType.DAY_OF_WEEK_SINGLE;
import static org.openmrs.module.messages.api.model.TemplateFieldType.END_OF_MESSAGES;
import static org.openmrs.module.messages.api.model.TemplateFieldType.MESSAGING_FREQUENCY_DAILY_OR_WEEKLY_OR_MONTHLY;
import static org.openmrs.module.messages.api.model.TemplateFieldType.MESSAGING_FREQUENCY_WEEKLY_OR_MONTHLY;
import static org.openmrs.module.messages.api.model.TemplateFieldType.START_OF_MESSAGES;

public final class FieldDateUtil {

    public static final String SEPARATOR = "|";
    private static final String AFTER_TIMES_TEXT = "after %s time(s)";
    private static final String NEVER_TEXT = "never";
    private static final String EMPTY_TEXT = "";
    private static final List<TemplateFieldType> FREQUENCIES =
            Arrays.asList(MESSAGING_FREQUENCY_WEEKLY_OR_MONTHLY, MESSAGING_FREQUENCY_DAILY_OR_WEEKLY_OR_MONTHLY);
    private static final List<TemplateFieldType> DAYS_OF_WEEK = Arrays.asList(DAY_OF_WEEK, DAY_OF_WEEK_SINGLE);
    private static final int OTHER_TYPE_MIN_PARTS = 3;
    private static final String ADHERENCE_REPORT_DAILY = "Adherence report daily";
    private static final String ADHERENCE_REPORT_WEEKLY = "Adherence report weekly";
    private static final String VISIT_REMINDER = "Visit reminder";
    private static final FrequencyType DEFAULT_FREQUENCY_TYPE = FrequencyType.DAILY;

    private static final Log LOG = LogFactory.getLog(FieldDateUtil.class);

    private FieldDateUtil() {
    }

    public static ZonedDateTime getStartDate(List<TemplateFieldValue> templateFieldValues) {
        return getStartDateValue(templateFieldValues)
                .flatMap(FieldDateUtil::parseStringDbDateToServerSideFormat)
                .orElse(getDefaultStartDateValue(templateFieldValues)
                        .flatMap(FieldDateUtil::parseStringDbDateToServerSideFormat)
                        .orElse(null));
    }

    public static ZonedDateTime getEndDate(List<TemplateFieldValue> templateFieldValues, String templateName) {
        final ZonedDateTime startDate = getStartDate(templateFieldValues);
        final FrequencyType frequency = getFrequency(templateFieldValues, templateName);
        final String[] daysOfWeek = getDaysOfWeek(templateFieldValues);

        final Optional<String> templateFieldValue = getEndDateValue(templateFieldValues);

        final Optional<ZonedDateTime> result;

        if (templateFieldValue.isPresent()) {
            result = parseEndDate(templateFieldValue.get(), startDate, frequency, daysOfWeek);
        } else {
            result = getDefaultEndDateValue(templateFieldValues).flatMap(
                    templateFieldDefaultValue -> parseEndDate(templateFieldDefaultValue, startDate, frequency, daysOfWeek));
        }

        return result.map(FieldDateUtil::convertToDateWithMaxTime).orElse(null);
    }

    private static ZonedDateTime convertToDateWithMaxTime(ZonedDateTime date) {
        return date.truncatedTo(ChronoUnit.DAYS).plus(Duration.ofDays(1).minusMillis(1));
    }

    public static String getEndDateText(String endDate) {
        if (isSpecialPhraseFormat(endDate, EndDateType.AFTER_TIMES)) {
            final String value = StringUtils.split(endDate, SEPARATOR)[1];
            return String.format(AFTER_TIMES_TEXT, value);
        } else if (isSpecialPhraseFormat(endDate, EndDateType.NO_DATE)) {
            return NEVER_TEXT;
        } else {
            return parseEndDateSafe(endDate)
                    .map(DateUtil::formatToFrontSideDate)
                    .orElse(StringUtils.defaultString(endDate, EMPTY_TEXT));
        }
    }

    private static Optional<ZonedDateTime> parseEndDate(String dbValue, ZonedDateTime startDate, FrequencyType frequency,
                                                        String[] daysOfWeek) {
        if (StringUtils.isBlank(dbValue)) {
            return Optional.empty();
        }

        final ZonedDateTime result;

        final String[] chain = StringUtils.split(dbValue, SEPARATOR);
        if (chain.length >= 2) {
            final String typeName = chain[0];
            final EndDateType type = EndDateType.fromName(typeName);
            final String value = parseEndDateTail(type, chain);
            result = getDateFromType(type, new EndDateParams(value, startDate, frequency, nullableArrayToList(daysOfWeek)));
        } else {
            result = null;
        }

        return Optional.ofNullable(result);
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
                return getFirstValueOf(templateFieldValues, FREQUENCIES)
                        .filter(FrequencyType::isValidName)
                        .map(FrequencyType::fromName)
                        .orElse(getFirstDefaultValueOf(templateFieldValues, FREQUENCIES)
                                .filter(FrequencyType::isValidName)
                                .map(FrequencyType::fromName)
                                .orElse(DEFAULT_FREQUENCY_TYPE));
        }
    }

    private static String[] getDaysOfWeek(List<TemplateFieldValue> templateFieldValues) {
        final String separator = ",";

        return getFirstValueOf(templateFieldValues, DAYS_OF_WEEK)
                .map(daysOfWeek -> StringUtils.split(daysOfWeek, separator))
                .orElse(getFirstDefaultValueOf(templateFieldValues, DAYS_OF_WEEK)
                        .map(defaultDaysOfWeek -> StringUtils.split(defaultDaysOfWeek, separator))
                        .orElse(new String[0]));
    }

    private static Optional<String> getEndDateValue(List<TemplateFieldValue> templateFieldValues) {
        return getValue(templateFieldValues, END_OF_MESSAGES);
    }

    private static Optional<String> getStartDateValue(List<TemplateFieldValue> templateFieldValues) {
        return getValue(templateFieldValues, START_OF_MESSAGES);
    }

    private static Optional<String> getFirstValueOf(List<TemplateFieldValue> templateFieldValues,
                                                    Iterable<TemplateFieldType> templateFieldTypes) {
        for (TemplateFieldType type : templateFieldTypes) {
            final Optional<String> typeValue = getValue(templateFieldValues, type);
            if (typeValue.isPresent()) {
                return typeValue;
            }
        }

        return Optional.empty();
    }

    private static Optional<String> getDefaultStartDateValue(List<TemplateFieldValue> templateFieldValues) {
        return getDefaultValue(templateFieldValues, START_OF_MESSAGES);
    }

    private static Optional<String> getDefaultEndDateValue(List<TemplateFieldValue> templateFieldValues) {
        return getDefaultValue(templateFieldValues, END_OF_MESSAGES);
    }

    private static Optional<String> getFirstDefaultValueOf(List<TemplateFieldValue> templateFieldValues,
                                                           Iterable<TemplateFieldType> templateFieldTypes) {
        for (TemplateFieldType type : templateFieldTypes) {
            final Optional<String> typeValue = getDefaultValue(templateFieldValues, type);
            if (typeValue.isPresent()) {
                return typeValue;
            }
        }

        return Optional.empty();
    }

    private static Optional<String> getValue(List<TemplateFieldValue> templateFieldValues, TemplateFieldType type) {
        String value = null;

        for (TemplateFieldValue fieldValue : templateFieldValues) {
            if (fieldValue.getTemplateField().getTemplateFieldType().equals(type)) {
                if (value == null) {
                    value = StringUtils.isBlank(fieldValue.getValue()) ? null : fieldValue.getValue();
                } else {
                    LOG.warn(String.format("Found redundant field with type %s in the patient template with id %d. " +
                            "However, only one expected", type.name(), fieldValue.getPatientTemplate().getId()));
                }
            }
        }

        return Optional.ofNullable(value);
    }

    private static Optional<String> getDefaultValue(List<TemplateFieldValue> templateFieldValues, TemplateFieldType type) {
        for (TemplateFieldValue fieldValue : templateFieldValues) {
            if (fieldValue.getTemplateField().getTemplateFieldType().equals(type)) {
                final String defaultValue = fieldValue.getTemplateField().getDefaultValue();
                return Optional.ofNullable(StringUtils.isBlank(defaultValue) ? null : defaultValue);
            }
        }
        return Optional.empty();
    }

    private static ZonedDateTime getDateFromType(EndDateType type, EndDateParams params) {
        return new EndDateFactory().create(type, params).getDate();
    }

    private static Optional<ZonedDateTime> parseStringDbDateToServerSideFormat(String value) {
        if (value == null) {
            return Optional.empty();
        }

        try {
            final LocalDate localDate = LocalDate.parse(value, DateTimeFormatter.ofPattern(DEFAULT_SERVER_SIDE_DATE_FORMAT));
            return Optional.of(ZonedDateTime.of(localDate, LocalTime.MIDNIGHT, DateUtil.getDefaultUserTimeZone()));
        } catch (DateTimeParseException dte) {
            LOG.debug("Ignored date time value, could not parse string: " + value, dte);
            return Optional.empty();
        }
    }

    private static Optional<ZonedDateTime> parseEndDateSafe(String endDate) {
        try {
            return parseEndDate(endDate, null, null, null);
        } catch (DateTimeParseException ex) {
            LOG.debug("Ignored endDate value, could not parse string: " + endDate, ex);
            return Optional.empty();
        }
    }

    private static boolean isSpecialPhraseFormat(String endDate, EndDateType specialPhrase) {
        String[] chain = StringUtils.split(endDate, SEPARATOR);
        return chain != null && chain.length > 1 && StringUtils.equals(chain[0], specialPhrase.getName());
    }
}
