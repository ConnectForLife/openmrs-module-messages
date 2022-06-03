/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.dto;

import org.apache.commons.lang.NotImplementedException;

public interface DTO {
    /**
     * Necessary information to support CRUD operations.
     * It is not required to implement this functionality when DTO will be used only
     * in one-way communication - from the backend to the frontend.
     *
     * @return id of existing entity or null if it is new
     * @throws NotImplementedException when the DTO is used in CRUD operations
     */
    Integer getId();
}
