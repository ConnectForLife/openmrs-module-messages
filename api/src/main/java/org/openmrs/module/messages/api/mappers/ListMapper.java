package org.openmrs.module.messages.api.mappers;

import org.openmrs.BaseOpenmrsData;

import java.util.List;

public interface ListMapper<T, R extends BaseOpenmrsData> {

    T toDto(List<R> daos);

    List<R> fromDto(T dao);
}
