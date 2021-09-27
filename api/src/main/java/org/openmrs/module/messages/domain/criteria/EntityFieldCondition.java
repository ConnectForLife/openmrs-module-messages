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
