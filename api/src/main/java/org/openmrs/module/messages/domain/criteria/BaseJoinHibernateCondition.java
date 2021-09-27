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
