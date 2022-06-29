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

import org.openmrs.module.messages.api.dto.TemplateFieldValueDTO;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.model.TemplateFieldValue;

public class TemplateFieldValueMapper extends AbstractOpenMrsDataMapper<TemplateFieldValueDTO, TemplateFieldValue> {

    @Override
    public TemplateFieldValueDTO toDto(TemplateFieldValue dao) {
        return new TemplateFieldValueDTO()
            .withId(dao.getId())
            .withValue(dao.getValue())
            .withTemplateFieldId(dao.getTemplateField().getId())
            .withUuid(dao.getUuid());
    }

    @Override
    public TemplateFieldValue fromDto(TemplateFieldValueDTO dto) {
        TemplateFieldValue dao = new TemplateFieldValue();
        dao.setId(dto.getId());
        dao.setValue(dto.getValue());
        if (dto.getUuid() != null) {
            dao.setUuid(dto.getUuid());
        }

        PatientTemplate patientTemplate = new PatientTemplate();
        dao.setPatientTemplate(patientTemplate);

        TemplateField templateField = new TemplateField();
        templateField.setId(dto.getTemplateFieldId());
        dao.setTemplateField(templateField);

        return dao;
    }

    @Override
    public void updateFromDto(TemplateFieldValueDTO source, TemplateFieldValue target) {
        target.setValue(source.getValue());
        target.setTemplateField(new TemplateField(source.getTemplateFieldId()));
    }
}
