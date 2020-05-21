package org.openmrs.module.messages.api.dto;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Represent the DTO for the {@link org.openmrs.module.messages.api.model.Template}
 */
public class TemplateDTO extends BaseDTO implements DTO {

    private Integer id;

    @NotBlank
    private String name;

    @NotBlank
    private String serviceQuery;

    @NotBlank
    private String serviceQueryType;

    @Valid
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

    public String getName() {
        return name;
    }

    public TemplateDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public TemplateDTO setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }
}
