/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.builder;

import org.openmrs.PersonName;

public final class PersonNameBuilder extends AbstractBuilder< PersonName> {

    private Integer id;

    private String given;

    private String family;

    public PersonNameBuilder() {
        super();
        id = getInstanceNumber();
        given = "John";
        family = "Smith";
    }

    @Override
    public  PersonName build() {
        PersonName personName = new PersonName();
        personName.setId(id);
        personName.setGivenName(given);
        personName.setFamilyName(family);
        return personName;
    }

    @Override
    public PersonName buildAsNew() {
        return withId(null).build();
    }

    public PersonNameBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public PersonNameBuilder withGiven(String given) {
        this.given = given;
        return this;
    }

    public PersonNameBuilder withFamily(String family) {
        this.family = family;
        return this;
    }
}
