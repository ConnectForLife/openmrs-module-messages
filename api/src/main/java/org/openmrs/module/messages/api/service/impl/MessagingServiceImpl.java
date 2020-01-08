/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
 
package org.openmrs.module.messages.api.service.impl;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.PropertyValueException;
import org.openmrs.Concept;
import org.openmrs.api.ConceptService;
import org.openmrs.module.messages.api.config.ConfigService;
import org.openmrs.module.messages.api.dao.ActorResponseDao;
import org.openmrs.module.messages.api.execution.ExecutionException;
import org.openmrs.module.messages.api.execution.ServiceExecutor;
import org.openmrs.module.messages.api.execution.ServiceResultList;
import org.openmrs.module.messages.api.model.ActorResponse;
import org.openmrs.module.messages.api.model.DeliveryAttempt;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Range;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.module.messages.api.service.MessagingService;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.openmrs.module.messages.domain.criteria.PatientTemplateCriteria;

@Transactional
public class MessagingServiceImpl extends BaseOpenmrsDataService<ScheduledService> implements MessagingService {

    private ConceptService conceptService;
    private ActorResponseDao actorResponseDao;
    private ServiceExecutor serviceExecutor;
    private PatientTemplateService patientTemplateService;
    private ConfigService configService;

    @Override
    public ActorResponse registerResponse(Integer scheduledId,
                                          Integer questionId,
                                          Integer responseId,
                                          String textResponse,
                                          Date timestamp) throws PropertyValueException {

        ScheduledService scheduled = getById(scheduledId);
        Concept question = conceptService.getConcept(questionId);
        Concept response = conceptService.getConcept(responseId);

        ActorResponse actorResponse = new ActorResponse();
        actorResponse.setScheduledService(scheduled);
        actorResponse.setQuestion(question);
        actorResponse.setResponse(response);
        actorResponse.setTextResponse(textResponse);
        actorResponse.setAnsweredTime(timestamp);

        return actorResponseDao.saveOrUpdate(actorResponse);
    }

    @Override
    public List<ServiceResultList> retrieveAllServiceExecutions(Integer patientId, Date startDate, Date endDate)
            throws ExecutionException {

        PatientTemplateCriteria patientTemplateCriteria = PatientTemplateCriteria.forPatientId(patientId);
        return retrieveAllServiceExecutions(patientTemplateService.findAllByCriteria(patientTemplateCriteria),
                startDate, endDate);
    }

    @Override
    public List<ServiceResultList> retrieveAllServiceExecutions(Date startDate, Date endDate)
            throws ExecutionException {
        return retrieveAllServiceExecutions(patientTemplateService.getAll(false), startDate, endDate);
    }

    @Override
    public ScheduledService registerAttempt(int scheduledServiceId, ServiceStatus status, Date timestamp,
                                            String executionId) {
        ScheduledService scheduledService = getById(scheduledServiceId);

        DeliveryAttempt deliveryAttempt = new DeliveryAttempt();
        deliveryAttempt.setServiceExecution(executionId);
        deliveryAttempt.setAttemptNumber(scheduledService.getNumberOfAttempts() + 1);
        deliveryAttempt.setStatus(status);
        deliveryAttempt.setTimestamp(timestamp);
        deliveryAttempt.setScheduledService(scheduledService);

        scheduledService.setStatus(status);
        scheduledService.setLastServiceExecution(executionId);

        scheduledService.getDeliveryAttempts().add(deliveryAttempt);
        scheduledService = saveOrUpdate(scheduledService);

        configService.getReschedulingStrategy().execute(scheduledService);

        return scheduledService;
    }

    public void setConceptService(ConceptService conceptService) {
        this.conceptService = conceptService;
    }

    public void setActorResponseDao(ActorResponseDao actorResponseDao) {
        this.actorResponseDao = actorResponseDao;
    }

    public void setServiceExecutor(ServiceExecutor serviceExecutor) {
        this.serviceExecutor = serviceExecutor;
    }

    public void setPatientTemplateService(PatientTemplateService patientTemplateService) {
        this.patientTemplateService = patientTemplateService;
    }

    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    private List<ServiceResultList> retrieveAllServiceExecutions(List<PatientTemplate> patientTemplates, Date startDate,
                                                                 Date endDate)
            throws ExecutionException {

        Range<Date> dateRange = new Range<>(startDate, endDate);

        List<ServiceResultList> results = new ArrayList<>();
        for (PatientTemplate patientTemplate  : patientTemplates) {
            results.add(serviceExecutor.execute(patientTemplate, dateRange));
        }

        return results;
    }
}
