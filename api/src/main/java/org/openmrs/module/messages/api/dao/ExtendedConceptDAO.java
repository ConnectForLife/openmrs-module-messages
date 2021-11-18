package org.openmrs.module.messages.api.dao;

import org.openmrs.ConceptAttribute;

import java.util.List;

public interface ExtendedConceptDAO {
  List<ConceptAttribute> getConceptAttributesByTypeUuid(String uuid);
}
