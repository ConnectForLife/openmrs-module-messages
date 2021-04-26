package org.openmrs.module.messages.api.dao.impl;

import org.openmrs.module.messages.api.dao.BaseOpenmrsDataDao;
import org.openmrs.module.messages.api.dao.TemplateFieldValueDao;
import org.openmrs.module.messages.api.model.TemplateFieldValue;

public class TemplateFieldValueDaoImpl extends BaseOpenmrsDataDao<TemplateFieldValue>
        implements TemplateFieldValueDao {

    public TemplateFieldValueDaoImpl() {
        super(TemplateFieldValue.class);
    }
}
