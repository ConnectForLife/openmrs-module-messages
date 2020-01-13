/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.event.listener;

import org.apache.activemq.command.ActiveMQBlobMessage;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.LocationService;
import org.openmrs.api.PatientService;
import org.openmrs.api.PersonService;
import org.openmrs.module.messages.Constant;
import org.openmrs.module.messages.ContextSensitiveWithActivatorTest;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;
import org.openmrs.module.messages.api.model.PersonStatus;
import org.openmrs.module.messages.api.util.ConfigConstants;
import org.openmrs.module.messages.builder.PatientBuilder;
import org.openmrs.module.messages.builder.PatientIdentifierBuilder;
import org.openmrs.module.messages.builder.PersonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Iterator;
import javax.jms.JMSException;
import javax.jms.Message;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class InitialPersonAttributeListenerITTest extends ContextSensitiveWithActivatorTest {

    private static final int EXPECTED_SIZE = 1;

    private static final int NON_UNIQUE_ID_TYPE_ID = 2;

    private static final int DEFAULT_LOACTION_ID = 1;

    private static final String DISABLED_CONTROL_VALUE = "false";

    @Autowired
    private PersonService personService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private LocationService locationService;

    @Autowired
    @Qualifier("adminService")
    private AdministrationService administrationService;

    @Autowired
    @Qualifier("messages.personCreatedListener")
    private InitialPersonAttributeListener listener;

    @Test
    public void shouldAddStatusNoConsentAfterCreatingPerson() throws JMSException {
        Person person = new PersonBuilder().buildAsNew();

        personService.savePerson(person);
        mockFireEvent(buildMessage(person));
        person = personService.getPersonByUuid(person.getUuid());

        assertThat(person.getAttributes().size(), is(EXPECTED_SIZE));
        PersonAttribute attribute = getFirstAttribute(person);
        assertThat(attribute, hasProperty(Constant.UUID_KEY, notNullValue()));
        assertThat(attribute, hasProperty(Constant.VALUE_KEY, is(PersonStatus.NO_CONSENT.name())));
    }

    @Test
    public void shouldAddStatusNoConsentAfterCreatingPatient() throws JMSException {
        Patient patient = preparePatient();

        patientService.savePatient(patient);
        mockFireEvent(buildMessage(patient));
        patient = patientService.getPatientByUuid(patient.getUuid());

        assertThat(patient.getAttributes().size(), is(EXPECTED_SIZE));
        PersonAttribute attribute = getFirstAttribute(patient);
        assertThat(attribute, hasProperty(Constant.UUID_KEY, notNullValue()));
        assertThat(attribute, hasProperty(Constant.VALUE_KEY, is(PersonStatus.NO_CONSENT.name())));
    }

    @Test
    public void shouldAddStatusDeactivateAfterCreatingPerson() throws JMSException {
        disableConsentControl();
        Person person = new PersonBuilder().buildAsNew();

        personService.savePerson(person);
        mockFireEvent(buildMessage(person));
        person = personService.getPersonByUuid(person.getUuid());

        assertThat(person.getAttributes().size(), is(EXPECTED_SIZE));
        PersonAttribute attribute = getFirstAttribute(person);
        assertThat(attribute, hasProperty(Constant.UUID_KEY, notNullValue()));
        assertThat(attribute, hasProperty(Constant.VALUE_KEY, is(PersonStatus.DEACTIVATE.name())));
    }

    @Test
    public void shouldAddStatusDeactivateAfterCreatingPatient() throws JMSException {
        disableConsentControl();
        Patient patient = preparePatient();

        patientService.savePatient(patient);
        mockFireEvent(buildMessage(patient));
        patient = patientService.getPatientByUuid(patient.getUuid());

        assertThat(patient.getAttributes().size(), is(EXPECTED_SIZE));
        PersonAttribute attribute = getFirstAttribute(patient);
        assertThat(attribute, hasProperty(Constant.UUID_KEY, notNullValue()));
        assertThat(attribute, hasProperty(Constant.VALUE_KEY, is(PersonStatus.DEACTIVATE.name())));
    }

    @Test(expected = MessagesRuntimeException.class)
    public void shouldThrowExceptionWhenPersonDoesNotExist() throws JMSException {
        mockFireEvent(buildMessage(new Person()));
    }

    @Test(expected = MessagesRuntimeException.class)
    public void shouldThrowExceptionWhenPatientDoesNotExist() throws JMSException {
        mockFireEvent(buildMessage(new Patient()));
    }

    @Test(expected = MessagesRuntimeException.class)
    public void shouldThrowExceptionWhenNotValidMessage() {
        mockFireEvent(new ActiveMQBlobMessage());
    }

    private void mockFireEvent(Message message) {
        listener.performAction(message);
    }

    private Message buildMessage(Person person) throws JMSException {
        ActiveMQMapMessage message = new ActiveMQMapMessage();
        message.setString(Constant.UUID_KEY, person.getUuid());
        return message;
    }

    private PersonAttribute getFirstAttribute(Person person) {
        Iterator<PersonAttribute> iterator = person.getAttributes().iterator();
        return iterator.next();
    }

    private Patient preparePatient() {
        Person person = new PersonBuilder().buildAsNew();
        PatientIdentifier identifier = new PatientIdentifierBuilder()
                .withLocation(locationService.getLocation(DEFAULT_LOACTION_ID))
                .withIdentifierType(patientService.getPatientIdentifierType(NON_UNIQUE_ID_TYPE_ID))
                .buildAsNew();
        return new PatientBuilder()
                .withPerson(person)
                .withIdentifier(identifier)
                .buildAsNew();
    }

    private void disableConsentControl() {
        administrationService.setGlobalProperty(ConfigConstants.CONSENT_CONTROL_KEY, DISABLED_CONTROL_VALUE);
    }
}
