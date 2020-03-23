package org.openmrs.module.messages.domain.criteria;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.openmrs.Patient;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.model.ActorType;
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
                .createAlias("actorType", "a", JoinType.LEFT_OUTER_JOIN)
                .createAlias("a.relationshipType", "r", JoinType.LEFT_OUTER_JOIN)
                .add(
                        Restrictions.or(
                                Restrictions.isNull("actorType"), // directly related to a patient
                                Restrictions.in("r.uuid", getActorRelationshipTypeUuids())
                        )
                );
    }

    public static PatientTemplateCriteria forPatientId(Integer patientId) {
        Patient patient = new Patient(patientId);
        return new PatientTemplateCriteria(patient);
    }

    private List<String> getActorRelationshipTypeUuids() {
        ActorService actorService = getRegisteredComponent(
                MessagesConstants.ACTOR_SERVICE, ActorService.class);
        List<String> relationshipTypes = new ArrayList<>();
        for (ActorType actorType : actorService.getAllActorTypes()) {
            relationshipTypes.add(actorType.getRelationshipType().getUuid());
        }
        return relationshipTypes;
    }
}
