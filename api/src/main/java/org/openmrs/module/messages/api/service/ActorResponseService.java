package org.openmrs.module.messages.api.service;

import org.openmrs.annotation.Authorized;
import org.openmrs.module.messages.api.model.ActorResponse;
import org.openmrs.module.messages.api.util.PrivilegeConstants;
import org.openmrs.module.messages.domain.criteria.BaseCriteria;

import java.util.List;

/**
 * Provides methods for creating, reading, updating and deleting actor response entities
 */
public interface ActorResponseService extends BaseOpenmrsCriteriaDataService<ActorResponse> {

    /**
     * Allows to find desired entities filtered by the searching criteria
     *
     * @param criteria object representing the searching criteria
     * @return a list of found actor responses
     */
    @Authorized(value = { PrivilegeConstants.GET_PATIENTS_PRIVILEGE})
    List<ActorResponse> findAllByCriteria(BaseCriteria criteria);
}
