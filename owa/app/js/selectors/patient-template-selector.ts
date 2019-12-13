import { PatientTemplateUI } from '../shared/model/patient-template-ui';

export const getPatientTemplateWithTemplateId = (patientTemplates: ReadonlyArray<PatientTemplateUI>, 
    templateId: number): ReadonlyArray<PatientTemplateUI> =>
  patientTemplates.filter(pt => pt.templateId === templateId);