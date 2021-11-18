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
