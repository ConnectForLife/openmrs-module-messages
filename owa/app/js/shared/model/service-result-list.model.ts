/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import { IServiceResult } from "./service-result.model";
import { Moment } from 'moment';

export interface IServiceResultList {
  patientId: number | null;
  actorId: number | null;
  serviceId: number | null;
  serviceName: string;
  startDate: Moment | null;
  endDate: Moment | null;
  results: ReadonlyArray<IServiceResult>
}

export const getDefaultValue = (): IServiceResultList => ({
  patientId: null,
  actorId: null,
  serviceId: null,
  startDate: null,
  endDate: null,
  results: [],
  serviceName: ''
});
