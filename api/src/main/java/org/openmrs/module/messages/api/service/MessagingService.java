/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service;

import org.openmrs.module.messages.api.exception.EntityNotFoundException;
import org.openmrs.module.messages.api.execution.ServiceResultList;
import org.openmrs.module.messages.api.model.ActorResponse;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.types.ServiceStatus;

import java.util.Date;
import java.util.List;

public interface MessagingService extends BaseOpenmrsCriteriaDataService<ScheduledService> {

    /**
     * The API to be called by other modules, such as callflows and sms from non-Java environment, in order to update
     * the delivery status of a given service scheduled.
     * The method checks if the parameter status (provided as string) could by parsed to {@link ServiceStatus}.
     *
     * @param scheduledServiceId is an id of the ScheduledService for which the status should be updated
     * @param status             is a new value of a service delivery which should be set
     *                           (valid values: DELIVERED, FAILED)
     * @param timestamp          describes the time when the update took place
     * @param executionId        is the id coming in from the module that is doing the messaging (eg. from the sms module)
     * @return the updated scheduled service
     * @throws EntityNotFoundException when scheduled service with passed id does not exist.
     * @throws IllegalArgumentException when {@param status} could not by parsed to {@link ServiceStatus}
     *                                  and when is PENDING or FUTURE.
     */
    ScheduledService registerAttempt(int scheduledServiceId, String status, Date timestamp, String executionId);

    /**
     * The API to be called by other modules, such as callflows and sms, in order to update the delivery status of a
     * given service scheduled.
     *
     * @param scheduledServiceId is an id of the ScheduledService for which the status should be updated
     * @param status             is a new value of a service delivery which should be set (eg. delivered, failed)
     * @param timestamp          describes the time when the update took place
     * @param executionId        is the id coming in from the module that is doing the messaging (eg. from the sms module)
     * @return the updated scheduled service
     * @throws EntityNotFoundException when scheduled service with passed id does not exist.
     * @throws IllegalArgumentException when {@param status} is PENDING or FUTURE.
     */
    ScheduledService registerAttempt(int scheduledServiceId, ServiceStatus status, Date timestamp, String executionId);

    /**
     * The API to be called in order to update the delivery status of a given {@param scheduledService}.
     *
     * @param scheduledService is an id of the ScheduledService for which the status should be updated
     * @param status           is a new value of a service delivery which should be set (eg. delivered, failed)
     * @param timestamp        describes the time when the update took place
     * @param executionId      is the id coming in from the module that is doing the messaging (eg. from the sms module)
     * @return the updated scheduled service
     * @throws IllegalArgumentException when {@param status} is PENDING or FUTURE.
     */
    ScheduledService registerAttempt(ScheduledService scheduledService, ServiceStatus status, Date timestamp,
                                     String executionId);

    /**
     * The API to be called by other modules, such as callflows and sms, in order to create actor response.
     *
     * @param actorId is an id of the Person (actor) for which {@link ActorResponse} should be created.
     * @param patientId is an id of the Patient which is related to this {@link ActorResponse}.
     * @param sourceId is an string identifier of the response which is used to join this method with specific message.
     * @param sourceType is an string value of {@link org.openmrs.module.messages.api.model.ActorResponseType}.
     * @param questionId is an optional id of a concept which describes a question.
     * @param textQuestion is an optional string value of question.
     * @param responseId is an optional id of a concept which describes a response.
     * @param textResponse is an optional response text for ActorResponse.
     * @param timestamp describes the time when the event took place.
     * @return the created actor response.
     * @throws EntityNotFoundException when scheduled service with passed id does not exist.
     */
    @SuppressWarnings("checkstyle:ParameterNumber")
    ActorResponse registerResponse(Integer actorId,
                                   Integer patientId,
                                   String sourceId,
                                   String sourceType,
                                   Integer questionId,
                                   String textQuestion,
                                   Integer responseId,
                                   String textResponse,
                                   Date timestamp);

    /**
     * The API to be called by other modules, such as callflows and sms from non-Java environment, in order to create
     * actor response and update the delivery status of a given service scheduled. The method also invokes rescheduling
     * strategy (determined by Global Property) for all attempts.
     *
     * It should be used only if we registered the last response for service.
     *
     * @param scheduledId  is an id of the ScheduledService for which ActorResponse should be created.
     * @param questionId   is an id of a concept which describes a question.
     * @param textQuestion is an optional string value of question.
     * @param responseId   is an id of a concept which describes a response.
     * @param textResponse is a response text for ActorResponse.
     * @param status       is a new value of a service delivery which should be set
     *                     (valid values: DELIVERED, FAILED)
     * @param timestamp    describes the time when the event took place.
     * @param executionId  is the id coming in from the module that is doing the messaging (eg. from the sms module)
     * @return the created actor response.
     * @throws EntityNotFoundException when scheduled service with passed id does not exist.
     * @throws IllegalArgumentException when {@param status} could not by parsed to {@link ServiceStatus}
     *                                  and when is PENDING or FUTURE.
     *
     */
    @SuppressWarnings("checkstyle:ParameterNumber")
    ScheduledService registerResponseAndStatus(Integer scheduledId,
                                               Integer questionId,
                                               String textQuestion,
                                               Integer responseId,
                                               String textResponse,
                                               String status,
                                               Date timestamp,
                                               String executionId);

