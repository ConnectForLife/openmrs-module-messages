package org.openmrs.module.messages.api.service;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.util.PrivilegeConstants;

public interface TemplateService extends BaseOpenmrsCriteriaDataService<Template> {

    @Authorized(value = { PrivilegeConstants.MANAGE_PRIVILEGE})
    Template saveOrUpdateTemplate(Template newOrPersisted) throws APIException;
}
