/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.mappers;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;
import org.openmrs.module.messages.api.execution.ServiceResult;
import org.openmrs.module.messages.api.model.ScheduledServiceParameter;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class ScheduledParamsMapper implements ListMapper<ServiceResult, ScheduledServiceParameter> {

    private static final Log LOGGER = LogFactory.getLog(ScheduledParamsMapper.class);

    @Override
    public ServiceResult toDto(List<ScheduledServiceParameter> daos) {
        throw new NotImplementedException("mapping from List<ScheduledServiceParameter> to ServiceResult " +
                "is not implemented yet");
    }

    @Override
    public List<ScheduledServiceParameter> fromDto(ServiceResult dao) {
        List<ScheduledServiceParameter> result = new ArrayList<>();

        for (String key : dao.getAdditionalParams().keySet()) {
            Object value = dao.getAdditionalParams().get(key);
            if (null == value) {
                LOGGER.warn(String.format("Null value for `%s` key was noticed. This key will be skipped.", key));
                continue;
            }
            if (!BeanUtils.isSimpleValueType(value.getClass())) { // if not primitive value or String
                throw new MessagesRuntimeException("Complex object cannot be cast to ScheduledServiceParameter");
            }
            result.add(new ScheduledServiceParameter(key, value.toString()));
        }

        return result;
    }
}
