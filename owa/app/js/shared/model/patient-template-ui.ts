import _ from 'lodash'

import { ObjectUI } from './object-ui';
import { IPatientTemplate, getDefaultValue } from './patient-template.model';
import { TemplateFieldValueUI } from './template-field-value-ui';
import { TemplateUI } from './template-ui';
import { IForm } from './form';
import { validateFormAsSuccess } from '../utils/validation-util';

export class PatientTemplateUI extends ObjectUI<IPatientTemplate> implements IPatientTemplate, IForm {
  id: number | null;
  templateFieldValues: Array<TemplateFieldValueUI>;
  patientId: number;
  templateId: number;
  actorId: number;
  actorTypeId: number;

  errors: {[key: string]: string};

  constructor(model: IPatientTemplate) {
    super(model);
    this.templateFieldValues = _.map(model.templateFieldValues,
      templateFieldValue => new TemplateFieldValueUI(templateFieldValue));
    this.errors = {};
  }

  async validate(templates: Array<TemplateUI>, validateNotTouched: boolean): Promise<PatientTemplateUI> {
    const form = _.chain(this.templateFieldValues)
      .keyBy(v => v.templateFieldId)
      .mapValues(v => v)
      .value()

    const schema = _(templates)
      .filter(template => template.id === this.templateId)
      .first()!
      .getValidationSchema(validateNotTouched);

    const validationResult = await validateFormAsSuccess(form, schema);

    const patientTemplate = _.clone(this);
    patientTemplate.errors = validationResult;
    return patientTemplate;
  }

  get localId(): string {
    return this.id ? this.id.toString() : super.getLocalId();
  }

  get isNew(): boolean {
    return !this.id;
  }

  static getNew(template: TemplateUI): PatientTemplateUI {
    return new PatientTemplateUI({
      ...getDefaultValue(),
      templateId: template.id!,
      templateFieldValues: _.map(template.templateFields, TemplateFieldValueUI.getNew)
    });
  }

  static fromModel(modal: IPatientTemplate) {
    return new PatientTemplateUI(modal);
  }
}
