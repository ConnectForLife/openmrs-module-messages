/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import _ from 'lodash'

import { ObjectUI } from './object-ui';
import { ITemplateFieldValue, getDefaultValue } from './template-field-value.model';
import { IFormField } from './form-field';
import { TemplateUI } from './template-ui';
import { TemplateFieldUI } from './template-field-ui';
import { TemplateFieldType } from './template-field-type';

export class TemplateFieldValueUI extends ObjectUI<ITemplateFieldValue> implements ITemplateFieldValue, IFormField {
  id: number | null;
  templateFieldId: number;
  value: string;

  isEnabled: boolean;
  isEdited: boolean;
  isTouched: boolean;

  constructor(model: ITemplateFieldValue) {
    super(model);

    this.isEnabled = true;
    this.isEdited = false;
    this.isTouched = false;

    if (!this.templateFieldId) {
      throw new Error("Id must be set.");
    }
  }

  get localId(): string {
    return this.templateFieldId.toString();
  }

  get isNew(): boolean {
    return !this.id;
  }

  getFieldName(template: TemplateUI): string {
    return _(template.templateFields)
      .filter(templateField => templateField.id === this.templateFieldId)
      .first()!
      .name;
  }

  isMandatory(template: TemplateUI): boolean {
    return _(template.templateFields)
      .filter(templateField => templateField.id === this.templateFieldId)
      .first()!
      .mandatory;
  }

  getFieldType(template: TemplateUI): TemplateFieldType {
    return _(template.templateFields)
      .filter(templateField => templateField.id === this.templateFieldId)
      .first()!
      .type;
  }

  static getNew(templateField: TemplateFieldUI): TemplateFieldValueUI {
    return new TemplateFieldValueUI({
      ...getDefaultValue(),
      templateFieldId: templateField.id!,
      value: templateField.defaultValue,
    });
  }
}
