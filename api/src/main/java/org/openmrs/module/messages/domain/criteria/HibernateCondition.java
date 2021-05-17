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
