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
}

export const parseOrNow = (date: ParsableToDate): Date => {
  const parsed = parse(date);
  return isDateValid(parsed) ? parsed : parse(Date.now());
}
