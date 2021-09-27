package org.openmrs.module.messages.api.service;

public interface TemplateFieldValueService {

    void updateTemplateFieldValue(Integer patientTemplateId, String fieldName, String newValue);
}
