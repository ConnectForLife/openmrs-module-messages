import _ from 'lodash'

import { ObjectUI } from './object-ui';
import { IPatientTemplate, getDefaultValue } from './patient-template.model';
import { TemplateFieldValueUI } from './template-field-value-ui';
import { TemplateUI } from './template-ui';
import { IForm } from './form';
import { validateFormSafely } from '@bit/soldevelo-omrs.cfl-components.validation';
import { IActor } from './actor.model';
import { prepareFieldValues } from '../utils/end-date-util';

export class PatientTemplateUI extends ObjectUI<IPatientTemplate> implements IPatientTemplate, IForm {
  id: number | null;
  uuid: string | null;
  templateFieldValues: Array<TemplateFieldValueUI>;
  patientId: number;
  templateId: number;
  actorId: number;
  actorTypeId: number;
  isPersisted: boolean;

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

    const validationResult = await validateFormSafely(form, schema);

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

  hasErrors(): boolean {
    return Object.keys(this.errors).length != 0;
  }

  static getNew(patientId: number, template: TemplateUI, relationshipDirection?: string, relationshipTypeId?: number, actor?: IActor): PatientTemplateUI {
    const actorId = actor && actor.actorId ? actor.actorId : patientId;
    const actorTypeId = actor && actor.actorTypeId ? actor.actorTypeId : undefined;
    const templateFields = prepareFieldValues(template.templateFields, relationshipTypeId, relationshipDirection);
    return new PatientTemplateUI({
      ...getDefaultValue(),
      patientId,
      actorId,
      actorTypeId,
      templateId: template.id!,
      templateFieldValues: _.map(templateFields, item  => TemplateFieldValueUI.getNew(item, relationshipDirection, relationshipTypeId))
    });
  }

  static fromModel(modal: IPatientTemplate) {
    return new PatientTemplateUI(modal);
  }
}
