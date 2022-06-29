/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

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
