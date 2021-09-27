/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.util;

import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;

public class Properties {

    private final Map<String, Object> propertiesMap;

    public Properties(Map<String, Object> propertiesMap) {
        this.propertiesMap = propertiesMap;
    }

    public Object get(String propertyName) {
        return propertiesMap.get(propertyName);
    }

    public Properties getNestedProperties(String propertyName) {
        Object value = propertiesMap.get(propertyName);
        if (value == null) {
            return null;
        } else if (value instanceof Map<?, ?>) {
            Map<Object, Object> map = (Map<Object, Object>) value;
            for (Object key : map.keySet()) {
                if (!(key instanceof String)) {
                    throw new MessagesRuntimeException(String.format(
                            "Cannot parse %s as nested properties - not all keys are strings: %s",
                            propertyName,
                            value));
                }
            }
            return new Properties((Map<String, Object>) value);
        } else {
            throw new MessagesRuntimeException(String.format(
                    "Cannot parse %s as nested properties - invalid type: %s ",
                    propertyName,
                    value.getClass().getName()));
        }
    }

    public String getString(String propertyName) {
        Object value = propertiesMap.get(propertyName);
        return value == null ? null : value.toString();
    }

    public Integer getInt(String propertyName) {
        Object value = propertiesMap.get(propertyName);
        if (value == null) {
            return null;
        } else if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof String) {
            return Integer.parseInt(value.toString());
        } else {
            throw new MessagesRuntimeException(String.format("Cannot parse %s as int - invalid type: %s ",
                    propertyName,
                    value.getClass().getName()));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return "Properties" + propertiesMap;
    }
}
