package org.openmrs.module.messages.api.service.impl;

import org.openmrs.module.messages.api.dao.PatientTemplateDao;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.openmrs.module.messages.domain.PagingInfo;
import org.openmrs.module.messages.domain.PatientTemplateCriteria;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class PatientTemplateServiceImpl extends BaseOpenmrsDataService<PatientTemplate>
        implements PatientTemplateService {

    private PatientTemplateDao patientTemplateDao;

    public void setPatientTemplateDao(PatientTemplateDao patientTemplateDao) {
        this.patientTemplateDao = patientTemplateDao;
    }

    @Override
    public List<PatientTemplate> findAllByCriteria(PatientTemplateCriteria criteria, PagingInfo paging) {
        return patientTemplateDao.findAllByCriteria(criteria, paging);
    }
}
