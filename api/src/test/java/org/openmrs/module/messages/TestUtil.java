/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages;

import java.util.Date;

import static org.openmrs.module.messages.api.util.DateUtil.DAY_IN_SECONDS;

public final class TestUtil {

    public static final int DAY_IN_MILLISECONDS = DAY_IN_SECONDS * 1000;

    public static Date getMaxTimeForDate(int year, int month, int dayOfMonth) {
        final int y = year - 1900; // new Date implementation is x - 1900
        Date result = new Date(y, month, dayOfMonth);
        result.setTime(result.getTime() + DAY_IN_MILLISECONDS - 1);

        return result;
    }

    public static Date getMinTimeForDate(int year, int month, int dayOfMonth) {
        final int y = year - 1900; // new Date implementation is x - 1900
        return new Date(y, month, dayOfMonth);
    }

    private TestUtil() {
    }
}
