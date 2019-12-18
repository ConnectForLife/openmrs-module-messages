package org.openmrs.module.messages.api.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PatientTemplateDTO implements Serializable {

    private static final long serialVersionUID = -6043667008851204408L;

    private Integer id;

    private List<TemplateFieldValueDTO> templateFieldValues = new ArrayList<TemplateFieldValueDTO>();

    private Integer patientId;

    private Integer templateId;

    private Integer actorId;

    private Integer actorTypeId;

    private String serviceQuery;

    private String serviceQueryType;

    private String uuid;

    public PatientTemplateDTO() {
    }

    public Integer getId() {
        return id;
    }

    public PatientTemplateDTO withId(Integer id) {
        this.id = id;
        return this;
    }

    public List<TemplateFieldValueDTO> getTemplateFieldValues() {
        return templateFieldValues;
    }

    public PatientTemplateDTO withTemplateFieldValues(List<TemplateFieldValueDTO> templateFieldValues) {
        this.templateFieldValues = templateFieldValues;
        return this;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public PatientTemplateDTO withPatientId(Integer patientId) {
        this.patientId = patientId;
        return this;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public PatientTemplateDTO withTemplateId(Integer templateId) {
        this.templateId = templateId;
        return this;
    }

    public Integer getActorId() {
        return actorId;
    }

    public PatientTemplateDTO withActorId(Integer actorId) {
        this.actorId = actorId;
        return this;
    }

    public Integer getActorTypeId() {
        return actorTypeId;
    }

    public PatientTemplateDTO withActorTypeId(Integer actorTypeId) {
        this.actorTypeId = actorTypeId;
        return this;
    }

    public String getServiceQuery() {
        return serviceQuery;
    }

    public PatientTemplateDTO withServiceQuery(String serviceQuery) {
        this.serviceQuery = serviceQuery;
        return this;
    }

    public String getServiceQueryType() {
        return serviceQueryType;
    }

    public PatientTemplateDTO withServiceQueryType(String serviceQueryType) {
        this.serviceQueryType = serviceQueryType;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public PatientTemplateDTO withUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }
}
