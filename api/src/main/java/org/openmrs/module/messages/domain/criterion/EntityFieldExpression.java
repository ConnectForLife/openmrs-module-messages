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
