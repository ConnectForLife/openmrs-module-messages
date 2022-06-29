/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

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
