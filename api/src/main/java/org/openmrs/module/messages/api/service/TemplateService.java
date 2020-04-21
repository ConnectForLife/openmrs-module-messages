package org.openmrs.module.messages.api.service;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.module.messages.api.dto.TemplateDTO;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.util.PrivilegeConstants;

import java.util.List;

/**
 * Provides methods for creating, reading, updating and deleting template entities
 */
public interface TemplateService extends BaseOpenmrsCriteriaDataService<Template> {

    /**
     * Creates new or updates existing template
     *
     * @param newOrPersisted template to save or update
     * @return saved or updated template
     * @throws APIException
     */
    @Authorized(value = { PrivilegeConstants.MANAGE_PRIVILEGE})
    Template saveOrUpdateTemplate(Template newOrPersisted) throws APIException;

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
}
