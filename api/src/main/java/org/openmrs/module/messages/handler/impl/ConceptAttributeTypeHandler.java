/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.handler.impl;

import org.openmrs.ConceptAttributeType;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.metadatadeploy.handler.AbstractObjectDeployHandler;

@Handler(supports = ConceptAttributeType.class)
public class ConceptAttributeTypeHandler extends AbstractObjectDeployHandler<ConceptAttributeType> {
  @Override
  public ConceptAttributeType fetch(String identifier) {
    return Context.getConceptService().getConceptAttributeTypeByUuid(identifier);
  }

  @Override
  public ConceptAttributeType save(ConceptAttributeType obj) {
    return Context.getConceptService().saveConceptAttributeType(obj);
  }

  @Override
  public void uninstall(ConceptAttributeType obj, String reason) {
    Context.getConceptService().retireConceptAttributeType(obj, reason);
  }
}
