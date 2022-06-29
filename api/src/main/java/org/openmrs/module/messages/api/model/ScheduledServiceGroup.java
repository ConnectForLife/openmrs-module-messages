/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.model;

import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.module.messages.api.model.types.ServiceStatus;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "messages.ScheduledServiceGroup")
@Table(name = "messages_scheduled_service_group")
public class ScheduledServiceGroup extends AbstractBaseOpenmrsData {

    private static final long serialVersionUID = -2938591098039855643L;

    @Id
    @GeneratedValue
    @Column(name = "messages_scheduled_service_group_id")
    private Integer id;

    @Column(name = "msg_send_time")
    private Date msgSendTime;

    @OneToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @OneToOne
    @JoinColumn(name = "actor_id", nullable = false)
    private Person actor;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ServiceStatus status;

    @Column(name = "channel_type", nullable = false)
    private String channelType;

    @OneToMany(mappedBy = "group", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ScheduledService> scheduledServices = new ArrayList<>();

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
        return "ScheduledServiceGroup#" + id;
    }

    public Date getMsgSendTime() {
        return msgSendTime;
    }

    public void setMsgSendTime(Date msgSendTime) {
        this.msgSendTime = msgSendTime;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public ServiceStatus getStatus() {
        return status;
    }

    public void setStatus(ServiceStatus status) {
        this.status = status;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public List<ScheduledService> getScheduledServices() {
        return scheduledServices;
    }

    public void setScheduledServices(List<ScheduledService> scheduledServices) {
        this.scheduledServices = scheduledServices;
        for (ScheduledService service : scheduledServices) {
            service.setGroup(this);
        }
    }

    public Person getActor() {
        return actor;
    }

    public void setActor(Person actor) {
        this.actor = actor;
    }
}
