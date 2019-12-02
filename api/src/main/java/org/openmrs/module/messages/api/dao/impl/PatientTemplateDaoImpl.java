package org.openmrs.module.messages.api.dao.impl;

import org.hibernate.Criteria;
import org.openmrs.module.messages.api.dao.BaseOpenmrsDataDao;
import org.openmrs.module.messages.api.dao.PatientTemplateDao;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.domain.PagingInfo;
import org.openmrs.module.messages.domain.PatientTemplateCriteria;

import java.util.List;

public class PatientTemplateDaoImpl extends BaseOpenmrsDataDao<PatientTemplate>
        implements PatientTemplateDao {
    
    public PatientTemplateDaoImpl() {
        super(PatientTemplate.class);
    }

    @Override
    public List<PatientTemplate> findAllByCriteria(PatientTemplateCriteria criteria,
                                                   PagingInfo paging) {
        Criteria hibernateCriteria = createCriteria();
        criteria.loadHibernateCriteria(hibernateCriteria);
        loadPagingTotal(paging, hibernateCriteria);
        createPagingCriteria(paging, hibernateCriteria);
        return hibernateCriteria.list();
    }
}
