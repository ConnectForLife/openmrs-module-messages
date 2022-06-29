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

import org.openmrs.api.db.OpenmrsMetadataDAO;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.domain.PagingInfo;
import org.openmrs.module.messages.domain.criteria.BaseCriteria;

import java.util.List;

/**
 * Data access object for the Template entities
 */
public interface TemplateDao extends OpenmrsMetadataDAO<Template> {

    /**
     * Finds paginated collection of the objects of the specified type
     *
     * @param criteria   object representing searching criteria
     * @param pagingInfo object representing pagination parameters (eg. page size)
     * @return list of the objects of the specified type, implicitly paginated
     */
    List<Template> findAllByCriteria(BaseCriteria criteria, PagingInfo pagingInfo);

    /**
     * Finds single instance of Template which fits the {@code criteria}.
     *
     * @param criteria the criteria used to find the Template, not null
     * @return the Template fitting {@code criteria} or null if not found
     * @throws org.hibernate.HibernateException if there are more then one Template which fits the {@code criteria}
     */
    Template findOneByCriteria(BaseCriteria criteria);

    /**
     * Gets count of all Template which fit {@code criteria}.
     *
     * @param criteria object representing searching criteria
     * @return the count of Template entities which fits the {@code criteria}
     */
    long getCountByCriteria(BaseCriteria criteria);
}
