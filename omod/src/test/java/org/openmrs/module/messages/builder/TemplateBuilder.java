package org.openmrs.module.messages.builder;

import org.openmrs.module.messages.api.model.Template;

public final class TemplateBuilder extends AbstractBuilder<Template> {

    private Integer id;
    private String serviceQuery;
    private String serviceQueryType;
    private String name;
    private String calendarServiceQuery;
    private boolean shouldUseOptimizedQuery;

    public TemplateBuilder() {
        id = getInstanceNumber();
        serviceQuery = "SELECT * FROM template";
        serviceQueryType = "SQL";
        name = "Example name";
        calendarServiceQuery = "SELECT CALENDAR SERVICE QUERY;";
        shouldUseOptimizedQuery = false;
    }

    @Override
    public Template build() {
        Template template = new Template();
        template.setId(id);
        template.setServiceQuery(serviceQuery);
        template.setServiceQueryType(serviceQueryType);
        template.setName(name);
        template.setCalendarServiceQuery(calendarServiceQuery);
        template.setShouldUseOptimizedQuery(shouldUseOptimizedQuery);
        return template;
    }

    @Override
    public Template buildAsNew() {
        return withId(null).build();
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

    public TemplateBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public TemplateBuilder withCalendarServiceQuery(String calendarServiceQuery) {
        this.calendarServiceQuery = calendarServiceQuery;
        return this;
    }

    public TemplateBuilder withShouldUseOptimizedQuery(boolean shouldUseOptimizedQuery) {
        this.shouldUseOptimizedQuery = shouldUseOptimizedQuery;
        return this;
    }
}
