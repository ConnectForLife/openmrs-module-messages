import { ITemplateFieldValue } from './template-field-value.model';

export interface IPatientTemplate {
  id: number | null;
  uuid: string | null;
  templateFieldValues: Array<ITemplateFieldValue>;
  patientId: number | undefined;
  templateId: number | undefined;
  actorId: number | undefined;
  actorTypeId: number | undefined;
  serviceQuery: string;
  serviceQueryType: string;
}

export const getDefaultValue = (): IPatientTemplate => ({
  id: null,
  uuid: null,
  templateFieldValues: [] as Array<ITemplateFieldValue>,
  patientId: undefined,
  templateId: undefined,
  actorId: undefined,
  actorTypeId: undefined,
  serviceQuery: '',
  serviceQueryType: ''
});
