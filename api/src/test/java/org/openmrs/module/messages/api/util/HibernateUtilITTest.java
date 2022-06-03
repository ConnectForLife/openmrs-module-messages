/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.openmrs.module.messages.Constant.NOT_EXISTING_ID;
import static org.openmrs.module.messages.api.service.DatasetConstants.DELIVERED_SCHEDULED_SERVICE;
import static org.openmrs.module.messages.api.service.DatasetConstants.XML_DATA_SET_PATH;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.messages.ContextSensitiveTest;
import org.openmrs.module.messages.api.exception.EntityNotFoundException;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.service.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class HibernateUtilITTest extends ContextSensitiveTest {

    @Autowired
    @Qualifier("messages.messagingService")
    private MessagingService messagingService;

    @Before
    public void setUp() throws Exception {
        executeDataSet(XML_DATA_SET_PATH + "MessageDataSet.xml");
    }

    @Test
    public void getNotNullShouldGetEntityIfEntityExists() {
        ScheduledService scheduledService = HibernateUtil.getNotNull(DELIVERED_SCHEDULED_SERVICE, messagingService);

        assertNotNull(scheduledService);
        assertEquals(new Integer(DELIVERED_SCHEDULED_SERVICE), scheduledService.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void getNotNullShouldThrowExceptionIfEntityNotExists() {
        HibernateUtil.getNotNull(NOT_EXISTING_ID, messagingService);
    }
}
