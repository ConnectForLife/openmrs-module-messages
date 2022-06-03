/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.domain.criteria;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;

public class TemplateCriteria extends BaseOpenmrsMetadataCriteria implements Serializable {

    private static final long serialVersionUID = -651234477563418680L;

    private final String name;

    TemplateCriteria(final String name) {
        this.name = name;
    }

    public static TemplateCriteria forName(final String name) {
        return new TemplateCriteria(name);
    }

    public static TemplateCriteria nonRetired() {
        return new TemplateCriteria(null);
    }

    @Override
    public void loadHibernateCriteria(Criteria hibernateCriteria) {
        if (name != null) {
            hibernateCriteria.add(Restrictions.eq("name", name));
        }
    }
}
