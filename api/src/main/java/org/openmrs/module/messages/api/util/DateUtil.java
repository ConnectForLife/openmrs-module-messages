/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.util;

import org.apache.commons.lang.time.DateUtils;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.service.MessagesAdministrationService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

public final class DateUtil {

  public static final int DAY_IN_SECONDS = 24 * 60 * 60;

  /** The date-time pattern which contains only hour of day and minute of hour fields. */
  public static final String HOUR_AND_MINUTE_PATTERN = "HH:mm";

  /**
   * The main Clock used by all methods of this class. This facilitates unit tests which needs
   * different time zones.
   */
  private static Clock clock = Clock.system(ZoneId.systemDefault());

  private DateUtil() {}

  /**
   * Obtains the current date-time with system's default time zone.
   *
   * @return new instance of ZonedDateTime, never null
   * @see #getDefaultSystemTimeZone()
   */
  public static ZonedDateTime now() {
    return ZonedDateTime.now(clock);
  }

  /**
   * Gets the default TimeZone of the Java virtual machine.
   *
   * @return the default TimeZone, never null
   */
  public static ZoneId getDefaultSystemTimeZone() {
    return clock.getZone();
  }

  /**
   * Gets the default user timezone as configured by {@link ConfigConstants#DEFAULT_USER_TIMEZONE}
   * global property or system's default timezone.
   *
   * @return the ZoneId which represents default user timezone, never null
   */
  public static ZoneId getDefaultUserTimeZone() {
    final ZoneId result;

    if (Context.isSessionOpen()) {
      result =
          ofNullable(
                  Context.getAdministrationService()
                      .getGlobalProperty(ConfigConstants.DEFAULT_USER_TIMEZONE))
              .map(ZoneId::of)
              .orElse(getDefaultSystemTimeZone());
    } else {
      result = getDefaultSystemTimeZone();
    }

    return result;
  }

  /**
   * Get timezone of a person or system's default timezone.
   *
   * @param person the person to get timezone for, not null
   * @return the ZoneId which represents person's timezone, never null
   */
  public static ZoneId getPersonTimeZone(Person person) {
    return ofNullable(
            Context.getService(MessagesAdministrationService.class)
                .getGlobalProperty(ConfigConstants.DEFAULT_USER_TIMEZONE, person))
        .map(ZoneId::of)
        .orElse(getDefaultSystemTimeZone());
  }

