package org.openmrs.module.messages.api.helper;

import org.openmrs.Person;
import org.openmrs.PersonName;
import org.openmrs.api.context.Context;

public final class PersonHelper {
    
    private PersonHelper() {
    }
    
    public static Person createTestInstance() {
        Person person = new Person();
        person.setGender("M");
        person.setBirthdateEstimated(true);
        person.setDead(false);
        person.setDeathdateEstimated(false);
        
        PersonName name = new PersonName();
        name.setGivenName("test name");
        name.setFamilyName("test family");
        person.addName(name);
        
        return Context.getPersonService().savePerson(person);
    }
}
