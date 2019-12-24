/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.builder;

import org.openmrs.module.messages.api.model.ScheduledServiceParameter;

public class ScheduledServiceParameterBuilder extends AbstractBuilder<ScheduledServiceParameter> {

    private Integer id;
    private String type;
    private String value;

    public ScheduledServiceParameterBuilder() {
    }

    @Override
    public ScheduledServiceParameter build() {
        ScheduledServiceParameter result = new ScheduledServiceParameter();
        result.setId(id);
        result.setParameterType(type);
        result.setParameterValue(value);
        return result;
    }

    @Override
    public ScheduledServiceParameter buildAsNew() {
        return withId(null).build();
    }

    public ScheduledServiceParameterBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public ScheduledServiceParameterBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public ScheduledServiceParameterBuilder withValue(String value) {
        this.value = value;
        return this;
    }
}
