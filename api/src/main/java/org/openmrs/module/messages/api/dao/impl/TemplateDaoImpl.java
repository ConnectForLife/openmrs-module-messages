package org.openmrs.module.messages.api.dao.impl;

import org.openmrs.module.messages.api.dao.BaseOpenmrsDataDao;
import org.openmrs.module.messages.api.dao.TemplateDao;
import org.openmrs.module.messages.api.model.Template;

public class TemplateDaoImpl extends BaseOpenmrsDataDao<Template> implements TemplateDao {
    
    public TemplateDaoImpl() {
        super(Template.class);
    }
}
