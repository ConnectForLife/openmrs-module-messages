/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.SQLGrammarException;
import org.openmrs.Concept;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.ConceptService;
import org.openmrs.api.PatientService;
import org.openmrs.api.PersonService;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.dao.ActorResponseDao;
import org.openmrs.module.messages.api.exception.EntityNotFoundException;
import org.openmrs.module.messages.api.execution.ServiceExecutor;
import org.openmrs.module.messages.api.execution.ServiceResultList;
import org.openmrs.module.messages.api.model.ActorResponse;
import org.openmrs.module.messages.api.model.ActorResponseType;
import org.openmrs.module.messages.api.model.DeliveryAttempt;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Range;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.module.messages.api.service.MessagingService;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.openmrs.module.messages.api.util.DateUtil;
import org.openmrs.module.messages.api.util.HibernateUtil;
import org.openmrs.module.messages.domain.criteria.LastResponseCriteria;
import org.openmrs.module.messages.domain.criteria.PatientTemplateCriteria;
import org.openmrs.module.messages.domain.criteria.ScheduledServiceCriteria;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional
public class MessagingServiceImpl extends BaseOpenmrsDataService<ScheduledService> implements MessagingService {

    private static final Log LOGGER = LogFactory.getLog(MessagingServiceImpl.class);
    public static final int ONE = 1;

    private ConceptService conceptService;
    private ActorResponseDao actorResponseDao;
    private ServiceExecutor serviceExecutor;
    private PatientTemplateService patientTemplateService;
    private PersonService personService;
    private PatientService patientService;
    
    @Override
    @SuppressWarnings("checkstyle:ParameterNumber")
    public ActorResponse registerResponse(Integer actorId, Integer patientId, String sourceId, String sourceType,
            Integer questionId, String textQuestion, Integer responseId, String textResponse, Date timestamp) {
        Person actor = actorId == null ? null : personService.getPerson(actorId);
        Patient patient = patientId == null ? null : patientService.getPatient(patientId);
        Concept question = questionId == null ? null : conceptService.getConcept(questionId);
        Concept response = responseId == null ? null : conceptService.getConcept(responseId);
        ActorResponseType responseType = new ActorResponseType(sourceType);
        
        ActorResponse actorResponse = new ActorResponse();
        actorResponse.setActor(actor);
        actorResponse.setPatient(patient);
        actorResponse.setSourceId(sourceId);
        actorResponse.setSourceType(responseType);
        actorResponse.setQuestion(question);
        actorResponse.setTextQuestion(textQuestion);
        actorResponse.setResponse(response);
        actorResponse.setTextResponse(textResponse);
        actorResponse.setAnsweredTime(timestamp);
        
        return actorResponseDao.saveOrUpdate(actorResponse);
    }
    
    @Override
    public ScheduledService registerAttempt(int scheduledServiceId, String status, Date timestamp, String executionId) {
        return registerAttempt(scheduledServiceId, ServiceStatus.valueOf(status), timestamp, executionId);
    }
    
    @Override
    public ScheduledService registerAttempt(int scheduledServiceId, ServiceStatus status, Date timestamp,
                                            String executionId) {
        checkIfStatusIsNotPendingOrFuture(status);
        ScheduledService service = HibernateUtil.getNotNull(scheduledServiceId, this);
        return registerAttempt(service, status, timestamp, executionId);
    }
    
