package org.openmrs.module.messages.api.mappers;

import org.openmrs.BaseOpenmrsObject;

public interface UpdateMapper<T, R extends BaseOpenmrsObject> {
    void updateFromDto(T source, R target);
}
