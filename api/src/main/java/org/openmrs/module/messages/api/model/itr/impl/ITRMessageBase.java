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