    @Override
    public ScheduledService registerAttempt(ScheduledService service, ServiceStatus status, Date timestamp,
                                            String executionId) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(String.format(
                    "Called registerAttempt with scheduledServiceId=%d, status=%s, timestamp=%s, executionId=%s",
                    service.getId(),
                    status.name(),
                    timestamp,
                    executionId));
        }
        
        DeliveryAttempt deliveryAttempt = new DeliveryAttempt();
        deliveryAttempt.setServiceExecution(executionId);
        deliveryAttempt.setAttemptNumber(service.getNumberOfAttempts() + 1);
        deliveryAttempt.setStatus(status);
        deliveryAttempt.setTimestamp(timestamp);
        deliveryAttempt.setScheduledService(service);
        
        service.setStatus(status);
        service.setLastServiceExecution(executionId);
        service.getDeliveryAttempts().add(deliveryAttempt);
        
        return saveOrUpdate(service);
    }
    
    @Override
    @SuppressWarnings("checkstyle:ParameterNumber")
    public ScheduledService registerResponseAndStatus(Integer scheduledId,
                                                      Integer questionId,
                                                      String textQuestion,
                                                      Integer responseId,
                                                      String textResponse,
                                                      String status,
                                                      Date timestamp,
                                                      String executionId) throws PropertyValueException {
        registerResponseForScheduledService(scheduledId, questionId, textQuestion, responseId, textResponse, timestamp);
        return registerAttempt(scheduledId, status, timestamp, executionId);
    }
    
    @Override
    @SuppressWarnings("checkstyle:ParameterNumber")
    public ScheduledService registerResponseAndStatus(Integer scheduledId,
                                                      Integer questionId,
                                                      String textQuestion,
                                                      Integer responseId,
                                                      String textResponse,
                                                      ServiceStatus status,
                                                      Date timestamp,
                                                      String executionId) throws PropertyValueException {
        registerResponseForScheduledService(scheduledId, questionId, textQuestion, responseId, textResponse, timestamp);
        return registerAttempt(scheduledId, status, timestamp, executionId);
    }
    
    @Override
    @Transactional(noRollbackFor = {RuntimeException.class, SQLGrammarException.class}, readOnly = true)
    public List<ServiceResultList> retrieveAllServiceExecutions(Integer patientId, Date startDate, Date endDate) {
        
        PatientTemplateCriteria patientTemplateCriteria = PatientTemplateCriteria.forPatientId(patientId);
        return retrieveAllServiceExecutions(patientTemplateService.findAllByCriteria(patientTemplateCriteria),
                startDate, endDate);
    }
    
    @Override
    @Transactional(noRollbackFor = {RuntimeException.class, SQLGrammarException.class}, readOnly = true)
    public List<ServiceResultList> retrieveAllServiceExecutions(Date startDate, Date endDate) {
        return retrieveAllServiceExecutions(patientTemplateService.getAll(false), startDate, endDate);
    }
    
    @Override
    public ActorResponse updateActorResponse(Integer actorResponseId, Integer newResponseId, String newResponseTxt)
            throws EntityNotFoundException {
        if (actorResponseId == null) {
            throw new EntityNotFoundException("Actor response identifier cannot be null!");
        }
        ActorResponse current = actorResponseDao.getById(actorResponseId);
        throwExceptionIfMissing(current, actorResponseId, ActorResponse.class.getName());
        
        if (newResponseId != null) {
            Concept newResponse = conceptService.getConcept(newResponseId);
            throwExceptionIfMissing(newResponse, newResponseId, Concept.class.getName());
            current.setResponse(newResponse);
        }
        
        current.setAnsweredTime(DateUtil.now());
        current.setTextResponse(newResponseTxt);
        return actorResponseDao.saveOrUpdate(current);
    }
    
    @Override
    public List<ActorResponse> getLastActorResponsesForConceptQuestion(Integer patientId,
                                                                       Integer actorId,
                                                                       Integer conceptQuestionId,
                                                                       Integer pageSize) {
        return actorResponseDao.findAllByCriteria(new LastResponseCriteria()
                        .setActorId(actorId)
                        .setPatientId(patientId)
                        .setConceptQuestionId(conceptQuestionId)
                        .setLimit(pageSize)
                        , null);
    }
    
    @Override
    public List<ActorResponse> getLastActorResponsesForTextQuestion(Integer patientId,
                                                                    Integer actorId,
                                                                    String textQuestion,
                                                                    Integer pageSize) {
        return actorResponseDao.findAllByCriteria(new LastResponseCriteria()
                        .setActorId(actorId)
                        .setPatientId(patientId)
                        .setTextQuestion(textQuestion)
                        .setLimit(pageSize)
                        , null);
    }
    
    @Override
    public List<ActorResponse> getLastActorResponsesForServiceType(Integer patientId,
                                                                   Integer actorId,
                                                                   String serviceType,
                                                                   Integer limit) {
        return actorResponseDao.findAllByCriteria(new LastResponseCriteria()
                        .setPatientId(patientId)
                        .setActorId(actorId)
                        .setServiceType(serviceType)
                        .setLimit(limit)
                        , null);
    }
    
    @Override
    public List<ScheduledService> getScheduledServicesByGroupId(Integer groupId) {
        return findAllByCriteria(ScheduledServiceCriteria.forGroupId(groupId));
    }
    
    @Override
    public List<ScheduledService> getScheduledServicesByPatientIdAndActorId(Integer patientId, Integer actorId) {
        return findAllByCriteria(ScheduledServiceCriteria
                .forPatientTemplateActorId(actorId).forPatientTemplatePatientId(patientId));
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
    
    private void throwExceptionIfMissing(Object entity, Integer id, String entityName) throws EntityNotFoundException {
        if (entity == null) {
            throw new EntityNotFoundException(String.format(
                    "Could not find %s with identifier: %s", entityName, id));
        }
    }
    
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }
    
    public void setPatientService(PatientService patientService) {
        this.patientService = patientService;
    }
    
    private List<ServiceResultList> retrieveAllServiceExecutions(List<PatientTemplate> patientTemplates, Date startDate,
                                                                 Date endDate) {
        List<ServiceResultList> results = new ArrayList<>();
        for (PatientTemplate patientTemplate : patientTemplates) {
            try {
                Range<Date> dateRange = new Range<>(startDate, endDate);
                if (patientTemplate.isDeactivated()) {
                    dateRange = new Range<>(startDate, getMaxExecutionDate(patientTemplate));
                    if (LOGGER.isTraceEnabled()) {
                        LOGGER.trace(String.format(
                                "PatientTemplate %d is disabled, so applying the max execution date or now as " +
                                        "the end date (%s instead of %s)",
                                patientTemplate.getId(),
                                DateUtil.convertToServerSideDateTime(dateRange.getEnd()),
                                DateUtil.convertToServerSideDateTime(endDate)));
                    }
                }
                results.add(serviceExecutor.execute(patientTemplate, dateRange));
            } catch (Exception e) {
                logException(patientTemplate, e);
            }
        }
        
        return results;
    }

    private Date getMaxExecutionDate(PatientTemplate patientTemplate) {
        List<ScheduledService> services = findAllByCriteria(
                ScheduledServiceCriteria.forLastExecution(patientTemplate.getId()));
        if (services.size() > ONE) {
            LOGGER.warn("Found more then 1 service for the ScheduledServiceCriteria.forLastExecution criteria");
        }
        if (services.size() >= ONE
                && services.get(0).getGroup() != null
                && services.get(0).getGroup().getMsgSendTime() != null) {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace(String.format("PatientTemplate %d ('%s') has got already scheduled tasks at the future, "
                                + "so the last execution date will be applied as the end date",
                        patientTemplate.getId(),
                        patientTemplate.getTemplate().getName()));
            }
            return services.get(0).getGroup().getMsgSendTime();
        }
        return DateUtil.now();
    }

    private void checkIfStatusIsNotPendingOrFuture(ServiceStatus status) {
        if (ServiceStatus.PENDING.equals(status) || ServiceStatus.FUTURE.equals(status)) {
            throw new IllegalArgumentException(String.format(
                    "%s status cannot be registered", status.name()));
        }
    }
    
    private void registerResponseForScheduledService(Integer scheduledId, Integer questionId, String textQuestion,
                                                     Integer responseId, String textResponse, Date timestamp) {
        ScheduledService scheduled = HibernateUtil.getNotNull(scheduledId, this);
        Integer actorId = scheduled.getGroup().getActor().getPersonId();
        Integer patientId = scheduled.getGroup().getPatient().getPatientId();
        String sourceId = scheduled.getGroup().getId().toString();
        String sourceType = MessagesConstants.DEFAULT_ACTOR_RESPONSE_TYPE;
        registerResponse(actorId, patientId, sourceId, sourceType, questionId, textQuestion,
                responseId, textResponse, timestamp);
    }

    private void logException(PatientTemplate patientTemplate, Exception e) {
        try {
            LOGGER.error(String.format(
                    "Cannot execute query for patientTemplate with id %d (template with id %d - '%s'), " +
                            "related to patient %s (uuid: %s)" +
                            "The execution of this particular patient template will be skipped.",
                    patientTemplate.getId(),
                    patientTemplate.getTemplate().getId(),
                    patientTemplate.getTemplate().getName(),
                    patientTemplate.getPatient().getPersonName().getFullName(),
                    patientTemplate.getPatient().getUuid()),
                    e);
        } catch (Exception ex) {
            LOGGER.error(String.format(
                    "Cannot execute query for patientTemplate with id %d. The details couldn't be extracted." +
                     "The execution of this particular patient template will be skipped.",
                    patientTemplate.getId()), e);
        }
    }
}
