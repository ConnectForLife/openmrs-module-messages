/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import _ from 'lodash'

import { ObjectUI } from './object-ui';
import { IFormField } from './form-field';
import { ITemplateField, getDefaultValue } from './template-field.model';
import { TemplateFieldType } from './template-field-type';
import { TemplateFieldDefaultValue } from './template-field-default-value.model';

export class TemplateFieldUI extends ObjectUI<ITemplateField> implements ITemplateField, IFormField {
  id: number | null;
  name: string;
  mandatory: boolean;
  defaultValue: string;
  type: TemplateFieldType;
  defaultValues: Array<TemplateFieldDefaultValue>;
  possibleValues: Array<string>;

  isEnabled: boolean;
  isEdited: boolean;
  isTouched: boolean;

  constructor(model: ITemplateField) {
    super(model);

    this.isEnabled = true;
    this.isEdited = false;
    this.isTouched = false;

    if (!this.type) {
      throw new Error("Type must be set.");
    }
  }

  get localId(): string {
    return this.id ? this.id.toString() : super.getLocalId();
  }

  get isNew(): boolean {
    return !this.id;
  }

  static getNew(): TemplateFieldUI {
    return new TemplateFieldUI(getDefaultValue());
  }
}
