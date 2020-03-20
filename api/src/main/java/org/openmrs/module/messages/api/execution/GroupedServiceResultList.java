package org.openmrs.module.messages.api.execution;

import org.apache.commons.lang.NotImplementedException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.openmrs.module.messages.api.dto.DTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class GroupedServiceResultList implements DTO {

    private ActorWithDate actorWithExecutionDate;

    private ServiceResultList group;

    public GroupedServiceResultList(ActorWithDate actorWithExecutionDate, ServiceResultList group) {
        this.actorWithExecutionDate = actorWithExecutionDate;
        this.group = group;
    }

    @Override
    @JsonIgnore
    public Integer getId() {
        throw new NotImplementedException("not implemented yet");
    }

    public static List<GroupedServiceResultList> fromServiceResultLists(Collection<ServiceResultList> input) {
        List<GroupedServiceResultList> result = new ArrayList<>();

        for (ServiceResultList list : input) {
            if (!list.getResults().isEmpty()) {
                Date date = list.getResults().get(0).getExecutionDate();
                result.add(new GroupedServiceResultList(new ActorWithDate(
                        list.getActorId(),
                        list.getActorType(),
                        date), list));
            }
        }

        return result;
    }

    public ServiceResultList getGroup() {
        return group;
    }

    public ActorWithDate getActorWithExecutionDate() {
        return actorWithExecutionDate;
    }
}
