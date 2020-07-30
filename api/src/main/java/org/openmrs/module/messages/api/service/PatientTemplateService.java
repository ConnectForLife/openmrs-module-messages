package org.openmrs.module.messages.api.service;

import org.openmrs.api.APIException;
import org.openmrs.api.handler.BaseVoidHandler;
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

    /**
     * Voids patient templates for person which acts as a patient or actor.
     *
     * @param personId id of person for whom patient templates are voided
     * @param reason reason for voiding patient
     * @throws APIException
     */
    void voidForPerson(int personId, String reason) throws APIException;

    /**
     * Voids patient template (functionally delete patient template from system).
     *
     * @param patientTemplate object which should be voided
     * @param reason reason for voiding patient
     * @return the voided patient template
     * @see BaseVoidHandler
     * @throws APIException
     */
    PatientTemplate voidPatientTemplate(PatientTemplate patientTemplate, String reason) throws APIException;
}
