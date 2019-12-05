package org.openmrs.module.messages.api.dto;

import java.io.Serializable;
import java.util.List;

public class PatientTemplateDTO implements Serializable {

    private static final long serialVersionUID = -6043667008851204408L;

    private Integer id;

    private List<TemplateFieldValueDTO> templateFieldValues;

    private Integer patientId;

    private Integer templateId;

    private Integer actorId;

    private Integer actorTypeId;

    public PatientTemplateDTO() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public PatientTemplateDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public List<TemplateFieldValueDTO> getTemplateFieldValues() {
        return templateFieldValues;
    }

    public PatientTemplateDTO setTemplateFieldValues(List<TemplateFieldValueDTO> templateFieldValues) {
        this.templateFieldValues = templateFieldValues;
        return this;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public PatientTemplateDTO setPatientId(Integer patientId) {
        this.patientId = patientId;
        return this;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public PatientTemplateDTO setTemplateId(Integer templateId) {
        this.templateId = templateId;
        return this;
    }

    public Integer getActorId() {
        return actorId;
    }

    public PatientTemplateDTO setActorId(Integer actorId) {
        this.actorId = actorId;
        return this;
    }

    public Integer getActorTypeId() {
        return actorTypeId;
    }

    public PatientTemplateDTO setActorTypeId(Integer actorTypeId) {
        this.actorTypeId = actorTypeId;
        return this;
    }
}
