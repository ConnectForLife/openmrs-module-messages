package org.openmrs.module.messages.api.dao.impl;

import org.openmrs.api.db.hibernate.HibernateOpenmrsDataDAO;
import org.openmrs.module.messages.api.dao.TemplateDao;
import org.openmrs.module.messages.api.model.Template;

public class TemplateDaoImpl extends HibernateOpenmrsDataDAO<Template> implements TemplateDao {
    
    public TemplateDaoImpl() {
        super(Template.class);
    }
}
