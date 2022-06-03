/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import { IPatientTemplate } from './patient-template.model';
import IMessageDetailsWrapped from './message-details';

export interface IDefaultValuesState {
  defaultValuesUsed: boolean | undefined;
  allValuesDefault: boolean | undefined;
  lackingPatientTemplates: Array<IPatientTemplate>;
  details: IMessageDetailsWrapped | undefined;
}

export const getDefaultValue = (): IDefaultValuesState => ({
  defaultValuesUsed: undefined,
  allValuesDefault: undefined,
  lackingPatientTemplates: [],
  details: undefined
});
