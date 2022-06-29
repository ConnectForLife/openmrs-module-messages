/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service.impl;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.messages.api.dao.BaseOpenmrsPageableDao;
import org.openmrs.module.messages.api.service.BaseOpenmrsCriteriaDataService;
import org.openmrs.module.messages.domain.PagingInfo;
import org.openmrs.module.messages.domain.criteria.BaseCriteria;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements generic methods for creating, reading, updating and deleting entities
 *
 * @param <T> an object which extends BaseOpenmrsData
 */
@Transactional
public class BaseOpenmrsDataService<T extends BaseOpenmrsData> extends BaseOpenmrsService
        implements BaseOpenmrsCriteriaDataService<T> {

    private BaseOpenmrsPageableDao<T> dao;

    private String daoBeanName;

    @Override
    @Transactional(readOnly = true)
    public T getById(Serializable id) throws APIException {
        return getDao().getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public T getByUuid(String uuid) throws APIException {
        return getDao().getByUuid(uuid);
    }

    @Override
    public void delete(T persistent) throws APIException {
        getDao().delete(persistent);
    }

    @Override
    public void delete(List<T> collection) throws APIException {
        for (T newOrPersisted : collection) {
            delete(newOrPersisted);
        }
    }

    @Override
    public T saveOrUpdate(T newOrPersisted) throws APIException {
        return getDao().saveOrUpdate(newOrPersisted);
    }

    @Override
    public List<T> saveOrUpdate(List<T> collection) throws APIException {
        List<T> result = new ArrayList<T>();
        for (T newOrPersisted : collection) {
            result.add(saveOrUpdate(newOrPersisted));
        }
        return result;
    }

    @Override
    public List<T> getAll(boolean includeVoided) throws APIException {
        return getDao().getAll(includeVoided);
    }

    @Override
    public List<T> getAll(boolean includeVoided, Integer firstResult, Integer maxResults) throws APIException {
        return getDao().getAll(includeVoided, firstResult, maxResults);
    }

    @Override
    public int getAllCount(boolean includeVoided) throws APIException {
        return getDao().getAllCount(includeVoided);
    }

    @Override
    public List<T> findAllByCriteria(BaseCriteria criteria) {
        return findAllByCriteria(criteria, null);
    }

    @Override
    public T findOneByCriteria(BaseCriteria criteria) {
        return getDao().findOneByCriteria(criteria);
    }

    public List<T> findAllByCriteria(BaseCriteria criteria, PagingInfo paging) {
        return getDao().findAllByCriteria(criteria, paging);
    }

    public BaseOpenmrsPageableDao<T> getDao() {
        if (this.dao == null) {
            this.dao = Context.getRegisteredComponent(daoBeanName, BaseOpenmrsPageableDao.class);
        }
        return this.dao;
    }

    public void setDaoBeanName(String daoBeanName) {
        this.daoBeanName = daoBeanName;
    }
}
