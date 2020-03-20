package org.openmrs.module.messages.api.mappers;

import org.openmrs.BaseOpenmrsData;

public interface UpdateMapper<T, R extends BaseOpenmrsData> {
    void updateFromDto(T source, R target);
}
