/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.model;

public enum ErrorMessageEnum {

    ERR_SYSTEM("system.error"),
    ERR_BAD_PARAM("system.param"),
    ERR_ENTITY_NOT_FOUND("system.entity_not_found");

    private String code;

    ErrorMessageEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
