package org.openmrs.module.messages.api.service;

import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.domain.PagingInfo;
import org.openmrs.module.messages.domain.PatientTemplateCriteria;

import java.util.List;

public interface PatientTemplateService extends OpenmrsDataService<PatientTemplate> {
    List<PatientTemplate> findAllByCriteria(PatientTemplateCriteria criteria, PagingInfo paging);
}
