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
            LOGGER.debug(String.format("Voiding patient templates for %s person", person.toString()));
            Integer personId = person.getPersonId();
            patientTemplateService.voidForPerson(personId, person.getVoidReason());
        }
    }

    public void setPatientTemplateService(PatientTemplateService patientTemplateService) {
        this.patientTemplateService = patientTemplateService;
    }
}
