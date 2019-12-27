package org.openmrs.module.messages.api.execution;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ServiceResultGroupHelper {

    public static List<GroupedServiceResultList> groupByActorAndExecutionDate(List<ServiceResultList> input) {
        List<ActorServiceResultList> groupedByActor = groupByActor(input);
        List<GroupedServiceResultList> resultGroups = groupByExecutionDate(groupedByActor);
        return connectTheSameGroups(resultGroups);
    }

    private static List<GroupedServiceResultList> connectTheSameGroups(List<GroupedServiceResultList> input) {
        Map<ActorWithDate, GroupedServiceResultList> groupsMap = new HashMap<>();

        for (GroupedServiceResultList group : input) {
            ActorWithDate actorWithDate = new ActorWithDate(
                    group.getActorWithExecutionDate().getActorId(),
                    group.getActorWithExecutionDate().getDate());
            if (groupsMap.containsKey(actorWithDate)) {
                groupsMap.get(actorWithDate).getGroup().getResults().addAll(group.getGroup().getResults());
            } else {
                groupsMap.put(actorWithDate, group);
            }
        }

        return new ArrayList<>(groupsMap.values());
    }

    private static List<GroupedServiceResultList> groupByExecutionDate(List<ActorServiceResultList> input) {
        List<GroupedServiceResultList> result = new ArrayList<>();

        for (ActorServiceResultList actorGroup : input) {
            result.addAll(groupByExecutionDate(actorGroup));
        }

        return result;
    }

    private static List<GroupedServiceResultList> groupByExecutionDate(ActorServiceResultList input) {
        List<GroupedServiceResultList> result = new ArrayList<>();

        for (ServiceResultList group : input.getGroups()) {
            result.addAll(groupByExecutionDate(group));
        }

        return result;
    }

    private static List<GroupedServiceResultList> groupByExecutionDate(ServiceResultList input) {
        Map<Date, ServiceResultList> resultListMap = new HashMap<>();

        for (ServiceResult serviceResult : input.getResults()) {
            Date date = serviceResult.getExecutionDate();
            if (resultListMap.containsKey(date)) {
                resultListMap.get(date).getResults().add(serviceResult);
            } else {
                resultListMap.put(date, ServiceResultList.withEmptyResults(input));
                resultListMap.get(date).getResults().add(serviceResult);
            }
        }

        return GroupedServiceResultList.fromServiceResultLists(resultListMap.values());
    }

    private static List<ActorServiceResultList> groupByActor(List<ServiceResultList> input) {
        Map<Integer, ActorServiceResultList> actorGroupsMap = new HashMap<>();

        for (ServiceResultList list : input) {
            Integer id = list.getActorId();
            if (actorGroupsMap.containsKey(id)) {
                actorGroupsMap.get(id).addServiceResultList(list);
            } else {
                actorGroupsMap.put(id, new ActorServiceResultList(list));
            }
        }

        return new ArrayList<>(actorGroupsMap.values());
    }

    private ServiceResultGroupHelper() {
    }
}
