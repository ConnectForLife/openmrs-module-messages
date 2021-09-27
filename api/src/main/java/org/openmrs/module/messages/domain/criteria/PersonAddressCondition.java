package org.openmrs.module.messages.domain.criteria;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.openmrs.PersonAddress;
import org.openmrs.module.messages.domain.criterion.EntityFieldExpression;

import static org.hibernate.criterion.Restrictions.and;
import static org.hibernate.criterion.Restrictions.eq;

/**
 * The PersonAddressCondition Class.
 * <p>
 * The Person Address Condition allows to make simple condition on the preferred address of a Person.
 * </p>
 */
public class PersonAddressCondition extends BaseJoinHibernateCondition {
    private static final String PREFERRED_FIELD_SUFFIX = ".preferred";

    private final String addressPropertyName;
    private final Object value;

    public PersonAddressCondition(String addressPropertyName, String operator, Object value) {
        super(addressPropertyName, operator);
        this.addressPropertyName = addressPropertyName;
        this.value = value;
    }

    @Override
    public Void applyCondition(Criteria hibernateCriteria) {
        final DetachedCriteria conditionQuery = DetachedCriteria.forClass(PersonAddress.class, getAlias());
        conditionQuery.setProjection(Property.forName(PERSON_PROP));

        final Criterion joinOnClause =
                and(new EntityFieldExpression(getAlias() + "." + addressPropertyName, getOperator(), value),
                        eq(getAlias() + VOIDED_FIELD_SUFFIX, false), eq(getAlias() + PREFERRED_FIELD_SUFFIX, true));
        conditionQuery.add(joinOnClause);

        hibernateCriteria.add(Property.forName(ID_PROP).in(conditionQuery));
        return null;
    }
}
