/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import { parse } from 'date-fns';
import moment from 'moment-timezone';

export const ISO_DATE_FORMAT = 'YYYY-MM-DD';
export const DATE_FORMAT = 'dd MMM yyyy';
export const WEEK_DAYS_KEYS = [
  'cfl.weekDay.Sunday.shortName',
  'cfl.weekDay.Monday.shortName',
  'cfl.weekDay.Tuesday.shortName',
  'cfl.weekDay.Wednesday.shortName',
  'cfl.weekDay.Thursday.shortName',
  'cfl.weekDay.Friday.shortName',
  'cfl.weekDay.Saturday.shortName'
];
export const MONTH_NAMES_KEYS = [
  'cfl.month.January.fullName',
  'cfl.month.February.fullName',
  'cfl.month.March.fullName',
  'cfl.month.April.fullName',
  'cfl.month.May.fullName',
  'cfl.month.June.fullName',
  'cfl.month.July.fullName',
  'cfl.month.August.fullName',
  'cfl.month.September.fullName',
  'cfl.month.October.fullName',
  'cfl.month.November.fullName',
  'cfl.month.December.fullName'
];

export type ParsableToDate = number | string | Date;

export const isDateValid = (date: Date | undefined) =>
  !!date && date instanceof Date && !isNaN(date.valueOf());

export const parseDateOrDefault = (date: ParsableToDate,
    defaultDate: Date): Date => {
  if (!(defaultDate instanceof Date) || !isDateValid(defaultDate)) {
    throw 'Default date is not valid';
  }

  const parsed = parse(date);
  return isDateValid(parsed) ? parsed : defaultDate;
};

export const parseOrNow = (date: ParsableToDate): Date => {
  const parsed = parse(date);
  return isDateValid(parsed) ? parsed : parse(Date.now());
};

/**
 * Checks whenever both timezones have the same offset at the time of function's call.
 */
export const isSameTimezoneOffset = (timezoneA: string, timezoneB: string): boolean => {
  const timezoneAOffset = moment().tz(timezoneA).utcOffset();
  const timezoneBOffset = moment().tz(timezoneB).utcOffset();
  return timezoneAOffset == timezoneBOffset;
};
