package org.openmrs.module.messages.domain.criteria;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Patient;
import org.openmrs.Relationship;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.model.Actor;
import org.openmrs.module.messages.api.service.ActorService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.openmrs.api.context.Context.getRegisteredComponent;

public class PatientTemplateCriteria extends BaseCriteria implements Serializable {

    private static final long serialVersionUID = -486120008842837370L;

    private Patient patient;

    public PatientTemplateCriteria(Patient patient) {
        this.patient = patient;
    }

    public Integer getPatientId() {
        return patient.getPatientId();
    }

    @Override
    public void loadHibernateCriteria(Criteria hibernateCriteria) {
        // filtering PTs for a patient and the related actors - based on the allowed relationships (defined in GP)
        hibernateCriteria
                .add(Restrictions.eq("patient", patient))
                .add(
                        Restrictions.or(
                                Restrictions.isNull("actorType"), // directly related to a patient
                                Restrictions.in("actorType", getActorTypeRelationships(patient))
                        )
                );
    }

    public static PatientTemplateCriteria forPatientId(Integer patientId) {
        Patient patient = new Patient(patientId);
        return new PatientTemplateCriteria(patient);
    }

    private List<Relationship> getActorTypeRelationships(Patient patient) {
        ActorService actorService = getRegisteredComponent(
                MessagesConstants.ACTOR_SERVICE, ActorService.class);
        List<Relationship> relationships = new ArrayList<>();
        relationships.add(null);
        for (Actor actor : actorService.getAllActorsForPatient(patient)) {
            relationships.add(actor.getRelationship());
        }
        return relationships;
    }
}
