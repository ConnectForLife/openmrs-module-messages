package org.openmrs.module.messages.handler;

import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.module.messages.api.model.AdherenceFeedback;

public interface AdherenceFeedbackCalculationHandler {
  String getServiceName();

  AdherenceFeedback getAdherenceFeedback(Person actor, Patient patient);
}
