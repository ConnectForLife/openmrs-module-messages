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
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;

import java.util.ArrayList;
import java.util.List;

public final class OpenmrsObjectUtil {

    public static <T extends OpenmrsObject> List<Integer> getIds(List<T> openmrsObjects) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (OpenmrsObject openmrsObject : openmrsObjects) {
            if (openmrsObject.getId() == null) {
                throw new MessagesRuntimeException("OpenmrsObject must have id set");
            }
            ids.add(openmrsObject.getId());
        }
        return ids;
    }

    private OpenmrsObjectUtil() {
    }
}
