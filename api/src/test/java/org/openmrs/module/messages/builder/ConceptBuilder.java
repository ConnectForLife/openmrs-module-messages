/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.builder;

import org.openmrs.Concept;
import org.openmrs.ConceptName;
import org.openmrs.api.ConceptNameType;

import java.util.Collection;
import java.util.HashSet;

@SuppressWarnings({ "PMD.ConstructorCallsOverridableMethod" })
public class ConceptBuilder extends AbstractBuilder<Concept> {
    // This builder does not cover every ConceptName attribute.
    // Please, extend if needed.
    private Integer id;
    private Collection<ConceptName> names;

    public ConceptBuilder() {
        this.id = 1;
        this.names = new HashSet<ConceptName>();
        ConceptName shortName = new ConceptNameBuilder()
            .withId(2)
            .withName("IV")
            .withConceptNameType(ConceptNameType.SHORT)
            .withLocalePreferred(false)
            .build();

        names.add(new ConceptNameBuilder().build());
        names.add(shortName);
    }

    @Override
    public Concept build() {
        Concept result = new Concept();
        result.setConceptId(id);
        result.setNames(names);
        return result;
    }

    public ConceptBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public ConceptBuilder withNames(Collection<ConceptName> names) {
        this.names = names;
        return this;
    }
}
