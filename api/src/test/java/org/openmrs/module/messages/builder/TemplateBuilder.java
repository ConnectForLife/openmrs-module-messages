package org.openmrs.module.messages.builder;

import org.openmrs.User;
import org.openmrs.module.messages.api.model.ChannelType;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.model.TemplateFieldType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public final class TemplateBuilder extends AbstractBuilder<Template> {

    private static final int YEAR_2010 = 2010;
    private static final int DAY = 16;
    private Integer id;
    private String serviceQuery;
    private String serviceQueryType;
    private String name;
    private User creator;
    private Date dateCreated;
    private List<TemplateField> templateFields;

    public TemplateBuilder() {
        id = getInstanceNumber();
        serviceQuery = "SELECT * FROM template";
        serviceQueryType = "SQL";
        name = "Example service";
        creator = new User();
        dateCreated = new Date(YEAR_2010, Calendar.NOVEMBER, DAY);
        templateFields = new ArrayList<>();
    }

    @Override
    public Template build() {
        Template template = new Template();
        if (templateFields.isEmpty()) {
            buildTemplateFields(template);
        }
        template.setId(id);
        template.setServiceQuery(serviceQuery);
        template.setServiceQueryType(serviceQueryType);
        template.setName(name);
        template.setCreator(creator);
        template.setDateCreated(dateCreated);
        template.setTemplateFields(templateFields);
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

    public TemplateBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TemplateBuilder withTemplateFields(List<TemplateField> templateFields) {
        this.templateFields = templateFields;
        return this;
    }

    public TemplateBuilder withTemplateField(TemplateField templateField) {
        this.templateFields = Collections.singletonList(templateField);
        return this;
    }

    public TemplateBuilder withDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    private void buildTemplateFields(Template template) {
        templateFields = Arrays.asList(buildServiceTypeFieldDef(template),
                buildDayOfWeekFieldDef(template),
                buildStartOfMessagesFieldDef(template),
                buildEndOfMessagesFieldDef(template));
    }

    private TemplateField buildServiceTypeFieldDef(Template template) {
        TemplateField serviceType = new TemplateFieldBuilder()
                .withName("Service type")
                .withMandatory(true)
                .withDefaultValue(ChannelType.SMS.getName())
                .withTemplate(template)
                .withTemplateFieldType(TemplateFieldType.SERVICE_TYPE)
                .build();
        serviceType.setDefaultValues(Collections.singletonList(new TemplateFieldDefaultValueBuilder()
                .withTemplateField(serviceType)
                .withDefaultValue(ChannelType.DEACTIVATED.getName())
                .build()));
        return serviceType;
    }

    private TemplateField buildDayOfWeekFieldDef(Template template) {
        TemplateField serviceType = new TemplateFieldBuilder()
                .withName("Week day of delivering message")
                .withMandatory(true)
                .withDefaultValue("Monday,Tuesday,Wednesday,Sunday")
                .withTemplate(template)
                .withTemplateFieldType(TemplateFieldType.DAY_OF_WEEK)
                .build();
        serviceType.setDefaultValues(Collections.singletonList(new TemplateFieldDefaultValueBuilder()
                .withTemplateField(serviceType)
                .withDefaultValue("Monday,Friday,Saturday")
                .build()));
        return serviceType;
    }

    private TemplateField buildStartOfMessagesFieldDef(Template template) {
        TemplateField serviceType = new TemplateFieldBuilder()
                .withName("Start of daily messages")
                .withMandatory(false)
                .withDefaultValue("")
                .withTemplate(template)
                .withTemplateFieldType(TemplateFieldType.START_OF_MESSAGES)
                .build();
        serviceType.setDefaultValues(Collections.singletonList(new TemplateFieldDefaultValueBuilder()
                .withTemplateField(serviceType)
                .withDefaultValue("")
                .build()));
        return serviceType;
    }

    private TemplateField buildEndOfMessagesFieldDef(Template template) {
        TemplateField serviceType = new TemplateFieldBuilder()
                .withName("End of daily messages")
                .withMandatory(false)
                .withDefaultValue("NO_DATE|EMPTY")
                .withTemplate(template)
                .withTemplateFieldType(TemplateFieldType.END_OF_MESSAGES)
                .build();
        serviceType.setDefaultValues(Collections.singletonList(new TemplateFieldDefaultValueBuilder()
                .withTemplateField(serviceType)
                .withDefaultValue("AFTER_TIMES|1")
                .build()));
        return serviceType;
    }
}
