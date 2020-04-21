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
}
