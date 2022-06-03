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

import org.openmrs.OpenmrsObject;
import org.openmrs.Relationship;
import org.openmrs.api.PersonService;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Message;
import java.util.Collections;
import java.util.List;

/**
 * Abstract class for subscribable event listening.
 */
public abstract class RelationshipActionListener extends BaseActionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(RelationshipActionListener.class);

    private PersonService personService;

    /**
     * @see BaseActionListener#subscribeToObjects()
     */
    @Override
    public List<Class<? extends OpenmrsObject>> subscribeToObjects() {
        return Collections.singletonList(Relationship.class);
    }

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    public PersonService getPersonService() {
        return personService;
    }

    /**
     * Extracts the relationship from the received messages.
     * Fetch the {@link org.openmrs.Relationship} object from DB based on UUID from message properties.
     * @param message message with properties.
     * @return retrieved relationship
     */
    protected Relationship extractRelationship(Message message) {
        LOGGER.trace("Handle extractPerson");
        String relationshipUuid = getMessagePropertyValue(message, MessagesConstants.UUID_KEY);
        return getRelationship(relationshipUuid);
    }

    private Relationship getRelationship(String relationshipUuid) {
        LOGGER.debug("Handle getRelationship for {} uuid", relationshipUuid);
        Relationship relationship = personService.getRelationshipByUuid(relationshipUuid);
        if (relationship == null) {
            throw new MessagesRuntimeException(
                    String.format("Unable to retrieve relationship by uuid: %s", relationshipUuid));
        }
        return relationship;
    }
}
