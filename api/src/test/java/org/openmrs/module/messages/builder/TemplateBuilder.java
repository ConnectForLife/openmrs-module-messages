package org.openmrs.module.messages.builder;

import org.openmrs.module.messages.api.model.Template;

public final class TemplateBuilder extends AbstractBuilder<Template> {

    private Integer id;
    private String serviceQuery;
    private String serviceQueryType;

    public TemplateBuilder() {
        id = getInstanceNumber();
        serviceQuery = "SELECT * FROM template";
        serviceQueryType = "SQL";
    }

    @Override
    public Template build() {
        Template template = new Template();
        template.setId(id);
        template.setServiceQuery(serviceQuery);
        template.setServiceQueryType(serviceQueryType);
        return template;
    }

    public TemplateBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public TemplateBuilder withServiceQuery(String serviceQuery) {
        this.serviceQuery = serviceQuery;
        return this;
    }

    public TemplateBuilder withServiceQueryType(String serviceQueryType) {
        this.serviceQueryType = serviceQueryType;
        return this;
    }
}
