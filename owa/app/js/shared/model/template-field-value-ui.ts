import _ from 'lodash'

import { ObjectUI } from './object-ui';
import { ITemplateFieldValue, getDefaultValue } from './template-field-value.model';
import { IFormField } from './form-field';
import { TemplateUI } from './template-ui';
import { TemplateFieldUI } from './template-field-ui';

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

  static getNew(templateField: TemplateFieldUI): TemplateFieldValueUI {
    return new TemplateFieldValueUI({
      ...getDefaultValue(),
      templateFieldId: templateField.id!,
      value: templateField.defaultValue,
    });
  }
}
