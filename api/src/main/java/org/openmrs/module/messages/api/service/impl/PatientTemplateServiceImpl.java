package org.openmrs.module.messages.api.service.impl;

import org.openmrs.api.APIException;
import org.openmrs.module.messages.api.dto.PatientTemplateDTO;
import org.openmrs.module.messages.api.mappers.PatientTemplateMapper;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.openmrs.module.messages.domain.criteria.PatientTemplateCriteria;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class PatientTemplateServiceImpl extends BaseOpenmrsDataService<PatientTemplate>
        implements PatientTemplateService {

    private PatientTemplateMapper patientTemplateMapper;

    @Override
    @Transactional
    public List<PatientTemplate> batchSave(List<PatientTemplateDTO> newDtos, int patientId)
            throws APIException {

        List<PatientTemplate> existingPt = findAllByCriteria(PatientTemplateCriteria.forPatientId(patientId));
        patientTemplateMapper.updateFromDtos(newDtos, existingPt);
        return saveOrUpdate(existingPt);
    }

    public void setPatientTemplateMapper(PatientTemplateMapper patientTemplateMapper) {
        this.patientTemplateMapper = patientTemplateMapper;
    }
}
