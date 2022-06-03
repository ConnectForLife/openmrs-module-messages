/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.builder;

import org.openmrs.Person;
import org.openmrs.PersonName;

public final class PersonBuilder extends AbstractBuilder<Person> {

    private Integer id;

    private PersonName name;

    private String gender;

    public PersonBuilder() {
        super();
        id = getInstanceNumber();
        name = new PersonNameBuilder().build();
        gender = "M";
    }

    @Override
    public Person build() {
        Person person = new Person();
        person.setId(id);
        person.addName(name);
        person.setGender(gender);
        return person;
    }

    @Override
    public Person buildAsNew() {
        return withId(null).withName(new PersonNameBuilder().buildAsNew()).build();
    }

    public PersonBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public PersonBuilder withName(PersonName name) {
        this.name = name;
        return this;
    }

    public PersonBuilder withGender(String gender) {
        this.gender = gender;
        return this;
    }
}
