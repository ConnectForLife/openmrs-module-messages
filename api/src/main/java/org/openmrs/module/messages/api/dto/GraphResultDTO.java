/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.dto;

import java.util.Map;

public class GraphResultDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    private Integer result;

    private String alias;

    private transient Map<String, Object> configMap;

    public Integer getResult() {
        return result;
    }

    public GraphResultDTO setResult(Integer result) {
        this.result = result;
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public GraphResultDTO setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public Map<String, Object> getConfigMap() {
        return configMap;
    }

    public GraphResultDTO setConfigMap(Map<String, Object> configMap) {
        this.configMap = configMap;
        return this;
    }
}
