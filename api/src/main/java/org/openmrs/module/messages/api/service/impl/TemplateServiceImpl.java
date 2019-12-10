package org.openmrs.module.messages.api.service.impl;

import org.openmrs.api.APIException;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.service.TemplateService;

public class TemplateServiceImpl extends BaseOpenmrsDataService<Template> implements TemplateService {

    @Override
    public Template saveOrUpdateTemplate(Template newOrPersisted) throws APIException {
        return super.saveOrUpdate(newOrPersisted);
    }
}
