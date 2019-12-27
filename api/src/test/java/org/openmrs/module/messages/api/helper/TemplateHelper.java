/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.helper;

import org.openmrs.module.messages.api.model.Template;

public final class TemplateHelper {

    public static final String DUMMY_SERVICE_NAME = "example name of service";

    private TemplateHelper() {
    }
    
    public static Template createTestInstance() {
        Template template = new Template();
        template.setServiceQuery("example service query");
        template.setServiceQueryType("example service query type");
        template.setName(DUMMY_SERVICE_NAME);
        
        return template;
    }
}
