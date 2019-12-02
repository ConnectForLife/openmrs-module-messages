package org.openmrs.module.messages.api.dao;

import org.openmrs.api.db.OpenmrsDataDAO;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.domain.PagingInfo;
import org.openmrs.module.messages.domain.PatientTemplateCriteria;

import java.util.List;

public interface PatientTemplateDao extends OpenmrsDataDAO<PatientTemplate> {

    List<PatientTemplate> findAllByCriteria(PatientTemplateCriteria criteria, PagingInfo paging);
}
