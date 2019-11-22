package org.openmrs.module.messages.api.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.openmrs.BaseOpenmrsData;

public abstract class AbstractBaseOpenmrsData extends BaseOpenmrsData {
    
    private static final String ID_FIELD_NAME = "id";
    private static final long serialVersionUID = 8907890539048617187L;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return EqualsBuilder.reflectionEquals(this, o, ID_FIELD_NAME);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, ID_FIELD_NAME);
    }
}
