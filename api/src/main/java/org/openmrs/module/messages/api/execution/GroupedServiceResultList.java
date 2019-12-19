package org.openmrs.module.messages.api.execution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class GroupedServiceResultList {

    private Integer actorId;

    private Date executionDate;

    private ServiceResultList group;

    public GroupedServiceResultList(Integer actorId, Date executionDate, ServiceResultList group) {
        this.actorId = actorId;
        this.executionDate = executionDate;
        this.group = group;
    }

    public static List<GroupedServiceResultList> fromServiceResultLists(Collection<ServiceResultList> input) {
        List<GroupedServiceResultList> result = new ArrayList<>();

        for (ServiceResultList list : input) {
            if (!list.getResults().isEmpty()) {
                Date date = list.getResults().get(0).getExecutionDate();
                result.add(new GroupedServiceResultList(list.getActorId(), date, list));
            }
        }

        return result;
    }

    public ServiceResultList getGroup() {
        return group;
    }

    public Integer getActorId() {
        return actorId;
    }

    public Date getExecutionDate() {
        return executionDate;
    }
}
