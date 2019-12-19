package org.openmrs.module.messages.api.execution;

import java.util.ArrayList;
import java.util.List;

public class ActorServiceResultList {

    private List<ServiceResultList> groups = new ArrayList<>();

    public ActorServiceResultList(ServiceResultList serviceResultList) {
        this.groups.add(serviceResultList);
    }

    public void addServiceResultList(ServiceResultList serviceResultList) {
        this.groups.add(serviceResultList);
    }

    public List<ServiceResultList> getGroups() {
        return groups;
    }
}
