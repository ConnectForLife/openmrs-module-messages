/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.openmrs.module.messages.api.model.types.ServiceStatus;

@Entity(name = "messages.DeliveryAttempt")
@Table(name = "messages_delivery_attempt")
public class DeliveryAttempt extends AbstractBaseOpenmrsData {
    
    private static final long serialVersionUID = 8564832003377818271L;
    
    @Id
    @GeneratedValue
    @Column(name = "messages_delivery_attempt_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "scheduled_service_id", nullable = false)
    private ScheduledService scheduledService;
    
    @Column(name = "timestamp", nullable = false)
    private Date timestamp;
    
    @Column(name = "new_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ServiceStatus status;
    
    @Column(name = "attempt_number", nullable = false)
    private int attemptNumber;
    
    @Column(name = "service_execution_id", nullable = false)
    private String serviceExecution;
    
    @Override
    public Integer getId() {
        return id;
    }
    
    @Override
    public void setId(Integer id) {
        this.id = id;
    }
    
    public ScheduledService getScheduledService() {
        return scheduledService;
    }
    
    public void setScheduledService(ScheduledService scheduledService) {
        this.scheduledService = scheduledService;
    }
    
    public Date getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    
    public ServiceStatus getStatus() {
        return status;
    }
    
    public void setStatus(ServiceStatus status) {
        this.status = status;
    }
    
    public int getAttemptNumber() {
        return attemptNumber;
    }
    
    public void setAttemptNumber(int attemptNumber) {
        this.attemptNumber = attemptNumber;
    }
    
    public String getServiceExecution() {
        return serviceExecution;
    }
    
    public void setServiceExecution(String serviceExecution) {
        this.serviceExecution = serviceExecution;
    }
}
