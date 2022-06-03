/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.openmrs.api.db.hibernate.HibernateOpenmrsMetadataDAO;
import org.openmrs.module.messages.api.dao.TemplateDao;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.domain.PagingInfo;
import org.openmrs.module.messages.domain.criteria.BaseCriteria;

import java.util.List;

/**
 * The TemplateDaoImpl is a default implementation of TemplateDao.
 * <p>
 * See: moduleApplicationContext.xml
 * </p>
 */
public class TemplateDaoImpl extends HibernateOpenmrsMetadataDAO<Template> implements TemplateDao {

    public TemplateDaoImpl() {
        super(Template.class);
    }

    @Override
    public List<Template> findAllByCriteria(BaseCriteria criteria, PagingInfo pagingInfo) {
        final Criteria hibernateCriteria = sessionFactory.getCurrentSession().createCriteria(Template.class);

        if (criteria != null) {
            criteria.initHibernateCriteria(hibernateCriteria);
            criteria.loadHibernateCriteria(hibernateCriteria);
        }

        if (pagingInfo != null && pagingInfo.getPage() > 0 && pagingInfo.getPageSize() > 0) {
            hibernateCriteria
                    .setFirstResult((pagingInfo.getPage() - 1) * pagingInfo.getPageSize())
                    .setMaxResults(pagingInfo.getPageSize())
                    .setFetchSize(pagingInfo.getPageSize());

            if (pagingInfo.shouldLoadRecordCount()) {
                final long count = getCountByCriteria(criteria);
                pagingInfo.setTotalRecordCount(count);
                pagingInfo.setLoadRecordCount(false);
            }
        }

        return hibernateCriteria.list();
    }

    @Override
    public Template findOneByCriteria(BaseCriteria criteria) {
        final Criteria hibernateCriteria = sessionFactory.getCurrentSession().createCriteria(Template.class);

        if (criteria != null) {
            criteria.initHibernateCriteria(hibernateCriteria);
            criteria.loadHibernateCriteria(hibernateCriteria);
        }

        return (Template) hibernateCriteria.uniqueResult();
    }

    @Override
    public long getCountByCriteria(BaseCriteria criteria) {
        final Criteria hibernateCriteria = sessionFactory.getCurrentSession().createCriteria(Template.class);

        if (criteria != null) {
            criteria.initHibernateCriteria(hibernateCriteria);
            criteria.loadHibernateCriteria(hibernateCriteria);
        }

        return ((Number) hibernateCriteria.setProjection(Projections.rowCount()).uniqueResult()).longValue();
    }
}
