package org.openmrs.module.messages.builder;

import org.openmrs.module.messages.api.dto.TemplateFieldDTO;
import org.openmrs.module.messages.api.model.TemplateField;

public final class TemplateFieldDTOBuilder extends AbstractBuilder<TemplateFieldDTO> {

    private Integer id;

    private String name;

    private Boolean mandatory;

    private String defaultValue;

    private String templateFieldType;

    private String uuid;

    public TemplateFieldDTOBuilder() {
        super();
        TemplateField temmplateField = new TemplateFieldBuilder().build();
        id = temmplateField.getId();
        name = temmplateField.getName();
        mandatory = temmplateField.getMandatory();
        defaultValue = temmplateField.getDefaultValue();
        templateFieldType = temmplateField.getTemplateFieldType().name();
        uuid = temmplateField.getUuid();
    }

    @Override
    public TemplateFieldDTO build() {
        return new TemplateFieldDTO()
                .setId(id)
                .setName(name)
                .setMandatory(mandatory)
                .setDefaultValue(defaultValue)
                .setTemplateFieldType(templateFieldType)
                .setUuid(uuid);
    }

    @Override
    public TemplateFieldDTO buildAsNew() {
        return withId(null).build();
    }

    public TemplateFieldDTOBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public TemplateFieldDTOBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TemplateFieldDTOBuilder withMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
        return this;
    }

    public TemplateFieldDTOBuilder withDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public TemplateFieldDTOBuilder withTemplateFieldType(String templateFieldType) {
        this.templateFieldType = templateFieldType;
        return this;
    }
}
