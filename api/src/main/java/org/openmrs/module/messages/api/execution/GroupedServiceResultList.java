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
    public Integer getId() {
        // This DTO has no ID
        // Skipped throwing exception because of workaround for issues with JSON serialization in OMRS 2.4 and later
        return null;
    }

    public GroupedServiceResultListKey getKey() {
        return key;
    }

    public List<GroupedServiceResult> getGroup() {
        return group;
    }
}
