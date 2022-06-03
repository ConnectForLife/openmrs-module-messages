/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.helper;

import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.module.messages.api.util.DateUtil;

public final class ScheduledServiceGroupHelper {
    
    private ScheduledServiceGroupHelper() {
    }
    
    public static ScheduledServiceGroup createTestInstance() {
        ScheduledServiceGroup scheduledServiceGroup = new ScheduledServiceGroup();
        scheduledServiceGroup.setMsgSendTime(DateUtil.toDate(DateUtil.now()));
        scheduledServiceGroup.setPatient(PatientHelper.createTestInstance());
        scheduledServiceGroup.setActor(PatientHelper.createTestInstance().getPerson());
        scheduledServiceGroup.setStatus(ServiceStatus.DELIVERED);
        scheduledServiceGroup.setChannelType("Call");
        
        return scheduledServiceGroup;
    }
    
}
