package org.openmrs.module.messages.api.service;

import org.openmrs.api.APIException;
import org.openmrs.module.messages.api.dto.PatientTemplateDTO;
import org.openmrs.module.messages.api.model.PatientTemplate;

import java.util.List;

public interface PatientTemplateService extends BaseOpenmrsCriteriaDataService<PatientTemplate> {

    List<PatientTemplate> batchSave(List<PatientTemplateDTO> patientTemplates, int patientId) throws APIException;
}
