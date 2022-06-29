/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages;

import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

public final class ApiConstant {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType("application", "json",
            StandardCharsets.UTF_8);

    public static final String ROWS_PARAM = "rows";

    public static final String PAGE_PARAM = "page";

    public static final String CAREGIVER_RELATIONSHIP = "Caregiver";

    private ApiConstant() { }
}
