package org.openmrs.module.messages.domain;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Patient;

import java.io.Serializable;

public class PatientTemplateCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private Patient patient;

    public PatientTemplateCriteria(Patient patient) {
        this.patient = patient;
    }

    public Integer getPatientId() {
        return patient.getPatientId();
    }

    public void loadHibernateCriteria(Criteria hibernateCriteria) {
        hibernateCriteria.add(Restrictions.eq("patient", patient));
    }
}
