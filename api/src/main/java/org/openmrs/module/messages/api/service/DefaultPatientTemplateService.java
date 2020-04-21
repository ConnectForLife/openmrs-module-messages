package org.openmrs.module.messages.api.service;

import java.util.List;
import org.openmrs.Patient;
import org.openmrs.module.messages.api.dto.MessageDetailsDTO;
import org.openmrs.module.messages.api.model.PatientTemplate;

/**
 * Provides methods related to the creating of default patient templates
 */
public interface DefaultPatientTemplateService {

    /**
     * Creates default patient templates for patients who have not yet saved patient templates
     *
     * @param patient object of patient for whom patient templates are created
     * @return
     */
    List<PatientTemplate> generateDefaultPatientTemplates(Patient patient);

    /**
     * Finds patient templates which are not yet saved for patient
     *
     * @param patient object of patient for whom lacking patient templates are sought
     * @return
     */
    List<PatientTemplate> findLackingPatientTemplates(Patient patient);

    /**
     * Finds patient templates which are not yet saved for patient
     *
     * @param patient object of patient for whom lacking patient templates are sought
     * @param existing list of patient templates that already exist for patient
     * @return
     */
    List<PatientTemplate> findLackingPatientTemplates(Patient patient,
                                                      List<PatientTemplate> existing);

    /**
     * Finds detailed information about patient templates
     *
     * @param patient object of patient for whom detailed information from patient templates are sought
     * @return DTO object containing detailed data
     */
    MessageDetailsDTO getDetailsForRealAndDefault(Patient patient);

    /**
     * Finds detailed information about patient templates
     *
     * @param patientById object of patient for whom detailed information from patient templates are sought
     * @param lacking list of patient templates which are not yet saved for patient
     * @return DTO object containing detailed data
     */
    MessageDetailsDTO getDetailsForRealAndDefault(Patient patientById, List<PatientTemplate> lacking);
}
