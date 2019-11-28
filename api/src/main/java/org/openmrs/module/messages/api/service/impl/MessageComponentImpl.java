/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service.impl;

import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.api.impl.ConceptServiceImpl;
import org.openmrs.module.messages.api.dao.MessagingDao;
import org.openmrs.module.messages.api.model.ActorResponse;
import org.openmrs.module.messages.api.service.MessageComponent;

import java.util.Date;

// ToDo: remove SuppressWarnings after implementation https://sd-cfl.atlassian.net/browse/CFLM-190
@SuppressWarnings({ "PMD.UnusedPrivateField" })
public class MessageComponentImpl extends BaseOpenmrsService implements MessageComponent {

    private MessagingDao msgDao;

    private ConceptServiceImpl conceptService;

    public void setMsgDao(MessagingDao msgDao) {
        this.msgDao = msgDao;
    }

    @Override
    public ActorResponse registerResponse(Integer scheduledId,
                                          Integer questionId,
                                          Integer response,
                                          String textResponse,
                                          Date timestamp) {
        return null;
    }
}
