package org.openmrs.module.messages.api.service.impl;

import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class PatientTemplateServiceImpl extends BaseOpenmrsDataService<PatientTemplate>
        implements PatientTemplateService { }
