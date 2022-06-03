/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.mappers;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.module.messages.api.dto.DTO;

/**
 * The AbstractOpenMrsDataMapper is a base AbstractMapper for OpenMRS Data entities.
 */
public abstract class AbstractOpenMrsDataMapper<T extends DTO, R extends BaseOpenmrsData> extends AbstractMapper<T, R> {

    @Override
    protected void doSafeDelete(R target) {
        target.setVoided(Boolean.TRUE);
    }
}
