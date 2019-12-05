package org.openmrs.module.messages.api.service;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.module.messages.domain.PagingInfo;
import org.openmrs.module.messages.domain.criteria.BaseCriteria;

import java.util.List;

public interface BaseOpenmrsCriteriaDataService<T extends BaseOpenmrsData> extends OpenmrsDataService<T> {

    List<T> findAllByCriteria(BaseCriteria criteria);

    List<T> findAllByCriteria(BaseCriteria criteria, PagingInfo paging);
}
