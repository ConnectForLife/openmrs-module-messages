package org.openmrs.module.messages.api.service.impl;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.api.PatientService;
import org.openmrs.module.messages.api.event.MessagesEvent;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;
import org.openmrs.module.messages.api.service.MessagesEventService;
import org.openmrs.module.messages.api.service.ServiceResultHandlerService;

public abstract class AbstractServiceResultHandlerService implements ServiceResultHandlerService {
    private static final String PATIENT_PHONE_ATTR = "Telephone Number";

    private PatientService patientService;

    private MessagesEventService messagesEventService;

    public void setMessagesEventService(MessagesEventService messagesEventService) {
        this.messagesEventService = messagesEventService;
    }

    public void setPatientService(PatientService patientService) {
        this.patientService = patientService;
    }

    protected void sendEventMessage(MessagesEvent messagesEvent) {
        messagesEventService.sendEventMessage(messagesEvent);
    }

    protected String getPatientPhone(Integer patientId) {
        Patient patient = patientService.getPatient(patientId);
        if (patient == null) {
            throw new MessagesRuntimeException(String.format("Patient with id %s does not exist", patientId));
        }
        PersonAttribute attribute = patient.getAttribute(PATIENT_PHONE_ATTR);
        if (attribute == null || StringUtils.isBlank(attribute.getValue())) {
            throw new MessagesRuntimeException(String.format("Phone number not specified for " +
                "patient %s", patient.getId()));
        }
        return attribute.getValue();
    }
}
