package org.openmrs.module.messages.api.dao.impl;

import org.openmrs.module.messages.api.dao.BaseOpenmrsDataDao;
import org.openmrs.module.messages.api.dao.TemplateFieldDefaultValueDao;
import org.openmrs.module.messages.api.model.TemplateFieldDefaultValue;

public class TemplateFieldDefaultValueDaoImpl extends BaseOpenmrsDataDao<TemplateFieldDefaultValue>
        implements TemplateFieldDefaultValueDao {

    public TemplateFieldDefaultValueDaoImpl() {
        super(TemplateFieldDefaultValue.class);
    }
}
