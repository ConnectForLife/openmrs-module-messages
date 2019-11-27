package org.openmrs.module.messages.api.dao.impl;

import org.openmrs.api.db.hibernate.HibernateOpenmrsDataDAO;
import org.openmrs.module.messages.api.dao.PatientTemplateDao;
import org.openmrs.module.messages.api.model.PatientTemplate;

public class PatientTemplateDaoImpl extends HibernateOpenmrsDataDAO<PatientTemplate> implements PatientTemplateDao {
    
    public PatientTemplateDaoImpl() {
        super(PatientTemplate.class);
    }
}
