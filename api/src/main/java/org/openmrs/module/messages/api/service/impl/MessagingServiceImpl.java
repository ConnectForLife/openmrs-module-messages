package org.openmrs.module.messages.api.service.impl;

import org.hibernate.PropertyValueException;
import org.openmrs.Concept;
import org.openmrs.api.ConceptService;
import org.openmrs.module.messages.api.dao.ActorResponseDao;
import org.openmrs.module.messages.api.execution.ExecutionException;
import org.openmrs.module.messages.api.execution.ServiceExecutor;
import org.openmrs.module.messages.api.execution.ServiceResultList;
import org.openmrs.module.messages.api.model.ActorResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;

import org.openmrs.module.messages.api.model.DeliveryAttempt;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Range;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.module.messages.api.service.MessagingService;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.openmrs.module.messages.domain.criteria.PatientTemplateCriteria;

public class MessagingServiceImpl extends BaseOpenmrsDataService<ScheduledService> implements MessagingService {

    private ConceptService conceptService;
    private ActorResponseDao actorResponseDao;
    private ServiceExecutor serviceExecutor;
    private PatientTemplateService patientTemplateService;

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

    @Override
    @Transactional
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
    @Transactional
    public List<ServiceResultList> retrieveAllServiceExecutions(Integer patientId, Date startDate, Date endDate)
            throws ExecutionException {

        PatientTemplateCriteria patientTemplateCriteria = PatientTemplateCriteria.forPatientId(patientId);
        List<PatientTemplate> patientTemplates = patientTemplateService.findAllByCriteria(patientTemplateCriteria);

        Range<Date> dateRange = new Range<>(startDate, endDate);

        List<ServiceResultList> results = new ArrayList<>();
        for (PatientTemplate patientTemplate  : patientTemplates) {
            ServiceResultList resultList = serviceExecutor.execute(patientTemplate, dateRange);
            results.add(resultList);
        }

        return results;
    }

    @Override
    @Transactional
    public ScheduledService registerAttempt(int scheduledServiceId, ServiceStatus status, Date timestamp,
                                            String executionId) {
        ScheduledService scheduledServices = getById(scheduledServiceId);

        DeliveryAttempt deliveryAttempt = new DeliveryAttempt();
        deliveryAttempt.setServiceExecution(executionId);
        deliveryAttempt.setAttemptNumber(scheduledServices.getDeliveryAttempts().size() + 1);
        deliveryAttempt.setStatus(status);
        deliveryAttempt.setTimestamp(timestamp);
        deliveryAttempt.setScheduledService(scheduledServices);

        scheduledServices.setStatus(status);
        scheduledServices.setLastServiceExecution(executionId);

        ArrayList<DeliveryAttempt> deliveryAttempts = new ArrayList<>(scheduledServices.getDeliveryAttempts());
        deliveryAttempts.add(deliveryAttempt);
        scheduledServices.setDeliveryAttempts(deliveryAttempts);

        return saveOrUpdate(scheduledServices);
    }
}
