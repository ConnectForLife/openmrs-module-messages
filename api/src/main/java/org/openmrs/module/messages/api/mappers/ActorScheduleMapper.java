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

import org.apache.commons.lang.NotImplementedException;
import org.openmrs.module.messages.api.dto.ActorScheduleDTO;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.util.ActorScheduleBuildingUtil;

public class ActorScheduleMapper extends AbstractOpenMrsDataMapper<ActorScheduleDTO, PatientTemplate> {

    @Override
    public ActorScheduleDTO toDto(PatientTemplate dao) {
        return ActorScheduleBuildingUtil.build(dao);
    }

    @Override
    public PatientTemplate fromDto(ActorScheduleDTO dto) {
        throw new NotImplementedException("mapping from ActorScheduleDTO to PatientTemplate is not implemented yet");
    }

    @Override
    public void updateFromDto(ActorScheduleDTO source, PatientTemplate target) {
        throw new NotImplementedException("update from DTO is not implemented yet");
    }
}
