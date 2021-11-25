package org.openmrs.module.messages.api.dao;

import org.openmrs.ConceptAttribute;

import java.util.List;

/**
 * The ExtendedConceptDAO Class.
 * <p>This DAO provides extension to {@link org.openmrs.api.db.ConceptDAO} functions.
 */
public interface ExtendedConceptDAO {
  /**
   * Get all non-voided ConceptAttributes of Concept Attribute Type with {@code uuid}.
   *
   * @param uuid the Concept Attribute Type UUID, not null
   * @return the List of ConceptAttributes, never null
   */
  List<ConceptAttribute> getConceptAttributesByTypeUuid(String uuid);
}
