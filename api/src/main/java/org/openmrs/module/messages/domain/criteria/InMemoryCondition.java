package org.openmrs.module.messages.domain.criteria;

import org.openmrs.OpenmrsObject;

/**
 * The InMemoryCondition Class.
 * <p>
 * The In Memory Conditions are executed after data has been read from the database.
 * </p>
 * <p>
 * The {@code applyCondition} shall accept any {@code OpenmrsObject} entity and return boolean true or false whether the
 * entity fulfills the condition or not.
 * </p>
 */
public interface InMemoryCondition extends Condition<Boolean, OpenmrsObject> {

}
