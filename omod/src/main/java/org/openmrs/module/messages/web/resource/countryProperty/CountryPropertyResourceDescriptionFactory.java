package org.openmrs.module.messages.web.resource.countryProperty;

import org.openmrs.module.messages.api.model.CountryProperty;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;

public class CountryPropertyResourceDescriptionFactory {
  private CountryPropertyResourceDescriptionFactory() {}

  public static DelegatingResourceDescription newRefRepresentation() {
    final DelegatingResourceDescription description = new DelegatingResourceDescription();
    description.addProperty(CountryProperty.UUID);
    description.addProperty(CountryProperty.VALUE);
    description.addSelfLink();
    return description;
  }

  public static DelegatingResourceDescription newDefaultRepresentation() {
    final DelegatingResourceDescription description = new DelegatingResourceDescription();
    description.addProperty(CountryProperty.UUID);
    description.addProperty(CountryProperty.NAME);
    description.addProperty(CountryProperty.COUNTRY, Representation.REF);
    description.addProperty(CountryProperty.VALUE);
    description.addProperty(CountryProperty.DESCRIPTION);
    description.addSelfLink();
    return description;
  }

  public static DelegatingResourceDescription newCreatableProperties() {
    final DelegatingResourceDescription creatableProperties = new DelegatingResourceDescription();
    creatableProperties.addProperty(CountryProperty.NAME);
    creatableProperties.addProperty(CountryProperty.DESCRIPTION);
    creatableProperties.addProperty(CountryProperty.COUNTRY);
    creatableProperties.addProperty(CountryProperty.VALUE);
    return creatableProperties;
  }

  public static DelegatingResourceDescription newUpdatableProperties() {
    final DelegatingResourceDescription updatableProperties = new DelegatingResourceDescription();
    updatableProperties.addProperty(CountryProperty.VALUE);
    return updatableProperties;
  }
}