    /**
     * The API to be called by other modules, such as callflows and sms, in order to create actor response and update
     * the delivery status of a given service scheduled. The method also invokes rescheduling strategy (determined by
     * Global Property) for all attempts.
     *
     * @param scheduledId  is an id of the ScheduledService for which ActorResponse should be created.
     * @param questionId   is an id of a concept which describes a question.
     * @param textQuestion is an optional string value of question.
     * @param responseId   is an id of a concept which describes a response.
     * @param textResponse is a response text for ActorResponse.
     * @param status       is a new value of a service delivery which should be set (eg. delivered, failed)
     * @param timestamp    describes the time when the event took place.
     * @param executionId  is the id coming in from the module that is doing the messaging (eg. from the sms module)
     * @return the created actor response.
     * @throws EntityNotFoundException when scheduled service with passed id does not exist.
     * @throws IllegalArgumentException when {@param status} is PENDING or FUTURE.
     */
    @SuppressWarnings("checkstyle:ParameterNumber")
    ScheduledService registerResponseAndStatus(Integer scheduledId,
                                               Integer questionId,
                                               String textQuestion,
                                               Integer responseId,
                                               String textResponse,
                                               ServiceStatus status,
                                               Date timestamp,
                                               String executionId);

    /**
     * Retrieves all service executions bound to occur (or which occurred) in the given date range for a patient.
     * @param patientId id of the patient
     * @param startDate the start of the date range
     * @param endDate the end of the date range
     * @return list of all service executions in the given period
     */
    List<ServiceResultList> retrieveAllServiceExecutions(Integer patientId, Date startDate, Date endDate);

    /**
     * Retrieves all service executions bound to occur (or which occurred) in the given date range for all patients.
     * @param startDate the start of the date range
     * @param endDate the end of the date range
     * @return list of all service executions in the given period
     */
    List<ServiceResultList> retrieveAllServiceExecutions(Date startDate, Date endDate);

    /**
     * Updates specific ActorResponse entity.
     * @param actorResponseId database identifier of actor response which will be updated
     * @param newResponseId new response concept
     * @param newResponseTxt new response text
     * @return new version of actor response persisted by database.
     */
    ActorResponse updateActorResponse(Integer actorResponseId, Integer newResponseId, String newResponseTxt)
            throws EntityNotFoundException;
    
    /**
     * Retrieves the specified number of last actor responses for question that is a concept type.
     * @param patientId id of patient
     * @param actorId id of actor
     * @param conceptQuestionId id of concept type question
     * @param pageSize specifies number of records to return
     * @return list of actor responses for specified concept question
     */
    List<ActorResponse> getLastActorResponsesForConceptQuestion(Integer patientId,
                                                                Integer actorId,
                                                                Integer conceptQuestionId,
                                                                Integer pageSize);
    
    /**
     * Retrieves the specified number of last actor responses for question that is a String type.
     * @param patientId id of patient
     * @param actorId id of actor
     * @param textQuestion value of string type question
     * @param pageSize specifies number of records to return
     * @return list of actor responses for specified String question
     */
    List<ActorResponse> getLastActorResponsesForTextQuestion(Integer patientId,
                                                             Integer actorId,
                                                             String textQuestion,
                                                             Integer pageSize);
    
    /**
     * Retrieves the specified number of last actor responses for service type.
     * @param patientId id of patient
     * @param actorId id of actor
     * @param serviceType service type name e.g. Health tip
     * @param limit specifies number of records to return
     * @return
     */
    List<ActorResponse> getLastActorResponsesForServiceType(Integer patientId,
                                                            Integer actorId,
                                                            String serviceType,
                                                            Integer limit);
    
    /**
     * Retrieves all scheduled services assigned to group.
     * @param groupId id of the group
     * @return list of all scheduled services from particular group
     */
    List<ScheduledService> getScheduledServicesByGroupId(Integer groupId);
    
    /**
     * Retrieves all scheduled services by patient and actor id.
     * @param patientId id of patient
     * @param actorId id of actor
     * @return list of all scheduled services for particular patient and actor id
     */
    List<ScheduledService> getScheduledServicesByPatientIdAndActorId(Integer patientId, Integer actorId);

    /**
     * Retrieves all service executions bound to occur (or which occurred) in the given date range for the actor.
     * @param personId id of the actor
     * @param startDate the start of the date range
     * @param endDate the end of the date range
     * @return list of all service executions in the given period
     */
    List<ServiceResultList> retrieveAllServiceExecutionsForActor(Integer personId, Date startDate, Date endDate);
}
