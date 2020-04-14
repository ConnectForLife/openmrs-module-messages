package org.openmrs.module.messages.api.helper;

import org.openmrs.module.messages.api.model.PatientTemplate;

public final class PatientTemplateHelper {
    
    private PatientTemplateHelper() {
    }
    
    public static PatientTemplate createTestInstance() {
        PatientTemplate patientTemplate = new PatientTemplate();
        patientTemplate.setActor(PersonHelper.createTestInstance());
        patientTemplate.setActorType(RelationshipHelper.createTestInstance());
        patientTemplate.setPatient(PatientHelper.createTestInstance());
        
        return patientTemplate;
    }
}
