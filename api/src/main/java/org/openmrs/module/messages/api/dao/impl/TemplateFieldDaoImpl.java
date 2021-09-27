package org.openmrs.module.messages.api.dao.impl;

import org.openmrs.module.messages.api.dao.BaseOpenmrsDataDao;
import org.openmrs.module.messages.api.dao.TemplateFieldDao;
import org.openmrs.module.messages.api.model.TemplateField;

public class TemplateFieldDaoImpl extends BaseOpenmrsDataDao<TemplateField> implements TemplateFieldDao {

    public TemplateFieldDaoImpl() {
        super(TemplateField.class);
    }
}
