package org.openmrs.module.messages.api.mappers;

import org.openmrs.BaseOpenmrsObject;

import java.util.List;

public interface Mapper<T, R extends BaseOpenmrsObject> {

    T toDto(R dao);

    R fromDto(T dto);

    List<T> toDtos(Iterable<R> daos);

    List<R> fromDtos(List<T> dtos);
}
