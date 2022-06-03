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

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Projections;
import org.openmrs.BaseOpenmrsData;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.api.db.hibernate.HibernateOpenmrsDataDAO;
import org.openmrs.module.messages.domain.PagingInfo;
import org.openmrs.module.messages.domain.criteria.BaseCriteria;

import java.util.List;

public abstract class BaseOpenmrsDataDao<T extends BaseOpenmrsData> extends HibernateOpenmrsDataDAO<T>
        implements BaseOpenmrsPageableDao<T> {

    private DbSessionFactory dbSessionFactory;

    protected BaseOpenmrsDataDao(Class<T> mappedClass) {
        super(mappedClass);
    }

    public void setDbSessionFactory(DbSessionFactory dbSessionFactory) {
        this.dbSessionFactory = dbSessionFactory;
    }

    @Override
    public List<T> findAllByCriteria(BaseCriteria criteria, PagingInfo paging) {
        Criteria hibernateCriteria = createCriteria();
        if (criteria != null) {
            criteria.initHibernateCriteria(hibernateCriteria);
            criteria.loadHibernateCriteria(hibernateCriteria);
        }
        loadPagingTotal(paging, hibernateCriteria);
        createPagingCriteria(paging, hibernateCriteria);
        return hibernateCriteria.list();
    }

    @Override
    public T findOneByCriteria(BaseCriteria criteria) {
        Criteria hibernateCriteria = createCriteria();
        if (criteria != null) {
            criteria.initHibernateCriteria(hibernateCriteria);
            criteria.loadHibernateCriteria(hibernateCriteria);
        }
        return (T) hibernateCriteria.uniqueResult();
    }

    protected Criteria createCriteria() {
        return getSession().createCriteria(this.mappedClass);
    }

    /**
     * Updates the specified {@link Criteria} object to retrieve the data specified by the {@link PagingInfo} object.
     * @param pagingInfo The {@link PagingInfo} object that specifies which data should be retrieved.
     * @param criteria The {@link Criteria} to add the paging settings to, or {@code null} to create a new one.
     * @return The {@link Criteria} object with the paging settings applied.
     */
    protected Criteria createPagingCriteria(PagingInfo pagingInfo, Criteria criteria) {
        if (pagingInfo != null && pagingInfo.getPage() > 0 && pagingInfo.getPageSize() > 0) {
            criteria.setFirstResult((pagingInfo.getPage() - 1) * pagingInfo.getPageSize());
            criteria.setMaxResults(pagingInfo.getPageSize());
            criteria.setFetchSize(pagingInfo.getPageSize());
        }

        return criteria;
    }

    /**
     * Count amount of rows for the specified criteria.
     * @param criteria The {@link Criteria} tto execute against the hibernate data source
     * @return The row count
     */
    protected Long countRows(Criteria criteria) {
        Long rows = (Long) criteria
                .setProjection(Projections.rowCount())
                .uniqueResult();
        // resetting criteria
        criteria.setProjection(null)
                .setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        return rows;
    }

    protected DbSession getSession() {
        return dbSessionFactory.getCurrentSession();
    }

    /**
     * Writes all pending changes, clear the session cache memory and saves object to the database.
     * These actions are performed due to performance issues when large amounts of data are loaded into the persistence
     * context.
     * Related ticket key: CFLM-1531
     *
     * @param object that will be saved to the database
     */
    protected Object saveOrUpdateWithClearingSessionCache(T object) {
        getSession().flush();
        getSession().clear();
        getSession().saveOrUpdate(object);
        return object;
    }

    protected void flushAndClearCache() {
        getSession().flush();
        getSession().clear();
    }

    /**
     * Loads the record count for the specified criteria into the specified paging object.
     * @param pagingInfo The {@link PagingInfo} object to load with the record count.
     * @param criteria The {@link Criteria} to execute against the hibernate data source or {@code null} to create a new one.
     */
    private void loadPagingTotal(PagingInfo pagingInfo, Criteria criteria) {
        if (pagingInfo != null && pagingInfo.getPage() > 0 && pagingInfo.getPageSize() > 0
                && pagingInfo.shouldLoadRecordCount()) {
            Long count = countRows(criteria);
            pagingInfo.setTotalRecordCount(count == null ? Long.valueOf(0) : count);
            pagingInfo.setLoadRecordCount(false);
        }
    }
}

