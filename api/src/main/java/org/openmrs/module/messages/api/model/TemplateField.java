package org.openmrs.module.messages.api.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "messages.TemplateField")
@Table(name = "messages_template_field")
public class TemplateField extends AbstractBaseOpenmrsData {
    
    private static final long serialVersionUID = -5617673897191245574L;
    
    @Id
    @GeneratedValue
    @Column(name = "messages_template_field_id")
    private Integer id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "mandatory", nullable = false)
    private Boolean mandatory;
    
    @Column(name = "default_value", columnDefinition = "text")
    private String defaultValue;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "templateField")
    private List<TemplateFieldDefaultValue> defaultValues = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "template_id", nullable = false)
    private Template template;

    @Column(name = "template_field_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TemplateFieldType templateFieldType;

    @Column(name = "possible_values")
    private String possibleValues;

    /**
     * default empty constructor
     */
    public TemplateField() {
    }

    /**
     * Default constructor taking in the primary key id value
     *
     * @param id Integer internal id for this template field
     * @should set person id
     */
    public TemplateField(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "TemplateField#" + id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public TemplateFieldType getTemplateFieldType() {
        return templateFieldType;
    }

    public void setTemplateFieldType(TemplateFieldType templateFieldType) {
        this.templateFieldType = templateFieldType;
    }

    public List<TemplateFieldDefaultValue> getDefaultValues() {
        return defaultValues;
    }

    public void setDefaultValues(List<TemplateFieldDefaultValue> defaultValues) {
        this.defaultValues = defaultValues;
    }

    public String getPossibleValues() {
        return this.possibleValues;
    }

    public void setPossibleValues(String possibleValues) {
        this.possibleValues = possibleValues;
    }

    @Transient
    public String getDefaultValueForSpecificActorOrGeneral(Actor actor) {
        String defaultActorValue = getDefaultValue(actor);
        if (defaultActorValue == null) {
            defaultActorValue = getDefaultValue();
        }
        return defaultActorValue;
    }

    @Transient
    public String getDefaultValue(Actor actor) {
        String defaultActorValue = null;
        for (TemplateFieldDefaultValue d : getDefaultValues()) {
            if (d.isRelatedTo(actor)) {
                defaultActorValue = d.getDefaultValue();
            }
        }
        return defaultActorValue;
    }
}
