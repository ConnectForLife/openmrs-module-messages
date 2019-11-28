/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.builder;

import org.openmrs.ConceptName;
import org.openmrs.api.ConceptNameType;

import java.util.Locale;

@SuppressWarnings({ "PMD.ConstructorCallsOverridableMethod" })
public class ConceptNameBuilder extends AbstractBuilder<ConceptName> {
    // This builder does not cover every ConceptName attribute.
    // Please, extend if needed.
    private Integer id;
    private String name;
    private Locale locale;
    private ConceptNameType conceptNameType;
    private Boolean isLocalePreferred;

    public ConceptNameBuilder() {
        this.id = 1;
        this.name = "History Of Present Illness";
        this.locale = Locale.ENGLISH;
        this.conceptNameType = ConceptNameType.FULLY_SPECIFIED;
        this.isLocalePreferred = true;
    }

    @Override
    public ConceptName build() {
        ConceptName result = new ConceptName();
        result.setConceptNameId(id);
        result.setName(name);
        result.setLocale(locale);
        result.setConceptNameType(conceptNameType);
        result.setLocalePreferred(isLocalePreferred);
        return result;
    }

    public ConceptNameBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public ConceptNameBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ConceptNameBuilder withLocale(Locale locale) {
        this.locale = locale;
        return this;
    }

    public ConceptNameBuilder withConceptNameType(ConceptNameType conceptNameType) {
        this.conceptNameType = conceptNameType;
        return this;
    }

    public ConceptNameBuilder withLocalePreferred(Boolean localePreferred) {
        isLocalePreferred = localePreferred;
        return this;
    }
}
