/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import _ from 'lodash';
import { TemplateFieldType } from './template-field-type';
import { TemplateFieldDefaultValue } from './template-field-default-value.model';

export interface ITemplateField {
  id: number | null;
  name: string;
  mandatory: boolean;
  defaultValue: string;
  type: TemplateFieldType;
  defaultValues: Array<TemplateFieldDefaultValue>;
  possibleValues: Array<string>;
  // value_concept is skipped for now
}

export const getDefaultValue = (): ITemplateField => ({
  id: null,
  name: '',
  mandatory: true,
  defaultValue: '',
  type: TemplateFieldType.DAY_OF_WEEK,
  defaultValues: [],
  possibleValues: []
});
