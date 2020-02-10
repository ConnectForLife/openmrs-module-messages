package org.openmrs.module.messages.api.service;

import org.openmrs.annotation.Authorized;
import org.openmrs.module.messages.api.model.ActorResponse;
import org.openmrs.module.messages.api.util.PrivilegeConstants;
import org.openmrs.module.messages.domain.criteria.BaseCriteria;

import java.util.List;

public interface ActorResponseService extends BaseOpenmrsCriteriaDataService<ActorResponse> {

    @Authorized(value = { PrivilegeConstants.VIEW_PATIENTS_PRIVILEGE })
    List<ActorResponse> findAllByCriteria(BaseCriteria criteria);
}
