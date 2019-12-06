package org.openmrs.module.messages.api.service;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;

import java.io.Serializable;
import java.util.List;

public interface OpenmrsDataService<T extends BaseOpenmrsData> extends OpenmrsService {
    
    /**
     * Obtains an object matching a given identifier
     * It can be called by users with this module's privilege. It is executed in a transaction.
     *
     * @param id the data identifier
     * @return the matching data object
     * @throws APIException - service exception
     */
    T getById(Serializable id) throws APIException;
    
    /**
     * Obtains an object matching a given UUID
     * It can be called by users with this module's privilege. It is executed in a transaction.
     *
     * @param uuid - the data uuid
     * @return the matching data object
     * @throws APIException - service exception
     */
    T getByUuid(String uuid) throws APIException;
    
    /**
     * Completely deletes a persistent from the database
     * It can be called by users with this module's privilege. It is executed in a transaction.
     *
     * @param persistent - the persistent to delete
     * @throws APIException - service exception
     */
    void delete(T persistent) throws APIException;
    
    /**
     * Save or update a persistent in the database
     * It can be called by users with this module's privilege. It is executed in a transaction.
     *
     * @param newOrPersisted - the persistent to save or update
     * @return the persistent that was saved or updated
     * @throws APIException - service exception
     */
    T saveOrUpdate(T newOrPersisted) throws APIException;

    /**
     * Save or update a collection of data in the database
     * It can be called by users with this module's privilege. It is executed in a transaction.
     *
     * @param collection - collection of data to save or update
     * @return collection of data that was saved or updated
     * @throws APIException - service exception
     */
    List<T> saveOrUpdate(List<T> collection) throws APIException;

    /**
     * Return a list of persistents (optionally voided)
     * It can be called by users with this module's privilege. It is executed in a transaction.
     *
     * @param includeVoided - if true voided persistents are also returned
     * @return a list of persistents of the given class
     */
    List<T> getAll(boolean includeVoided) throws APIException;
    
    /**
     * Return a lists of persistents optionally voided, with paging
     * It can be called by users with this module's privilege. It is executed in a transaction.
     *
     * @param includeVoided - if true voided persistents are also returned
     * @param firstResult   - number of first result
     * @param maxResults    - number of max result
     * @return list of persistents
     */
    List<T> getAll(boolean includeVoided, Integer firstResult, Integer maxResults) throws APIException;
    
    /**
     * Returns total number of persistents (optionally voided)
     * It can be called by users with this module's privilege. It is executed in a transaction.
     *
     * @param includeVoided - if true voided persistents are also returned
     * @return total number of persistents
     */
    int getAllCount(boolean includeVoided) throws APIException;
}
