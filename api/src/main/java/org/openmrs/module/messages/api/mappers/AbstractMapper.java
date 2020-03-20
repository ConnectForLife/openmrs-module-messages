package org.openmrs.module.messages.api.mappers;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.module.messages.api.dto.DTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class AbstractMapper<T extends DTO, R extends BaseOpenmrsData>
        implements Mapper<T, R>, UpdateMapper<T, R> {

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

    public void updateFromDtos(List<T> sources, List<R> targets) {
        updateExisting(sources, targets);
        addNew(sources, targets);
    }

    private void updateExisting(List<T> sources, List<R> targets) {
        Iterator<R> targetIterator = targets.iterator();
        Map<Integer, T> sourcesByIds = mapDtoById(sources);
        while (targetIterator.hasNext()) {
            R target = targetIterator.next();
            T source = sourcesByIds.get(target.getId());
            if (source == null) {
                target.setVoided(true);
            } else {
                updateFromDto(source, target);
            }
        }
    }

    private void addNew(List<T> sources, List<R> targets) {
        for (T source : sources) {
            if (source.getId() == null) {
                targets.add(fromDto(source));
            }
        }
    }

    private Map<Integer, T> mapDtoById(List<T> dtos) {
        HashMap<Integer, T> map = new HashMap<>();
        for (T dto : dtos) {
            map.put(dto.getId(), dto);
        }
        return map;
    }
}
