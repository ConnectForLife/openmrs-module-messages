import { PatientTemplateUI } from '../shared/model/patient-template-ui';

export const getPatientTemplateWithTemplateId = (patientTemplates: ReadonlyArray<PatientTemplateUI>, templateId: number) =>
  patientTemplates.find(pt => pt.templateId === templateId);