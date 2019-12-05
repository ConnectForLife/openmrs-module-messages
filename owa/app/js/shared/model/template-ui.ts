import _ from 'lodash'

import { ObjectUI } from './object-ui';
import { ITemplate, getDefaultValue } from './template.model';
import { TemplateFieldUI } from './template-field-ui';

export class TemplateUI extends ObjectUI<ITemplate> implements ITemplate {
  id: number | null;
  name: string | null;
  templateFields: Array<TemplateFieldUI>;

  constructor(model: ITemplate) {
    super(model);
    this.templateFields = _.map(model.templateFields,
      templateField => new TemplateFieldUI(templateField));
  }

  get localId(): string {
    return this.id ? this.id.toString() : super.getLocalId();
  }

  get isNew(): boolean {
    return !this.id;
  }

  static getNew(): TemplateUI {
    return new TemplateUI(getDefaultValue());
  }

  static fromModel(modal: ITemplate) {
    return new TemplateUI(modal);
  }
}
