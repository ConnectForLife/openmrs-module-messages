/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "messages.ScheduleServiceParameter")
@Table(name = "messages_schedule_service_parameter")
public class ScheduledServiceParameter extends AbstractBaseOpenmrsData {
    
    private static final long serialVersionUID = 1573774741059669932L;
    
    @Id
    @GeneratedValue
    @Column(name = "messages_schedule_service_parameter_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "scheduled_message")
    private ScheduledService scheduledMessage;
    
    @Column(name = "parameter_type")
    private String parameterType;
    
    @Column(name = "parameter_value", columnDefinition = "text")
    private String parameterValue;

    public ScheduledServiceParameter() {
    }

    public ScheduledServiceParameter(String parameterType, String parameterValue) {
        this.parameterType = parameterType;
        this.parameterValue = parameterValue;
    }

    @Override
    public Integer getId() {
        return id;
    }
    
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ScheduledServiceParameter#" + id;
    }

    public ScheduledService getScheduledMessage() {
        return scheduledMessage;
    }

    public void setScheduledMessage(ScheduledService scheduledMessage) {
        this.scheduledMessage = scheduledMessage;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }
}
