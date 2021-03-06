package org.openmrs.module.messages.api.dto;

import org.hibernate.validator.constraints.NotBlank;
import org.openmrs.module.messages.api.model.TemplateFieldType;
import org.openmrs.module.messages.api.util.validate.ValueOfEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent the DTO for the {@link org.openmrs.module.messages.api.model.TemplateField}
 */
public class TemplateFieldDTO extends BaseDTO implements DTO {

    private static final long serialVersionUID = -6117000433536115231L;
    private Integer id;

    @NotBlank
    private String name;

    private boolean mandatory;

    private String defaultValue;

    private List<TemplateFieldDefaultValueDTO> defaultValues = new ArrayList<>();

    private List<String> possibleValues;

    @NotBlank
    @ValueOfEnum(enumClass = TemplateFieldType.class)
    private String type;

    private String uuid;

    public Integer getId() {
        return id;
    }

    public TemplateFieldDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TemplateFieldDTO setName(String name) {
        this.name = name;
        return this;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public TemplateFieldDTO setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
        return this;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public TemplateFieldDTO setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public String getType() {
        return type;
    }

    public TemplateFieldDTO setType(String templateFieldType) {
        this.type = templateFieldType;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public TemplateFieldDTO setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public List<TemplateFieldDefaultValueDTO> getDefaultValues() {
        return defaultValues;
    }

    public TemplateFieldDTO setDefaultValues(List<TemplateFieldDefaultValueDTO> defaultValues) {
        this.defaultValues = defaultValues;
        return this;
    }

    public List<String> getPossibleValues() {
        return this.possibleValues;
    }

    public TemplateFieldDTO setPossibleValues(List<String> possibleValues) {
        this.possibleValues = possibleValues;
        return this;
    }
}
