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
}
