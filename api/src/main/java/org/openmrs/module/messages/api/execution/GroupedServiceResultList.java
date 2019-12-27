package org.openmrs.module.messages.api.execution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class GroupedServiceResultList {

    private ActorWithDate actorWithExecutionDate;

    private ServiceResultList group;

    public GroupedServiceResultList(ActorWithDate actorWithExecutionDate, ServiceResultList group) {
        this.actorWithExecutionDate = actorWithExecutionDate;
        this.group = group;
    }

    public static List<GroupedServiceResultList> fromServiceResultLists(Collection<ServiceResultList> input) {
        List<GroupedServiceResultList> result = new ArrayList<>();

        for (ServiceResultList list : input) {
            if (!list.getResults().isEmpty()) {
                Date date = list.getResults().get(0).getExecutionDate();
                result.add(new GroupedServiceResultList(new ActorWithDate(list.getActorId(), date), list));
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
