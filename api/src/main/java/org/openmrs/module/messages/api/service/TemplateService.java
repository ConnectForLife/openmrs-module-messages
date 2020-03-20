package org.openmrs.module.messages.api.service;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.module.messages.api.dto.TemplateDTO;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.util.PrivilegeConstants;

import java.util.List;

public interface TemplateService extends BaseOpenmrsCriteriaDataService<Template> {

    @Authorized(value = { PrivilegeConstants.MANAGE_PRIVILEGE})
    Template saveOrUpdateTemplate(Template newOrPersisted) throws APIException;

    Template saveOrUpdateByDto(TemplateDTO templateDto);

    List<Template> saveOrUpdateByDtos(List<TemplateDTO> templateDtos);
}
