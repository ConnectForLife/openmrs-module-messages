/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import { combineReducers } from 'redux';
import { reducers as openmrs } from '@openmrs/react-components';
import patientTemplate, { PatientTemplateState } from './patient-template.reducer';
import bestContactTime, { BestContactTimeState } from './best-contact-time.reducer';
import calendar, { CalendarState } from './calendar.reducer';

export interface IRootState {
  readonly openmrs: any;
  readonly patientTemplate: PatientTemplateState;
  readonly bestContactTime: BestContactTimeState;
  readonly calendar: CalendarState;
}

export default combineReducers<IRootState>({
  openmrs,
  patientTemplate,
  bestContactTime,
  calendar
});
