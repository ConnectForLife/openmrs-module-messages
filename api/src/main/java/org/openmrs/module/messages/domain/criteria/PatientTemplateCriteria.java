package org.openmrs.module.messages.domain.criteria;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.Relationship;
import org.openmrs.RelationshipType;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.model.Actor;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.service.ActorService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.openmrs.api.context.Context.getRegisteredComponent;

public class PatientTemplateCriteria extends BaseOpenmrsDataCriteria implements Serializable {

    private static final long serialVersionUID = -486120008842837370L;

    private Patient patient;

    private Person actor;

    private Relationship actorType;

    private Template template;

    public PatientTemplateCriteria(Patient patient) {
        this.patient = patient;
    }

    public PatientTemplateCriteria(Person actor) {
        this.actor = actor;
    }

    public PatientTemplateCriteria(Relationship actorType) {
        this.actorType = actorType;
    }

    public PatientTemplateCriteria(Template template) {
        this.template = template;
    }

    public PatientTemplateCriteria(Patient patient, Person actor, Template template) {
        this.patient = patient;
        this.actor = actor;
        this.template = template;
    }

    /**
     * Factory method used to create criteria for specific patient
     *
     * @param patientId id of related patient
     */
    public static PatientTemplateCriteria forPatientId(Integer patientId) {
        Patient patient = new Patient(patientId);
        return new PatientTemplateCriteria(patient);
    }

    /**
     * Factory method used to create criteria for specific actor
     *
     * @param personId id of related actor
     */
    public static PatientTemplateCriteria forActorId(Integer personId) {
        Person actor = new Person(personId);
        return new PatientTemplateCriteria(actor);
    }

    /**
     * Factory method used to create criteria for specific actor type (relationship)
     *
     * @param relationshipId id of related relationship
     */
    public static PatientTemplateCriteria forActorType(Integer relationshipId) {
        Relationship actorType = new Relationship(relationshipId);
        return new PatientTemplateCriteria(actorType);
    }

    /**
     * Factory method used to create criteria for specific template
     *
     * @param templateId id of related template
     */
    public static PatientTemplateCriteria forTemplate(Integer templateId) {
        Template template = new Template(templateId);
        return new PatientTemplateCriteria(template);
    }

    /**
     * Factory method used to create criteria for specific patient, actor nad template
     *
     * @param patientId id of related patient
     * @param actorId id of related actor
     * @param templateId id of related template
     * @return
     */
    public static PatientTemplateCriteria forPatientAndActorAndTemplate(Integer patientId, Integer actorId,
                                                                        Integer templateId) {
        Patient patient = new Patient(patientId);
        Person actor = new Person(actorId);
        Template template = new Template(templateId);
        return new PatientTemplateCriteria(patient, actor, template);
    }

    /**
     * Return person id. It can be patient or actor id depending on criteria configuration.
     *
     * @return person id
     */
    public Integer getPersonId() {
        if (patient != null) {
            return patient.getPersonId();
        } else if (actor != null) {
            return actor.getPersonId();
        }
        return null;
    }

    /**
     * @see BaseCriteria#loadHibernateCriteria(Criteria)
     */
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
        } else if (actorType != null) {
            hibernateCriteria
                    .add(Restrictions.eq("actorType", actorType));
        } else if (template != null) {
            hibernateCriteria
                    .add(Restrictions.eq("template", template));
        }
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