  /**
   * Obtains a date time using milliseconds from the epoch of 1970-01-01T00:00:00Z and default user
   * timezone.
   *
   * @param epochMilli the number of milliseconds from 1970-01-01T00:00:00Z
   * @return the ZonedDateTime, never null
   * @see #getDefaultUserTimeZone()
   */
  public static ZonedDateTime ofEpochMilliInUserTimeZone(long epochMilli) {
    return ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(epochMilli), DateUtil.getDefaultUserTimeZone());
  }

  /**
   * Converts ZonedDateTime to Date.
   *
   * <p>The result will represent the same point in time, but the information about {@code date}'s
   * timezone is lost.
   *
   * @param date the zoned date to convert, no null
   * @return the Date, never null
   */
  public static Date toDate(ZonedDateTime date) {
    return Date.from(date.toInstant());
  }

  /**
   * Converts Date object which was read from OpenMRS database into an ZonedDateTime with a default
   * system timezone.
   *
   * <p>The date times in general OpenMRS are saved into database with timezone of the JVM the app
   * is running on. The database will convert the date&time from connection timezone (JVMs) into
   * UTC, for both read and write so the database timezone is transparent.
   *
   * @return the converted {@code dbDate} or null if {@code dbDate} is null
   * @see #getDefaultSystemTimeZone()
   */
  public static ZonedDateTime convertOpenMRSDatabaseDate(Date dbDate) {
    if (dbDate == null) {
      return null;
    }

    final ZonedDateTime result;

    if (dbDate instanceof java.sql.Date) {
      // SQL Date contains only a LocalDate part
      final java.sql.Date sqlDateOnly = (java.sql.Date) dbDate;
      result =
          ZonedDateTime.of(
              sqlDateOnly.toLocalDate(), LocalTime.MIDNIGHT, getDefaultSystemTimeZone());
    } else {
      result = ZonedDateTime.ofInstant(dbDate.toInstant(), getDefaultSystemTimeZone());
    }

    return result;
  }

  /**
   * Obtains a new instance of Zoned Date Time parsed from the {@code date}. Only the <b>local
   * date</b> part is parsed, the time part is set to midnight. The Time-zone {@code zoneId} must be
   * the time-zone of date stored as {@code date}.
   *
   * @param date the string to parse, not null
   * @param zoneId the zoneId representing the timezone of the {@code date}, not null
   * @return the new instance of Zoned Date Time, never null
   * @see #getDefaultSystemTimeZone()
   */
  public static ZonedDateTime parseServerSideDate(String date, ZoneId zoneId) {
    final LocalDate localDate =
        LocalDate.parse(
            date, DateTimeFormatter.ofPattern(MessagesConstants.DEFAULT_SERVER_SIDE_DATE_FORMAT));
    return ZonedDateTime.of(localDate, LocalTime.MIDNIGHT, zoneId);
  }

  /**
   * Obtains a string representation of a date time where, the Time Zone of the {@code dateTime} is
   * changed to default user time zone and the {@link
   * MessagesConstants#DEFAULT_SERVER_SIDE_DATETIME_FORMAT} is used to format the date time.
   *
   * @param dateTime the date time to format, not null
   * @return the string representation of {@code dateTime} in DEFAULT_SERVER_SIDE_DATETIME_FORMAT
   *     format, never null
   */
  public static String formatToServerSideDateTime(ZonedDateTime dateTime) {
    requireNonNull(dateTime);
    return dateTime
        .withZoneSameInstant(getDefaultUserTimeZone())
        .format(DateTimeFormatter.ofPattern(MessagesConstants.DEFAULT_SERVER_SIDE_DATETIME_FORMAT));
  }

  /**
   * Obtains a string representation of a date where, the Time Zone of {@code date} is changed to
   * default user time zone and the {@link MessagesConstants#DEFAULT_SERVER_SIDE_DATE_FORMAT} is
   * used to format the date time.
   *
   * @param date the date to format, not null
   * @return the string representation of {@code date} in DEFAULT_SERVER_SIDE_DATE_FORMAT format,
   *     never null
   * @see #formatToFrontSideDate(ZonedDateTime)
   */
  public static String formatToServerSideDate(ZonedDateTime date) {
    requireNonNull(date);
    return date.withZoneSameInstant(getDefaultUserTimeZone())
        .format(DateTimeFormatter.ofPattern(MessagesConstants.DEFAULT_SERVER_SIDE_DATE_FORMAT));
  }

  /**
   * Obtains a string representation of a date where, the Time Zone of {@code date} is changed to
   * default user time zone and the {@link MessagesConstants#DEFAULT_FRONT_END_DATE_FORMAT}
   *
   * @param date the date to format, not null
   * @return the string representation of {@code date} in DEFAULT_FRONT_END_DATE_FORMAT format,
   *     never null
   * @see #formatToServerSideDateTime(ZonedDateTime)
   */
  public static String formatToFrontSideDate(ZonedDateTime date) {
    requireNonNull(date);
    return date.withZoneSameInstant(getDefaultUserTimeZone())
        .format(DateTimeFormatter.ofPattern(MessagesConstants.DEFAULT_FRONT_END_DATE_FORMAT));
  }

  /**
   * Converts string representation of some date in {@link
   * MessagesConstants#DEFAULT_SERVER_SIDE_DATE_FORMAT} to string representation in {@link
   * MessagesConstants#DEFAULT_FRONT_END_DATE_FORMAT}.
   *
   * @param date the string representation to convert, not null
   * @return the string representation in DEFAULT_FRONT_END_DATE_FORMAT, never null
   */
  public static String convertServerSideDateFormatToFrontend(String date) {
    // We just take 'some date' and print it in another format
    final LocalDate localdate =
        LocalDate.parse(
            date, DateTimeFormatter.ofPattern(MessagesConstants.DEFAULT_SERVER_SIDE_DATE_FORMAT));
    return localdate.format(
        DateTimeFormatter.ofPattern(MessagesConstants.DEFAULT_FRONT_END_DATE_FORMAT));
  }

  /**
   * Obtains a string representation of a date time in given format.
   *
   * @param dateTime the date time to get string representation for
   * @param toFormat the format to use, not null
   * @return the string representation of {@code dateTime} in {@code toFormat} format or empty
   *     string if the {@code dateTime is null}
   */
  public static String formatDateTime(ZonedDateTime dateTime, String toFormat) {
    requireNonNull(toFormat);

    if (dateTime == null) {
      return "";
    }

    return dateTime.format(DateTimeFormatter.ofPattern(toFormat));
  }

  /**
   * Adds day(s) to given date and returns new date.
   *
   * @param date the date we want to add days to
   * @param days number of days we want to add
   * @return new date (date + days)
   */
  public static Date addDaysToDate(Date date, int days) {
    return DateUtils.addDays(date, days);
  }

  /**
   * Gets a Date object representing an instant of time where the date-part is equal to the
   * date-part of {@code date} and time-part is equal to time parsed from {@code timeOfDay}, the
   * seconds and milliseconds are both 0.
   *
   * <p>The {@code timeOfDay} has to fit {@link #HOUR_AND_MINUTE_PATTERN} pattern.
   *
   * <p>Example: <br>
   * For a {@code date} of `2001-07-04T12:08:56.235` and {@code timeOfDay} equal to `14:11`, the
   * result of this method will be equal to `2001-07-04T14:11:00.000`.
   *
   * @param date the source of date-part, not null
   * @param timeOfDay the source of time-part, not null
   * @param timeZone the time zone of the {@code date} and {@code timeOfDay}, not null
   * @return the Date object with date-time part from {{@code date} and time-part from {@code
   *     timeOfDay}, never null
   * @throws IllegalArgumentException if {@code timeOfDay} has illegal value
   */
  public static Date getDateWithTimeOfDay(Date date, String timeOfDay, TimeZone timeZone) {
    final SimpleDateFormat timeFormat = new SimpleDateFormat(HOUR_AND_MINUTE_PATTERN);
    timeFormat.setTimeZone(timeZone);

    try {
      final Calendar timePart =
          org.apache.commons.lang3.time.DateUtils.toCalendar(timeFormat.parse(timeOfDay));
      timePart.setTimeZone(timeZone);

      final Calendar result = org.apache.commons.lang3.time.DateUtils.toCalendar(date);
      result.setTimeZone(timeZone);
      result.set(Calendar.HOUR_OF_DAY, timePart.get(Calendar.HOUR_OF_DAY));
      result.set(Calendar.MINUTE, timePart.get(Calendar.MINUTE));
      result.set(Calendar.SECOND, 0);
      result.set(Calendar.MILLISECOND, 0);
      return result.getTime();
    } catch (ParseException pe) {
      throw new IllegalArgumentException(
          "Illegal value of timeOfDay="
              + timeOfDay
              + "; expected pattern: "
              + HOUR_AND_MINUTE_PATTERN,
          pe);
    }
  }

  /**
   * Converts ZoneId object into TimeZone object.
   *
   * @param zoneId object of ZoneId type
   * @return TimeZone object
   */
  public static TimeZone convertZoneIdToTimeZone(ZoneId zoneId) {
    return TimeZone.getTimeZone(zoneId);
  }
}
