/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.util;

import org.openmrs.OpenmrsObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Allows to store entities in a set based only on database identifier.
 * It's the best to avoid entities with id equal to null, but this case
 * is also handled by equals() and hashCode() methods.
 */
public class IdSetWrapper<T extends OpenmrsObject> {

    private T wrapped;

    public IdSetWrapper(T o) {
        wrapped = o;
    }

    public T getWrapped() {
        return wrapped;
    }

    public static <T extends OpenmrsObject> Set<IdSetWrapper<T>> wrapNonNull(List<T> entities) {
        Set<IdSetWrapper<T>> wrapped = new HashSet<IdSetWrapper<T>>();
        for (T entity : entities) {
            if (entity != null && entity.getId() != null) {
                wrapped.add(new IdSetWrapper<T>(entity));
            }
        }
        return wrapped;
    }

    public static <T extends OpenmrsObject> List<T> unwrap(Set<IdSetWrapper<T>> entities) {
        List<T> unwrapped = new ArrayList<T>();
        for (IdSetWrapper<T> wrapped : entities) {
            unwrapped.add(wrapped.getWrapped());
        }
        return unwrapped;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IdSetWrapper<?> that = (IdSetWrapper<?>) o;

        if (wrapped != null) {
            if (wrapped.getId() != null) {
                return wrapped.getId().equals(that.wrapped.getId());
            } else {
                return that.wrapped.getId() == null;
            }
        } else {
            return that.wrapped == null;
        }
    }

    @Override
    public int hashCode() {
        return (wrapped != null && wrapped.getId() != null) ? wrapped.getId() : 0;
    }
}
