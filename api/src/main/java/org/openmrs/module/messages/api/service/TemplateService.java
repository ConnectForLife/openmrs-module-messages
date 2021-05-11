package org.openmrs.module.messages.api.service;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.messages.api.dto.TemplateDTO;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.util.PrivilegeConstants;
import org.openmrs.module.messages.domain.PagingInfo;
import org.openmrs.module.messages.domain.criteria.BaseCriteria;

import java.io.Serializable;
import java.util.List;

/**
 * Provides methods for creating, reading, updating and deleting template entities
 */
public interface TemplateService extends OpenmrsService {

    /**
     * Obtains an object matching a given identifier
     * It can be called by users with this module's privilege. It is executed in a transaction.
     *
     * @param id the data identifier
     * @return the matching data object
     * @throws APIException - service exception
     */
    Template getById(Serializable id) throws APIException;

    /**
     * Return a list of persistents (optionally voided)
     * It can be called by users with this module's privilege. It is executed in a transaction.
     *
     * @param includeRetired - if true retired persistents should also be returned
     * @return a list of persistents of the given class
     */
    List<Template> getAll(boolean includeRetired) throws APIException;

    /**
     * Creates new or updates existing template
     *
     * @param newOrPersisted template to save or update
     * @return saved or updated template
     * @throws APIException
     */
    @Authorized(value = {PrivilegeConstants.MANAGE_PRIVILEGE})
    Template saveOrUpdate(Template newOrPersisted) throws APIException;

    /**
     * Creates new or updates existing template
     *
     * @param templateDto DTO object containing necessary data to save
     * @return saved or updated template
     */
    Template saveOrUpdateByDto(TemplateDTO templateDto);

    /**
     * Creates new or updates existing templates
     *
     * @param templateDtos list of DTO objects containing necessary data to save
     * @return saved or updated list of templates
     */
    List<Template> saveOrUpdateByDtos(List<TemplateDTO> templateDtos);

    /**
     * Method allows to find the desired entities filtered by the searching criteria
     *
     * @param criteria object representing the searching criteria
     * @return a list of found objects
     */
    List<Template> findAllByCriteria(BaseCriteria criteria);

    /**
     * Method allows to find the desired entities filtered by the searching criteria and paginated
     *
     * @param criteria criteria object representing the searching criteria
     * @param paging   paging object containing the paging information (eg. page size)
     * @return list of found object, implicitly paginated
     */
    List<Template> findAllByCriteria(BaseCriteria criteria, PagingInfo paging);

    /**
     * Finds one instance of Template based on the {@code criteria}.
     *
     * @param criteria the criteria used to search for one instance of Template, never null
     * @return the Template or null if there was not Template fit the criteria
     */
    Template findOneByCriteria(BaseCriteria criteria);
}
