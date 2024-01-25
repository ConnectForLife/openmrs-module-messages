/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.web.resource;

import org.openmrs.annotation.Handler;
import org.openmrs.module.messages.api.model.ActorResponseType;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingConverter;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;

@Handler(supports = {ActorResponseType.class})
public class ActorResponseTypeConverter extends BaseDelegatingConverter<ActorResponseType> {

  @Override
  public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
    DelegatingResourceDescription description = new DelegatingResourceDescription();
    description.addProperty("name");
    return description;
  }

  @Override
  public ActorResponseType newInstance(String name) {
    return new ActorResponseType(name);
  }

  @Override
  public ActorResponseType getByUniqueId(String name) {
    return new ActorResponseType(name);
  }
}
