import _ from 'lodash'

import { ObjectUI } from './object-ui';
import { IPatientTemplate, getDefaultValue } from './patient-template.model';
import { TemplateFieldValueUI } from './template-field-value-ui';

export class PatientTemplateUI extends ObjectUI<IPatientTemplate> implements IPatientTemplate {
  id: number | null;
  templateFieldValues: Array<TemplateFieldValueUI>;
  patientId: number;
  templateId: number;
  actorId: number;
  actorTypeId: number;

  constructor(model: IPatientTemplate) {
    super(model);
    this.templateFieldValues = _.map(model.templateFieldValues,
      templateField => new TemplateFieldValueUI(templateField));
  }

  get localId(): string {
    return this.id ? this.id.toString() : super.getLocalId();
  }

  get isNew(): boolean {
    return !this.id;
  }

  static getNew(): PatientTemplateUI {
    return new PatientTemplateUI(getDefaultValue());
  }

  static fromModel(modal: IPatientTemplate) {
    return new PatientTemplateUI(modal);
  }
}
