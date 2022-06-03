/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.event.listener.subscribable;

import org.openmrs.Relationship;
import org.openmrs.event.Event;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Message;
import java.util.Arrays;
import java.util.List;

public class RemovingRelationshipListener extends RelationshipActionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemovingRelationshipListener.class);

    private PatientTemplateService patientTemplateService;

    /**
     * @see RelationshipActionListener#subscribeToActions()
     */
    @Override
    public List<String> subscribeToActions() {
        return Arrays.asList(Event.Action.VOIDED.name(), Event.Action.PURGED.name(), Event.Action.RETIRED.name());
    }

    /**
     * @see RelationshipActionListener#performAction(Message)
     */
    @Override
    public void performAction(Message message) {
        Relationship relationship = extractRelationship(message);
        if (relationship != null && relationship.getVoided()) {
            LOGGER.debug("Voiding patient templates for {} relationship", relationship);
            Integer relationshipId = relationship.getRelationshipId();
            patientTemplateService.voidForRelationship(relationshipId, relationship.getVoidReason());
        }
    }

    public void setPatientTemplateService(PatientTemplateService patientTemplateService) {
        this.patientTemplateService = patientTemplateService;
    }
}
