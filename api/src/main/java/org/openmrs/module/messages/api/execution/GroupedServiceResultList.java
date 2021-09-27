package org.openmrs.module.messages.api.execution;

import org.apache.commons.lang.NotImplementedException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.openmrs.module.messages.api.dto.DTO;

import java.util.List;

public class GroupedServiceResultList implements DTO {

    private final GroupedServiceResultListKey key;
    private final List<GroupedServiceResult> group;

    public GroupedServiceResultList(GroupedServiceResultListKey key, List<GroupedServiceResult> group) {
        this.key = key;
        this.group = group;
    }

    @Override
    @JsonIgnore
    public Integer getId() {
        throw new NotImplementedException("not implemented yet");
    }

    public GroupedServiceResultListKey getKey() {
        return key;
    }

    public List<GroupedServiceResult> getGroup() {
        return group;
    }
}
