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

import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.test.context.ContextConfiguration;

// The order of context configuration classes is different than in BaseModuleContextSensitiveTest,
// so that we don't have to override every bean for test, but only those we need to change
@ContextConfiguration(
        locations = {"classpath:applicationContext-service.xml", "classpath*:moduleApplicationContext.xml",
                "classpath*:TestingApplicationContext.xml"}, inheritLocations = false)
public abstract class ContextSensitiveTest extends BaseModuleContextSensitiveTest {
}
