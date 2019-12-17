package org.openmrs.module.messages.api.service.impl;

import org.openmrs.api.APIException;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.openmrs.module.messages.api.util.IdSetWrapper;
import org.openmrs.module.messages.domain.criteria.PatientTemplateCriteria;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PatientTemplateServiceImpl extends BaseOpenmrsDataService<PatientTemplate>
        implements PatientTemplateService {

    @Override
    @Transactional
    public List<PatientTemplate> batchSave(List<PatientTemplate> patientTemplates, int patientId)
            throws APIException {

        List<PatientTemplate> result = saveOrUpdate(patientTemplates);
        deleteAbsent(patientTemplates, patientId);
        return result;
    }

    private void deleteAbsent(List<PatientTemplate> patientTemplates, int patientId) throws APIException {
        Set<IdSetWrapper<PatientTemplate>> fromDb = IdSetWrapper.<PatientTemplate>wrapNonNull(
                findAllByCriteria(PatientTemplateCriteria.forPatientId(patientId)));
        Set<IdSetWrapper<PatientTemplate>> difference = new HashSet<IdSetWrapper<PatientTemplate>>(fromDb);
        difference.removeAll(IdSetWrapper.<PatientTemplate>wrapNonNull(patientTemplates));

        delete(IdSetWrapper.<PatientTemplate>unwrap(difference));
    }

}
