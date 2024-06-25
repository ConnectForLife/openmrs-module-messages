/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service;

import org.openmrs.Patient;
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
     * @param patientId        id of patient for whom patient templates are updated
     * @return list of updated patient templates
     * @throws APIException
     */
    List<PatientTemplate> batchSave(List<PatientTemplateDTO> patientTemplates, int patientId) throws APIException;

    /**
     * Voids patient templates for person which acts as a patient or actor.
     *
     * @param personId id of person for whom patient templates are voided
     * @param reason   reason for voiding patient templates
     * @throws APIException
     */
    void voidForPerson(int personId, String reason) throws APIException;

    /**
     * Voids patient templates which are based on specific relationship.
     * Could be called after removing connection between people in order to avoid sending undesired messages.
     *
     * @param relationshipId id of related relationship
     * @param reason         reason for voiding patient templates
     * @throws APIException
     */
    void voidForRelationship(int relationshipId, String reason);

    /**
     * Voids patient template (functionally delete patient template from system).
     *
     * @param patientTemplate object which should be voided
     * @param reason          reason for voiding patient
     * @return the voided patient template
     * @throws APIException
     * @see BaseVoidHandler
     */
    PatientTemplate voidPatientTemplate(PatientTemplate patientTemplate, String reason) throws APIException;

    PatientTemplate createVisitReminder(String channel, String patientUuid);

  /**
   * Get {@link PatientTemplate} related to the {@code patient} for template with name {@code templateName}.
   * If there is no such PatientTemplate then this method creates one.
   * <p>
   * The {@code templateName} Template must exist.
   * </p>
   *
   * @param patient      the Patient to get PatientTemplate for, not null
   * @param templateName the Template name, not null
   * @return the {@link PatientTemplate} for {@code patient} and {@code templateName} Template, never null
   * @throws APIException if the PatientTemplate could not be read, or created, or Template with {@code templateName}
   *                      doesn't exist
   */
  PatientTemplate getOrBuildPatientTemplate(final Patient patient, final String templateName)
      throws APIException;
}
