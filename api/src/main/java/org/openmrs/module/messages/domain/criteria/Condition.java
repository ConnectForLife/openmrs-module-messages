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

/**
 * The Condition Class.
 * <p>
 * The base interface for objects which are conditions supported by {@link QueryCriteria}.
 * </p>
 *
 * @param <R> the type of result of an applying this condition
 * @param <D>   the type of object to which condition is applied
 * @see HibernateCondition
 * @see InMemoryCondition
 */
public interface Condition<R, D> {
    /**
     * Applies this condition.
     * <p>
     * The argument and result depend on specific implementation of the Condition.
     * </p>
     *
     * @param to the object where condition is applied
     * @return the result of applying this condition to the {@code to} object
     */
    R applyCondition(D to);
}
