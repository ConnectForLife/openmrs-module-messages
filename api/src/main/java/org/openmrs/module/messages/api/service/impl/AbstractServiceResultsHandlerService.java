package org.openmrs.module.messages.api.service.impl;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.api.PersonService;
import org.openmrs.module.messages.api.event.MessagesEvent;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;
import org.openmrs.module.messages.api.service.MessagesEventService;
import org.openmrs.module.messages.api.service.ServiceResultsHandlerService;

public abstract class AbstractServiceResultsHandlerService implements ServiceResultsHandlerService {

    private static final String PERSON_PHONE_ATTR = "Telephone Number";

    private PersonService personService;

    private MessagesEventService messagesEventService;

    public void setMessagesEventService(MessagesEventService messagesEventService) {
        this.messagesEventService = messagesEventService;
    }

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    protected void sendEventMessage(MessagesEvent messagesEvent) {
        messagesEventService.sendEventMessage(messagesEvent);
    }

    protected String getPersonPhone(Integer personId) {
        Person person = personService.getPerson(personId);
        if (person == null) {
            throw new MessagesRuntimeException(String.format("Person with id %s does not exist", personId));
        }
        PersonAttribute attribute = person.getAttribute(PERSON_PHONE_ATTR);
        if (attribute == null || StringUtils.isBlank(attribute.getValue())) {
            throw new MessagesRuntimeException(String.format("Phone number not specified for " +
                "person %s", person.getId()));
        }
        return attribute.getValue();
    }
}
