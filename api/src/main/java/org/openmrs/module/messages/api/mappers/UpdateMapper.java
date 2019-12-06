package org.openmrs.module.messages.api.mappers;

import org.openmrs.BaseOpenmrsData;

public interface UpdateMapper<T extends BaseOpenmrsData> {
    T update(T existingObject, T newObject);
}
