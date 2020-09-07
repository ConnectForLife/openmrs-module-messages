package org.openmrs.module.messages.builder;

import org.openmrs.module.messages.api.dto.TemplateFieldDTO;
import org.openmrs.module.messages.api.model.TemplateField;

import java.util.Arrays;
import java.util.List;

public final class TemplateFieldDTOBuilder extends AbstractBuilder<TemplateFieldDTO> {

    private Integer id;

    private String name;

    private Boolean mandatory;

    private String defaultValue;

    private String type;

    private String uuid;

    private List<String> possibleValues;

    public TemplateFieldDTOBuilder() {
        super();
        TemplateField temmplateField = new TemplateFieldBuilder().build();
        id = temmplateField.getId();
        name = temmplateField.getName();
        mandatory = temmplateField.getMandatory();
        defaultValue = temmplateField.getDefaultValue();
        type = temmplateField.getTemplateFieldType().name();
        uuid = temmplateField.getUuid();
        possibleValues = Arrays.asList("Deactivate service", "SMS", "Call");
    }

    @Override
    public TemplateFieldDTO build() {
        return new TemplateFieldDTO()
                .setId(id)
                .setName(name)
                .setMandatory(mandatory)
                .setDefaultValue(defaultValue)
                .setType(type)
                .setUuid(uuid)
                .setPossibleValues(possibleValues);
    }

    @Override
    public TemplateFieldDTO buildAsNew() {
        return withId(null).withUuid(null).build();
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

    public TemplateFieldDTOBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public TemplateFieldDTOBuilder withUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }
}
