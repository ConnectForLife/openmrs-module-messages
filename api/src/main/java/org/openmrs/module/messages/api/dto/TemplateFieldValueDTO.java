package org.openmrs.module.messages.api.dto;

public class TemplateFieldValueDTO {

    private Integer id;

    private Integer templateFieldId;

    private String value;

    public TemplateFieldValueDTO() {
    }

    public Integer getId() {
        return id;
    }

    public TemplateFieldValueDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getTemplateFieldId() {
        return templateFieldId;
    }

    public TemplateFieldValueDTO setTemplateFieldId(Integer templateFieldId) {
        this.templateFieldId = templateFieldId;
        return this;
    }

    public String getValue() {
        return value;
    }

    public TemplateFieldValueDTO setValue(String value) {
        this.value = value;
        return this;
    }
}
