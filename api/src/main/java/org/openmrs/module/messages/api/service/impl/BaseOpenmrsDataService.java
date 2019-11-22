package org.openmrs.module.messages.api.service.impl;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.api.APIException;
import org.openmrs.api.db.OpenmrsDataDAO;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.messages.api.service.OpenmrsDataService;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Transactional
public class BaseOpenmrsDataService<T extends BaseOpenmrsData> extends BaseOpenmrsService implements OpenmrsDataService<T> {
    
    private OpenmrsDataDAO<T> dao;
    
    public BaseOpenmrsDataService() {
    }
    
    public BaseOpenmrsDataService(OpenmrsDataDAO<T> dao) {
        this.dao = dao;
    }
    
    public void setDao(OpenmrsDataDAO<T> dao) {
        this.dao = dao;
    }
    
    @Override
    @Transactional(readOnly = true)
    public T getById(Serializable id) throws APIException {
        return dao.getById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public T getByUuid(String uuid) throws APIException {
        return dao.getByUuid(uuid);
    }
    
    @Override
    public void delete(T persistent) throws APIException {
        dao.delete(persistent);
    }
    
    @Override
    public T saveOrUpdate(T newOrPersisted) throws APIException {
        return dao.saveOrUpdate(newOrPersisted);
    }
    
    @Override
    public List<T> getAll(boolean includeVoided) throws APIException {
        return dao.getAll(includeVoided);
    }
    
    @Override
    public List<T> getAll(boolean includeVoided, Integer firstResult, Integer maxResults) throws APIException {
        return dao.getAll(includeVoided, firstResult, maxResults);
    }
    
    @Override
    public int getAllCount(boolean includeVoided) throws APIException {
        return dao.getAllCount(includeVoided);
    }
}
