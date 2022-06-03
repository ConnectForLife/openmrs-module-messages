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
import org.hibernate.criterion.Restrictions;
import org.openmrs.module.messages.domain.criterion.EntityFieldExpression;

import java.util.Collection;

/**
 * Simple condition on an Entity field.
 */
public class EntityFieldCondition implements HibernateCondition {
    private static final String IN_OPERATOR = "in";
    private final String propertyName;
    private final Object value;
    private final String operator;

    /**
     * @param propertyName the entity property accessor, not null
     * @param operator     the HQL operator, not null
     * @param value        the condition parameter
     */
    public EntityFieldCondition(final String propertyName, final String operator, final Object value) {
        this.propertyName = propertyName;
        this.value = value;
        this.operator = operator;
    }

    @Override
    public Void applyCondition(final Criteria hibernateCriteria) {
        if (operator.trim().equalsIgnoreCase(IN_OPERATOR)) {
            hibernateCriteria.add(Restrictions.in(propertyName, (Collection) value));
        } else {
            hibernateCriteria.add(new EntityFieldExpression(propertyName, operator, value));
        }

        return null;
    }
}
