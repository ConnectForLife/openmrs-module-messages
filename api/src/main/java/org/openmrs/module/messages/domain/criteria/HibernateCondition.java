/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.domain.criteria;

import org.hibernate.Criteria;

/**
 * The HibernateCondition Class.
 * <p>
 * The Hibernate Conditions are converted to Hibernate criteria query conditions and are executed as part of reading the
 * data from database.
 * </p>
 * <p>
 * The {@code applyCondition} shall accept {@code Criteria} object, and it shall apply its condition to this Criteria. The
 * return result shall be ignored.
 * </p>
 */
public interface HibernateCondition extends Condition<Void, Criteria> {

}
