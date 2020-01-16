/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.web.model;

import java.util.List;

public class ListWrapper<T> {

    private List<T> records;

    public ListWrapper() {
    }

    public ListWrapper(List<T> records) {
        this.records = records;
    }

    public List<T> getRecords() {
        return records;
    }

    public ListWrapper setRecords(List<T> records) {
        this.records = records;
        return this;
    }
}
