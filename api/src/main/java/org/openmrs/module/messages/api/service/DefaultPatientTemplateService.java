package org.openmrs.module.messages.api.service;

import java.util.List;
import org.openmrs.Patient;
import org.openmrs.module.messages.api.dto.MessageDetailsDTO;
import org.openmrs.module.messages.api.model.PatientTemplate;

public interface DefaultPatientTemplateService {

    List<PatientTemplate> generateDefaultPatientTemplates(Patient patient);

    List<PatientTemplate> findLackingPatientTemplates(Patient patient);

    List<PatientTemplate> findLackingPatientTemplates(Patient patient,
                                                      List<PatientTemplate> existing);

    MessageDetailsDTO getDetailsForRealAndDefault(Patient patient);

    MessageDetailsDTO getDetailsForRealAndDefault(Patient patientById, List<PatientTemplate> lacking);
}
