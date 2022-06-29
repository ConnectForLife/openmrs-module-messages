/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.model.itr.impl;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Concept;
import org.openmrs.ConceptAttribute;
import org.openmrs.ConceptAttributeType;
import org.openmrs.api.context.Context;

import java.util.List;

class ITRMessageBase {
  protected String getStringAttribute(String uuidGlobalProperty, Concept messageConcept) {
    final String result;
    final String attributeTypeUuid = Context.getAdministrationService().getGlobalProperty(uuidGlobalProperty);

    if (StringUtils.isBlank(attributeTypeUuid)) {
      result = null;
    } else {
      final ConceptAttributeType attributeType =
          Context.getConceptService().getConceptAttributeTypeByUuid(attributeTypeUuid);

      final List<ConceptAttribute> attributeValues = messageConcept.getActiveAttributes(attributeType);
      result = attributeValues.isEmpty() ? null : attributeValues.get(0).getValue().toString();
    }

    return result;
  }
}
