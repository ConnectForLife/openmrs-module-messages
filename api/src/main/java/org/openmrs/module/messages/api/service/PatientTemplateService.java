package org.openmrs.module.messages.api.service;

import org.openmrs.api.APIException;
import org.openmrs.module.messages.api.dto.PatientTemplateDTO;
import org.openmrs.module.messages.api.model.PatientTemplate;

import java.util.List;

/**
 * Provides methods for creating, reading, updating and deleting patient templates entities
 */
public interface PatientTemplateService extends BaseOpenmrsCriteriaDataService<PatientTemplate> {

    /**
     * Updates list of patient templates
     *
     * @param patientTemplates list of patient templates to update
     * @param patientId id of patient for whom patient templates are updated
     * @return list of updated patient templates
     * @throws APIException
     */
    List<PatientTemplate> batchSave(List<PatientTemplateDTO> patientTemplates, int patientId) throws APIException;
}
