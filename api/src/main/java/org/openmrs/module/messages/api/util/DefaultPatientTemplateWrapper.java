package org.openmrs.module.messages.api.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.openmrs.module.messages.api.model.PatientTemplate;

public class DefaultPatientTemplateWrapper {

    public static List<DefaultPatientTemplateWrapper> wrapToList(Collection<PatientTemplate> patientTemplates) {
        List<DefaultPatientTemplateWrapper> list = new ArrayList<>();
        for (PatientTemplate patientTemplate : patientTemplates) {
            list.add(new DefaultPatientTemplateWrapper(patientTemplate));
        }
        return list;
    }

    public static List<PatientTemplate> unwrapToList(Collection<DefaultPatientTemplateWrapper> wrappedList) {
        List<PatientTemplate> list = new ArrayList<>();
        for (DefaultPatientTemplateWrapper wrapped : wrappedList) {
            list.add(wrapped.getPatientTemplate());
        }
        return list;
    }

    public DefaultPatientTemplateWrapper(PatientTemplate patientTemplate) {
        if (patientTemplate == null) {
            throw new IllegalArgumentException("Patient template cannot be null");
        }
        this.patientTemplate = patientTemplate;
    }

    public PatientTemplate getPatientTemplate() {
        return patientTemplate;
    }

    private PatientTemplate patientTemplate;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PatientTemplate other = ((DefaultPatientTemplateWrapper) o).getPatientTemplate();

        return isSameTemplate(other) && isSamePatient(other) && isSameActor(other);
    }

    @Override
    @SuppressWarnings("checkstyle:magicnumber")
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(patientTemplate.getTemplate())
            .append(patientTemplate.getPatient())
            .append(patientTemplate.getActor())
            .toHashCode();
    }

    private boolean isSameActor(PatientTemplate other) {
        return this.patientTemplate
            .getActor()
            .equals(other.getActor());
    }

    private boolean isSamePatient(PatientTemplate other) {
        return this.patientTemplate
            .getPatient()
            .getId()
            .equals(other.getPatient().getId());
    }

    private boolean isSameTemplate(PatientTemplate other) {
        return this.patientTemplate
            .getTemplate()
            .getId()
            .equals(other.getTemplate().getId());
    }
}
