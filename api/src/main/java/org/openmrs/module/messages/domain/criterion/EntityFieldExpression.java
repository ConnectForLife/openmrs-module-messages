/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.domain.criterion;

import org.hibernate.criterion.SimpleExpression;

/**
 * The EntityFieldExpression Class.
 * <p>
 * Exposes {@link SimpleExpression} constructor.
 * </p>
 */
public class EntityFieldExpression extends SimpleExpression {

    private static final long serialVersionUID = -7339973977316451199L;

    public EntityFieldExpression(String propertyName, String op, Object value) {
        super(propertyName, value, op);
    }
}
