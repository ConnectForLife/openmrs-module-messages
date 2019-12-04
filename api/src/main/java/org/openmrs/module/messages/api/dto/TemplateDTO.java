package org.openmrs.module.messages.api.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent the DTO for the {@link org.openmrs.module.messages.api.model.Template}
 */
public class TemplateDTO {

    private Integer id;

    private String serviceQuery;

    private String serviceQueryType;

    private List<TemplateFieldDTO> templateFields = new ArrayList<>();

    private String uuid;

    public Integer getId() {
        return id;
    }

    public TemplateDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getServiceQuery() {
        return serviceQuery;
    }

    public TemplateDTO setServiceQuery(String serviceQuery) {
        this.serviceQuery = serviceQuery;
        return this;
    }

    public String getServiceQueryType() {
        return serviceQueryType;
    }

    public TemplateDTO setServiceQueryType(String serviceQueryType) {
        this.serviceQueryType = serviceQueryType;
        return this;
    }

    public List<TemplateFieldDTO> getTemplateFields() {
        return templateFields;
    }

    public TemplateDTO setTemplateFields(List<TemplateFieldDTO> templateFields) {
        this.templateFields = templateFields;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public TemplateDTO setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
