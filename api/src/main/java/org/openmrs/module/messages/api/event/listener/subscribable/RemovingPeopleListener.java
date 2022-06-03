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

import org.openmrs.Person;
import org.openmrs.event.Event;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Message;
import java.util.Arrays;
import java.util.List;

/**
 * The specific listener which voids the patient templates after people (Person / Patient) voiding / deletion.
 */
public class RemovingPeopleListener extends PeopleActionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemovingPeopleListener.class);

    private PatientTemplateService patientTemplateService;

    /**
     * @see PeopleActionListener#subscribeToActions()
     */
    @Override
    public List<String> subscribeToActions() {
        return Arrays.asList(Event.Action.VOIDED.name(), Event.Action.PURGED.name(), Event.Action.RETIRED.name());
    }

    /**
     * @see PeopleActionListener#performAction(Message)
     */
    @Override
    public void performAction(Message message) {
        Person person = extractPerson(message);
        if (person != null && person.getVoided()) {
            LOGGER.debug("Voiding patient templates for {} person", person);
            Integer personId = person.getPersonId();
            patientTemplateService.voidForPerson(personId, person.getVoidReason());
        }
    }

    public void setPatientTemplateService(PatientTemplateService patientTemplateService) {
        this.patientTemplateService = patientTemplateService;
    }
}
