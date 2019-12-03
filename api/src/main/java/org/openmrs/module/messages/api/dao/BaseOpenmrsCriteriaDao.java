package org.openmrs.module.messages.api.dao;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.api.db.OpenmrsDataDAO;
import org.openmrs.module.messages.domain.PagingInfo;
import org.openmrs.module.messages.domain.criteria.BaseCriteria;

import java.util.List;

public interface BaseOpenmrsCriteriaDao<T extends BaseOpenmrsData> extends OpenmrsDataDAO<T> {

    List<T> findAllByCriteria(BaseCriteria criteria, PagingInfo pagingInfo);
}
