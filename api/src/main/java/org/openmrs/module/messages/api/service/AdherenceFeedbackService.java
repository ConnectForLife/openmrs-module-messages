package org.openmrs.module.messages.api.service;

import org.openmrs.module.messages.api.model.AdherenceFeedback;

import java.util.Map;

/**
 * The AdherenceFeedback service API.
 *
 * @implNote The interface and implementation must be designed for usage in Velocity Template *
 *     scripts.
 */
public interface AdherenceFeedbackService {
  Map<String, AdherenceFeedback> getAdherenceFeedback(Integer patientId, Integer actorId);
}
