package org.openmrs.module.messages.api.service.impl;

import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.model.AdherenceFeedback;
import org.openmrs.module.messages.api.service.AdherenceFeedbackService;
import org.openmrs.module.messages.handler.AdherenceFeedbackCalculationHandler;

import java.util.Map;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toMap;

public class AdherenceFeedbackServiceImpl implements AdherenceFeedbackService {

  @Override
  public Map<String, AdherenceFeedback> getAdherenceFeedback(Integer actorId, Integer patientId) {
    requireNonNull(actorId, "The actorId must not be null!");
    requireNonNull(patientId, "The patientId must not be null!");

    final Person actor = getActor(actorId);
    final Patient patient = getPatient(patientId);

    return Context.getRegisteredComponents(AdherenceFeedbackCalculationHandler.class).stream()
        .collect(
            toMap(
                AdherenceFeedbackCalculationHandler::getServiceName,
                handler -> handler.getAdherenceFeedback(actor, patient)));
  }

  private Person getActor(Integer actorId) {
    final Person actor = Context.getPersonService().getPerson(actorId);

    if (actor == null) {
      throw new IllegalArgumentException("Could not find Person (Actor) for ID: " + actorId);
    }

    return actor;
  }

  private Patient getPatient(Integer patientId) {
    final Patient patient = Context.getPatientService().getPatient(patientId);

    if (patient == null) {
      throw new IllegalArgumentException("Could not find Patient for ID: " + patientId);
    }

    return patient;
  }
}
