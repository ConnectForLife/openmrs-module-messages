package org.openmrs.module.messages.api.mappers;

import org.openmrs.BaseOpenmrsData;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMapper<T, R extends BaseOpenmrsData> implements Mapper<T, R> {

    public List<T> toDtos(List<R> daos) {
        List<T> dtos = new ArrayList<T>();
        for (R dao : daos) {
            dtos.add(toDto(dao));
        }
        return dtos;
    }

    public List<R> fromDtos(List<T> dtos) {
        List<R> daos = new ArrayList<R>();
        for (T dto : dtos) {
            daos.add(fromDto(dto));
        }
        return daos;
    }
}
