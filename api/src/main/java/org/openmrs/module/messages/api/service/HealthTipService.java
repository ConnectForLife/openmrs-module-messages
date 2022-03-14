package org.openmrs.module.messages.api.service;

import org.openmrs.Concept;
import org.openmrs.module.messages.api.dto.HealthTipDTO;

import java.util.List;

public interface HealthTipService {

    /**
     * Gets the next health tip concept that should be played for a patient
     *
     * @param patientId patient id
     * @param actorId actor id
     * @param category health tip category, nullable
     * @return next health tip concept
     */
    Concept getNextHealthTipToPlay(Integer patientId, Integer actorId, String category);

    /**
     * Gets health tip text content that should be played for a patient
     *
     * @param patientId patient id
     * @param actorId actor id
     * @param category health tip category
     * @return health tip text
     */
    String getNextHealthTipText(Integer patientId, Integer actorId, String category);

    /**
     * Gets {@link HealthTipDTO} object related to found health tip concept
     *
     * @param patientId patient id
     * @param actorId actor id
     * @param category health tip category
     * @return HealthTipDTO
     */
    HealthTipDTO getNextHealthTipDTO(Integer patientId, Integer actorId, String category);

    /**
     * Gets list of {@link HealthTipDTO} objects related to all health tip concepts
     *
     * @param patientId patient id
     * @param actorId actor id
     * @param category health tip category
     * @return list of HealthTipDTO objects
     */
    List<HealthTipDTO> getHealthTipDTOs(Integer patientId, Integer actorId, String category);

    /**
     * Gets the health tip categories separated by a comma
     *
     * @param patientId patient id
     * @param actorId actor id
     * @return health tip categories
     */
    String getHealthTipCategories(Integer patientId, Integer actorId);

    /**
     * Gets list of all health tip ids already played related to patient's current health tip categories
     * or related to given category
     *
     * @param patientId patient id
     * @param actorId actor id
     * @param category health tip category
     * @return list of health tip ids
     */
    List<Integer> getHealthTipIdsAlreadyPlayed(Integer patientId, Integer actorId, String category);

    /**
     * Gets all health tip ids from patient's categories or given category
     *
     * @param patientId patient id
     * @param actorId actor id
     * @param category health tip category, nullable
     * @return list of health tip ids
     */
    List<Integer> getAllPossibleHealthTipIds(Integer patientId, Integer actorId, String category);

    /**
     * Gets the number of health tip ids from patient's categories or given category
     *
     * @param patientId patient id
     * @param actorId actor id
     * @param category health tip category
     * @return number of health tip ids
     */
    Integer getNumberOfPossibleHealthTipIds(Integer patientId, Integer actorId, String category);
}
