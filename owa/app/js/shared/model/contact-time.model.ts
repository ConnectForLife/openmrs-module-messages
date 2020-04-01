/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import { Moment } from "moment";
import _ from 'lodash';
import { getTimeFromString, getTimeToString } from "../utils/time-util";

export interface IContactTime {
  time?: Moment
}

export const mapFromRequest = (element) => {
  const result = _.clone(element);
  result.time = getTimeFromString(element.time);
  return result;
};

export const mapToRequest = (element: IContactTime) => {
  const result = _.clone(element);
  return { ...result, time: getTimeToString(element.time) };
};
