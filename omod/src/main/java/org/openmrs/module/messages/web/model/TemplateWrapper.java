package org.openmrs.module.messages.web.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.openmrs.module.messages.api.dto.TemplateDTO;

import java.util.List;

public class TemplateWrapper {

    private List<TemplateDTO> templates;

    public TemplateWrapper() { }

    public TemplateWrapper(List<TemplateDTO> templates) {
        this.templates = templates;
    }

    public List<TemplateDTO> getTemplates() {
        return templates;
    }

    public TemplateWrapper setTemplates(List<TemplateDTO> templates) {
        this.templates = templates;
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
