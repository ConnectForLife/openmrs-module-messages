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
