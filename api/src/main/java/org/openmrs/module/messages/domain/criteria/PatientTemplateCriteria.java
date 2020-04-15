package org.openmrs.module.messages.domain.criteria;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.RelationshipType;
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

    private Person actor;

    public PatientTemplateCriteria(Patient patient) {
        this.patient = patient;
        this.actor = null;
    }

    public PatientTemplateCriteria(Person actor) {
        this.actor = actor;
        this.patient = null;
    }

    public Integer getPersonId() {
        if (patient != null) {
            return patient.getPersonId();
        } else if (actor != null) {
            return actor.getPersonId();
        }
        return null;
    }

    @Override
    public void loadHibernateCriteria(Criteria hibernateCriteria) {
        if (patient != null) {
            // filtering PTs for a patient and the related actors - based on the allowed relationships (defined in GP)
            hibernateCriteria
                    .add(Restrictions.eq("patient", patient))
                    .createAlias("actorType", "a", JoinType.LEFT_OUTER_JOIN)
                    .add(
                            Restrictions.or(
                                    Restrictions.isNull("actorType"), // directly related to a patient
                                    Restrictions.in("a.relationshipType", getActorTypeRelationshipTypes(patient))
                            )
                    );
        } else if (actor != null) {
            hibernateCriteria
                    .add(Restrictions.eq("actor", actor));
                    //consider filtering by relationship type
        }
    }

    public static PatientTemplateCriteria forPatientId(Integer patientId) {
        Patient patient = new Patient(patientId);
        return new PatientTemplateCriteria(patient);
    }

    public static PatientTemplateCriteria forActorId(Integer personId) {
        Person actor = new Person(personId);
        return new PatientTemplateCriteria(actor);
    }

    private List<RelationshipType> getActorTypeRelationshipTypes(Patient patient) {
        ActorService actorService = getRegisteredComponent(
                MessagesConstants.ACTOR_SERVICE, ActorService.class);
        List<RelationshipType> relationships = new ArrayList<>();
        relationships.add(null);
        for (Actor patientActor : actorService.getAllActorsForPatient(patient)) {
            relationships.add(patientActor.getRelationship().getRelationshipType());
        }
        return relationships;
    }
}
