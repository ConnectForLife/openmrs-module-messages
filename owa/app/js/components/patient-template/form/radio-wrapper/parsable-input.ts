/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

export const SEPARATOR = '|';
export const EMPTY = 'EMPTY';

export enum InputTypeEnum {
  NO_DATE = 'NO_DATE',
  AFTER_TIMES = 'AFTER_TIMES',
  DATE_PICKER = 'DATE_PICKER',
  OTHER = 'OTHER'
}

export type ValueType = {
  name: string;
  value: string;
}

export type InputParser = {
  parse: (value: string) => string,
  serialize: (value: string) => string
}
