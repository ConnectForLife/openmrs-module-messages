/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import moment, { Moment } from "moment";

export const getUtcTimeToString = (time?: Moment): string | undefined => {
  return !!time ? moment.utc(time, 'HH:mm').format('HH:mm') : undefined;
}

export const getLocalTimeFromString = (time: string): Moment | undefined => {
  return !!time ? moment.utc(time, 'HH:mm').local() : undefined;
}

export const getTimeToString = (time?: Moment): string | undefined => {
  return !!time ? moment(time, 'HH:mm').format('HH:mm') : undefined;
}

export const getTimeFromString = (time: string): Moment | undefined => {
  return !!time ? moment(time, 'HH:mm') : undefined;
}