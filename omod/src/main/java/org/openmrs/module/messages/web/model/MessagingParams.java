package org.openmrs.module.messages.web.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.openmrs.Patient;
import org.openmrs.module.messages.domain.criteria.PatientTemplateCriteria;

/**
 * Models the messaging controller parameters
 */
public class MessagingParams extends PageableParams {

    /**
     * The id of patient to search for
     */
    private Integer patientId;

    public MessagingParams setPatientId(Integer patientId) {
        this.patientId = patientId;
        return this;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public PatientTemplateCriteria getCriteria() {
        return new PatientTemplateCriteria(wrap(patientId));
    }

    private Patient wrap(Integer patientId) {
        Patient patient = new Patient();
        patient.setId(patientId);
        return patient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
