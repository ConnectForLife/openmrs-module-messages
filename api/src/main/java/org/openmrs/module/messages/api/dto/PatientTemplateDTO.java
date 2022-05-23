package org.openmrs.module.messages.api.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a patient template DTO
 */
public class PatientTemplateDTO extends BaseDTO implements DTO {

    private static final long serialVersionUID = -2254698048130974286L;

    private Integer id;

    @Valid
    private List<TemplateFieldValueDTO> templateFieldValues = new ArrayList<TemplateFieldValueDTO>();

    @NotNull
    private Integer patientId;

    @NotNull
    private Integer templateId;

    @NotNull
    private Integer actorId;

    private Integer actorTypeId;

    private String uuid;

    @Override
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

    public String getUuid() {
        return uuid;
    }

    public PatientTemplateDTO withUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }
}
