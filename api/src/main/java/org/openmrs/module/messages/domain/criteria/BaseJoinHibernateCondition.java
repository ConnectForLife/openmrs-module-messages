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

import java.util.concurrent.atomic.AtomicInteger;

public abstract class BaseJoinHibernateCondition implements HibernateCondition {
    protected static final String VOIDED_FIELD_SUFFIX = ".voided";
    protected static final String PERSON_PROP = "person";
    protected static final String ID_PROP = "id";

    private static final AtomicInteger ALIAS_GEN = new AtomicInteger(0);

    private final String alias;
    private final String operator;

    protected BaseJoinHibernateCondition(String aliasPrefix, String operator) {
        this.alias = generateJoinAlias(aliasPrefix);
        this.operator = operator;
    }

    protected String getAlias() {
        return alias;
    }

    protected String getOperator() {
        return operator;
    }

    private String generateJoinAlias(String aliasPrefix) {
        return "join" + aliasPrefix + ALIAS_GEN.incrementAndGet();
    }
}
