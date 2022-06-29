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

import org.openmrs.BaseOpenmrsData;
import org.openmrs.module.messages.domain.PagingInfo;
import org.openmrs.module.messages.domain.criteria.BaseCriteria;

import java.util.List;

/**
 * Provides generic methods for creating, reading, updating and deleting entities
 *
 * @param <T> an object which extends BaseOpenmrsData
 */
public interface BaseOpenmrsCriteriaDataService<T extends BaseOpenmrsData> extends OpenmrsDataService<T> {

    /**
     * Method allows to find the desired entities filtered by the searching criteria
     *
     * @param criteria object representing the searching criteria
     * @return a list of found objects
     */
    List<T> findAllByCriteria(BaseCriteria criteria);

    /**
     * Method allows to find the desired entities filtered by the searching criteria and paginated
     *
     * @param criteria criteria object representing the searching criteria
     * @param paging paging object containing the paging information (eg. page size)
     * @return  list of found object, implicitly paginated
     */
    List<T> findAllByCriteria(BaseCriteria criteria, PagingInfo paging);

    T findOneByCriteria(BaseCriteria criteria);
}
