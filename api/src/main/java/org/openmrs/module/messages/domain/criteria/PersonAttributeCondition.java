package org.openmrs.module.messages.domain.criteria;

import org.apache.commons.lang.ObjectUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.openmrs.Attributable;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.module.messages.domain.criterion.EntityFieldExpression;

import static org.hibernate.criterion.Restrictions.and;
import static org.hibernate.criterion.Restrictions.eq;

/**
 * The condition on Person Attribute.
 */
public class PersonAttributeCondition extends BaseJoinHibernateCondition {
    private static final String VALUE_FIELD_SUFFIX = ".value";

    private final String valueAsString;

    public PersonAttributeCondition(PersonAttributeType attributeType, String operator, Object value) {
        super(attributeType.getPersonAttributeTypeId().toString(), operator);
        this.valueAsString = convertValueToString(value);
    }

    @Override
    public Void applyCondition(Criteria hibernateCriteria) {
        final DetachedCriteria conditionQuery = DetachedCriteria.forClass(PersonAttribute.class, getAlias());
        conditionQuery.setProjection(Property.forName(PERSON_PROP));

        final Criterion joinOnClause =
                and(new EntityFieldExpression(getAlias() + VALUE_FIELD_SUFFIX, getOperator(), valueAsString),
                        eq(getAlias() + VOIDED_FIELD_SUFFIX, false));
        conditionQuery.add(joinOnClause);

        hibernateCriteria.add(Property.forName(ID_PROP).in(conditionQuery));
        return null;
    }

    private String convertValueToString(final Object object) {
        if (object instanceof Attributable) {
            final Attributable attributableObj = (Attributable) object;
            return attributableObj.serialize();
        }

        return ObjectUtils.toString(object, null);
    }
}