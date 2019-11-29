package org.openmrs.module.messages.api.actor;

import org.openmrs.Patient;
import org.openmrs.module.messages.api.model.Actor;
import org.openmrs.module.messages.api.model.ActorType;

import java.util.List;

public interface ActorService {

    List<Actor> getAllActorsForPatient(Patient patient);

    List<ActorType> getAllActorTypes();
}
