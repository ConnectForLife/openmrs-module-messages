/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import { startOfDay, format, parse } from 'date-fns';
import { ISO_DATE_FORMAT } from './date-util';

const defaultValues = {
  START_OF_MESSAGES: format(startOfDay(Date.now()), ISO_DATE_FORMAT)
}

const isValueAvailable = (value: string) => {
  return !!value;
}

const prepareDefaultValue = (currentDefault: string, fieldType: string): string => {
  if (!isValueAvailable(currentDefault) && isValueAvailable(defaultValues[fieldType])) {
    return defaultValues[fieldType];
  } else {
    return currentDefault;
  }
}

export default prepareDefaultValue;
