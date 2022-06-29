/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.dao;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.api.db.OpenmrsDataDAO;
import org.openmrs.module.messages.domain.PagingInfo;
import org.openmrs.module.messages.domain.criteria.BaseCriteria;

import java.util.List;

/**
 * Data access object for the entities extending BaseOpenmrsData
 */
public interface BaseOpenmrsPageableDao<T extends BaseOpenmrsData> extends OpenmrsDataDAO<T> {

    /**
     * Finds paginated collection of the objects of the specified type
     *
     * @param criteria object representing searching criteria
     * @param pagingInfo object representing pagination parameters (eg. page size)
     * @return list of the objects of the specified type, implicitly paginated
     */
    List<T> findAllByCriteria(BaseCriteria criteria, PagingInfo pagingInfo);

    T findOneByCriteria(BaseCriteria criteria);
}
