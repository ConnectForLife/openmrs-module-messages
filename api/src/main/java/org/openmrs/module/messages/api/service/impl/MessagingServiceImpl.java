package org.openmrs.module.messages.api.service.impl;

import org.hibernate.PropertyValueException;
import org.openmrs.Concept;
import org.openmrs.api.ConceptService;
import org.openmrs.module.messages.api.dao.ActorResponseDao;
import org.openmrs.module.messages.api.model.ActorResponse;

import java.util.ArrayList;
import java.util.Date;
import javax.transaction.Transactional;

import org.openmrs.module.messages.api.model.DeliveryAttempt;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.module.messages.api.service.MessagingService;

public class MessagingServiceImpl extends BaseOpenmrsDataService<ScheduledService> implements MessagingService {

    private ConceptService conceptService;

    private ActorResponseDao actorResponseDao;

    public void setConceptService(ConceptService conceptService) {
        this.conceptService = conceptService;
    }

    public void setActorResponseDao(ActorResponseDao actorResponseDao) {
        this.actorResponseDao = actorResponseDao;
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
