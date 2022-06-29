/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import { ITemplateFieldValue } from './template-field-value.model';

export interface IPatientTemplate {
  id: number | null;
  uuid: string | null;
  templateFieldValues: Array<ITemplateFieldValue>;
  patientId: number | undefined;
  templateId: number | undefined;
  actorId: number | undefined;
  actorTypeId: number | undefined;
}

export const getDefaultValue = (): IPatientTemplate => ({
  id: null,
  uuid: null,
  templateFieldValues: [] as Array<ITemplateFieldValue>,
  patientId: undefined,
  templateId: undefined,
  actorId: undefined,
  actorTypeId: undefined
});
