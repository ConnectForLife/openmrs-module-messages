/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.util.end.date;

import org.openmrs.module.messages.api.util.EndDateType;

public class EndDateFactory {

    public EndDate create(EndDateType type, EndDateParams params) {
        switch (type) {
            case DATE_PICKER:
                return new SimpleEndDate(params);
            case AFTER_TIMES:
                return new AfterTimesEndDate(params);
            case OTHER:
                return new OtherEndDate(params);
            case NO_DATE:
            default:
                return new NoEndDate();
        }
    }
}
