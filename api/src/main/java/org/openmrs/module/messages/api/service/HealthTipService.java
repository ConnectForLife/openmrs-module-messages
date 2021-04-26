package org.openmrs.module.messages.api.service;

import org.openmrs.Concept;

public interface HealthTipService {

    Concept getNextHealthTipToPlay(Integer patientId, Integer actorId, String category);
}
