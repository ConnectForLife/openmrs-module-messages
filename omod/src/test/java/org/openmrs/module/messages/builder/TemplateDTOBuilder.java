package org.openmrs.module.messages.builder;

import org.openmrs.module.messages.api.dto.TemplateDTO;
import org.openmrs.module.messages.api.dto.TemplateFieldDTO;
import org.openmrs.module.messages.api.model.Template;

import java.util.Collections;
import java.util.List;

public final class TemplateDTOBuilder extends AbstractBuilder<TemplateDTO> {

    private Integer id;

    private String name;

    private String serviceQuery;

    private String serviceQueryType;

    private List<TemplateFieldDTO> templateFields;

    private String uuid;

    public TemplateDTOBuilder() {
        Template template = new TemplateBuilder().build();
        id = template.getId();
        name = template.getName();
        serviceQuery = template.getServiceQuery();
        serviceQueryType = template.getServiceQueryType();
        templateFields = Collections.singletonList(new TemplateFieldDTOBuilder().build());
        uuid = template.getUuid();
    }

    @Override
    public TemplateDTO build() {
        return new TemplateDTO()
                .setId(id)
                .setName(name)
                .setServiceQuery(serviceQuery)
                .setServiceQueryType(serviceQueryType)
                .setTemplateFields(templateFields)
                .setUuid(uuid);
    }

    @Override
    public TemplateDTO buildAsNew() {
        return withId(null).withUuid(null).build();
    }

    public TemplateDTOBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public TemplateDTOBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TemplateDTOBuilder withServiceQuery(String serviceQuery) {
        this.serviceQuery = serviceQuery;
        return this;
    }

    public TemplateDTOBuilder withServiceQueryType(String serviceQueryType) {
        this.serviceQueryType = serviceQueryType;
        return this;
    }

    public TemplateDTOBuilder withTemplateFields(List<TemplateFieldDTO> templateFields) {
        this.templateFields = templateFields;
        return this;
    }

    public TemplateDTOBuilder withUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }
}
