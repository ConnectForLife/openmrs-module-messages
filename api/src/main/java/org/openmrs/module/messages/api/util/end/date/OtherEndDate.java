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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.messages.api.util.TimeType;

import java.time.ZonedDateTime;

import static org.openmrs.module.messages.api.util.FieldDateUtil.SEPARATOR;

public class OtherEndDate implements EndDate {

    private static final Log LOGGER = LogFactory.getLog(OtherEndDate.class);

    private EndDateParams params;

    public OtherEndDate(EndDateParams params) {
        this.params = params;
    }

    @Override
    public ZonedDateTime getDate() {
        ZonedDateTime endDate = null;

        if (null != params && null != params.getValue() && null != params.getStartDate()) {
            String[] chain = StringUtils.split(params.getValue(), SEPARATOR);
            if (chain.length > 1) {
                try {
                    final TimeType timeType = TimeType.valueOf(chain[0]);
                    final int amount = Integer.parseInt(chain[1]);

                    endDate = params.getStartDate().plus(amount, timeType.getTemporalUnit());
                } catch (IllegalArgumentException e) {
                    LOGGER.warn("Exception occurred during processing end date: " + e.getMessage());
                }
            }
        } else {
            LOGGER.warn("The end date could not be calculated for provided params:" + params);
        }

        return endDate;
    }
}
