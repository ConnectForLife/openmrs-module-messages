/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.util;

import java.io.Serializable;
import org.openmrs.BaseOpenmrsData;
import org.openmrs.module.messages.api.exception.EntityNotFoundException;
import org.openmrs.module.messages.api.service.OpenmrsDataService;

public final class HibernateUtil {

    public static <T extends BaseOpenmrsData> T getNotNull(Serializable id, OpenmrsDataService<T> service) {
        T entity = service.getById(id);
        if (entity == null) {
            throw new EntityNotFoundException(String.format(
                    "Cannot get the object with id %s from %s. Entity not found.", id, service.getClass()));
        }
        return entity;
    }

    private HibernateUtil() {
    }
}
