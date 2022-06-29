/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

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
