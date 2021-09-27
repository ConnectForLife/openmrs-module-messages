package org.openmrs.module.messages.model;

import org.openmrs.module.messages.api.execution.ServiceResult;
import org.openmrs.module.messages.api.model.types.ServiceStatus;

import java.util.Map;

public class ServiceResultDTO {
    private Long executionDate;
    private Object messageId;
    private String channelType;

    private Integer patientId;
    private Integer actorId;

    private ServiceStatus serviceStatus;
    private Map<String, Object> additionalParams;
    private Integer patientTemplateId;

    public ServiceResultDTO() {
    }

    public ServiceResultDTO(ServiceResult serviceResult) {
        this.executionDate = serviceResult.getExecutionDate().toInstant().toEpochMilli();
        this.messageId = serviceResult.getMessageId();
        this.channelType = serviceResult.getChannelType();
        this.patientId = serviceResult.getPatientId();
        this.actorId = serviceResult.getActorId();
        this.serviceStatus = serviceResult.getServiceStatus();
        this.additionalParams = serviceResult.getAdditionalParams();
        this.patientTemplateId = serviceResult.getPatientTemplateId();
    }

    public Long getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Long executionDate) {
        this.executionDate = executionDate;
    }

    public Object getMessageId() {
        return messageId;
    }

    public void setMessageId(Object messageId) {
        this.messageId = messageId;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getActorId() {
        return actorId;
    }

    public void setActorId(Integer actorId) {
        this.actorId = actorId;
    }

    public ServiceStatus getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(ServiceStatus serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public Map<String, Object> getAdditionalParams() {
        return additionalParams;
    }

    public void setAdditionalParams(Map<String, Object> additionalParams) {
        this.additionalParams = additionalParams;
    }

    public Integer getPatientTemplateId() {
        return patientTemplateId;
    }

    public void setPatientTemplateId(Integer patientTemplateId) {
        this.patientTemplateId = patientTemplateId;
    }
}
