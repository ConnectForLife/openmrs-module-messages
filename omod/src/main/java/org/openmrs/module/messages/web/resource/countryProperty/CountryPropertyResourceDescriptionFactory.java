/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.web.resource.countryProperty;

import org.openmrs.module.messages.api.model.CountryProperty;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;

public class CountryPropertyResourceDescriptionFactory {
  private CountryPropertyResourceDescriptionFactory() {}

  public static DelegatingResourceDescription newRefRepresentation() {
    final DelegatingResourceDescription description = new DelegatingResourceDescription();
    description.addProperty(CountryProperty.UUID_PROP_NAME);
    description.addProperty(CountryProperty.VALUE_PROP_NAME);
    description.addSelfLink();
    return description;
  }

  public static DelegatingResourceDescription newDefaultRepresentation() {
    final DelegatingResourceDescription description = new DelegatingResourceDescription();
    description.addProperty(CountryProperty.UUID_PROP_NAME);
    description.addProperty(CountryProperty.NAME_PROP_NAME);
    description.addProperty(CountryProperty.COUNTRY_PROP_NAME, Representation.REF);
    description.addProperty(CountryProperty.VALUE_PROP_NAME);
    description.addProperty(CountryProperty.DESCRIPTION_PROP_NAME);
    description.addSelfLink();
    return description;
  }

  public static DelegatingResourceDescription newCreatableProperties() {
    final DelegatingResourceDescription creatableProperties = new DelegatingResourceDescription();
    creatableProperties.addProperty(CountryProperty.NAME_PROP_NAME);
    creatableProperties.addProperty(CountryProperty.DESCRIPTION_PROP_NAME);
    creatableProperties.addProperty(CountryProperty.COUNTRY_PROP_NAME);
    creatableProperties.addProperty(CountryProperty.VALUE_PROP_NAME);
    return creatableProperties;
  }

  public static DelegatingResourceDescription newUpdatableProperties() {
    final DelegatingResourceDescription updatableProperties = new DelegatingResourceDescription();
    updatableProperties.addProperty(CountryProperty.VALUE_PROP_NAME);
    return updatableProperties;
  }
}
