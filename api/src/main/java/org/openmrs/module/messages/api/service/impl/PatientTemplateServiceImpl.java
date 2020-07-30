package org.openmrs.module.messages.api.service.impl;

import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.dto.PatientTemplateDTO;
import org.openmrs.module.messages.api.mappers.PatientTemplateMapper;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.openmrs.module.messages.domain.criteria.PatientTemplateCriteria;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implements methods related to creating, reading, updating and deleting patient templates entities
 */
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

    @Override
    public void voidForPerson(int personId, String reason) throws APIException {
        List<PatientTemplate> templatesForActor = findAllByCriteria(PatientTemplateCriteria.forActorId(personId));
        //Required to use AOP during voiding
        PatientTemplateService openMRSService = Context.getService(PatientTemplateService.class);
        for (PatientTemplate template : templatesForActor) {
            openMRSService.voidPatientTemplate(template, reason);
        }
        List<PatientTemplate> templatesForPatient = findAllByCriteria(PatientTemplateCriteria.forPatientId(personId));
        for (PatientTemplate template : templatesForPatient) {
            openMRSService.voidPatientTemplate(template, reason);
        }
    }

    @Override
    public PatientTemplate voidPatientTemplate(PatientTemplate patientTemplate, String reason) throws APIException {
        if (patientTemplate == null) {
            return null;
        }
        //call the DAO layer directly to avoid any further AOP around save*
        return getDao().saveOrUpdate(patientTemplate);
    }

    public void setPatientTemplateMapper(PatientTemplateMapper patientTemplateMapper) {
        this.patientTemplateMapper = patientTemplateMapper;
    }
}
