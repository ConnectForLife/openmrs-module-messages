package org.openmrs.module.messages.api.dao.impl;

import org.openmrs.module.messages.api.dao.BaseOpenmrsDataDao;
import org.openmrs.module.messages.api.dao.PatientTemplateDao;
import org.openmrs.module.messages.api.model.PatientTemplate;

public class PatientTemplateDaoImpl extends BaseOpenmrsDataDao<PatientTemplate>
        implements PatientTemplateDao {
    
    public PatientTemplateDaoImpl() {
        super(PatientTemplate.class);
    }
}
