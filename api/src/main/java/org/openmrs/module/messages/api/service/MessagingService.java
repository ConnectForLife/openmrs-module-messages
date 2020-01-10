/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service;

import java.util.Date;
import java.util.List;
import org.hibernate.PropertyValueException;
import org.openmrs.module.messages.api.execution.ExecutionException;
import org.openmrs.module.messages.api.execution.ServiceResultList;
import org.openmrs.module.messages.api.model.ActorResponse;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.types.ServiceStatus;

public interface MessagingService extends BaseOpenmrsCriteriaDataService<ScheduledService> {

    /**
     * The API to be called by other modules, such as callflows and sms from not Java environment, in order to update
     * the delivery status of a given service scheduled. The method also invokes rescheduling strategy (determined by
     * Global Property) for all attempts.
     * The method checks if the parameter status (provided as string) could by parsed to {@link ServiceStatus}.
     *
     * @param scheduledServiceId is the id of the ScheduledService for which the status should be updated
     * @param status             is a new value of a service delivery which should be set (eg. delivered, failed)
     * @param timestamp          describes the time when the update took place
     * @param executionId        is the id coming in from the module that is doing the messaging (eg. from the sms module)
     * @return the updated scheduled service
     */
    ScheduledService registerAttempt(int scheduledServiceId, String status, Date timestamp, String executionId);

    /**
     * The API to be called by other modules, such as callflows and sms, in order to update the delivery status of a
     * given service scheduled. The method also invokes rescheduling strategy (determined by Global Property)
     * for all attempts.
     *
     * @param scheduledServiceId is the id of the ScheduledService for which the status should be updated
     * @param status             is a new value of a service delivery which should be set (eg. delivered, failed)
     * @param timestamp          describes the time when the update took place
     * @param executionId        is the id coming in from the module that is doing the messaging (eg. from the sms module)
     * @return the updated scheduled service
     */
    ScheduledService registerAttempt(int scheduledServiceId, ServiceStatus status, Date timestamp, String executionId);

    /**
     * The API to be called by other modules, such as callflows and sms, in order to create actor response.
     *
     * @param scheduledId  is the id of the ScheduledService for which ActorResponse should be created.
     * @param questionId   is an ID of a concept which describes a question.
     * @param responseId   is an ID of a concept which describes a response.
     * @param textResponse is a response text for ActorResponse.
     * @param timestamp    describes the time when the event took place.
     * @return the created actor response.
     * @throws PropertyValueException when scheduled service does not exist.
     */
    ActorResponse registerResponse(Integer scheduledId,
                                   Integer questionId,
                                   Integer responseId,
                                   String textResponse,
                                   Date timestamp) throws PropertyValueException;

    /**
     * Retrieves all service executions bound to occur (or which occurred) in the given date range for a patient.
     * @param patientId id of the patient
     * @param startDate the start of the date range
     * @param endDate the end of the date range
     * @return list of all service executions in the given period
     */
    List<ServiceResultList> retrieveAllServiceExecutions(Integer patientId, Date startDate, Date endDate)
            throws ExecutionException;

    /**
     * Retrieves all service executions bound to occur (or which occurred) in the given date range for all patients.
     * @param startDate the start of the date range
     * @param endDate the end of the date range
     * @return list of all service executions in the given period
     */
    List<ServiceResultList> retrieveAllServiceExecutions(Date startDate, Date endDate)
            throws ExecutionException;
}
