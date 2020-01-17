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
