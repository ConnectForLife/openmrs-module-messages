package org.openmrs.module.messages.api.model;

import org.junit.Test;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;

import static org.junit.Assert.assertTrue;

public class PersonStatusTest {

    @Test
    public void shouldVerifyIfPersonIsActive() {
        Person person = createPersonWithStatusAttribute();

        boolean actual = PersonStatus.isActive(person);

        assertTrue(actual);
    }

    private Person createPersonWithStatusAttribute() {
        Person person = new Person();
        person.addAttribute(createStatusAttribute());
        return person;
    }

    private PersonAttribute createStatusAttribute() {
        PersonAttributeType type = new PersonAttributeType();
        type.setName("Person status");

        PersonAttribute personAttribute = new PersonAttribute();
        personAttribute.setAttributeType(type);
        personAttribute.setValue("ACTIVATED");

        return personAttribute;
    }
}
