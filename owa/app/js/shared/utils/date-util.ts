/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import { parse } from 'date-fns';

const DEFAULT: Date = parse(Date.now());

export type ParsableToDate = number | string | Date;

export const isDateValid = (date: Date) => !isNaN(date.valueOf());

export const parseDateOrDefault = (date: ParsableToDate,
  defaultDate: Date | undefined): Date => {
  if (defaultDate instanceof Date && !isDateValid(defaultDate)) {
    throw 'Default date is not valid';
  }

  const defaultValue = (!!defaultDate) ? defaultDate : DEFAULT;
  const parsed = parse(date);
  return isDateValid(parsed) ? parsed : defaultValue;
}

export const parseOrNow = (date: ParsableToDate): Date => {
  const parsed = parse(date);
  return isDateValid(parsed) ? parsed : DEFAULT;
}
