package org.openmrs.module.messages.api.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.openmrs.BaseOpenmrsData;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.api.db.hibernate.HibernateOpenmrsDataDAO;
import org.openmrs.module.messages.domain.PagingInfo;

public abstract class BaseOpenmrsDataDao<T extends BaseOpenmrsData> extends HibernateOpenmrsDataDAO<T> {

    private DbSessionFactory sessionFactory;

    public BaseOpenmrsDataDao(Class<T> mappedClass) {
        super(mappedClass);
    }

    public void setDbSessionFactory(DbSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Criteria createCriteria() {
        return getSession().createCriteria(this.mappedClass);
    }

    /**
     * Loads the record count for the specified criteria into the specified paging object.
     * @param pagingInfo The {@link PagingInfo} object to load with the record count.
     * @param criteria The {@link Criteria} to execute against the hibernate data source or {@code null} to create a new one.
     */
    protected void loadPagingTotal(PagingInfo pagingInfo, Criteria criteria) {
        if (pagingInfo != null && pagingInfo.getPage() > 0 && pagingInfo.getPageSize() > 0
                && pagingInfo.shouldLoadRecordCount()) {
            Long count = countRows(criteria);
            pagingInfo.setTotalRecordCount(count == null ? 0 : count);
            pagingInfo.setLoadRecordCount(false);
        }
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
    private Long countRows(Criteria criteria) {
        Long rows = (Long) criteria
                .setProjection(Projections.rowCount())
                .uniqueResult();
        // resetting criteria
        criteria.setProjection(null)
                .setResultTransformer(Criteria.ROOT_ENTITY);
        return rows;
    }

    private DbSession getSession() {
        return sessionFactory.getCurrentSession();
    }
}

