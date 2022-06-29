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

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.messages.api.dto.TemplateFieldValueDTO;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.builder.TemplateFieldValueBuilder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class TemplateFieldValueMapperTest {

    private static final Integer ID = 1;

    private static final Integer TEMPLATE_FIELD_ID = 22;

    private static final String VALUE = "Deactivate service";

    private static final String UUID = "5e34588e-fac8-4437-abfb-6cb8534ba392";

    private TemplateFieldValue dao;

    private TemplateFieldValueDTO dto;

    private TemplateFieldValueMapper templateFieldValueMapper;

    @Before
    public void setUp() {
        templateFieldValueMapper = new TemplateFieldValueMapper();
        dao = new TemplateFieldValueBuilder().build();
        dto = buildTemplateFieldValueDTOObject();
    }

    @Test
    public void shouldMapToDtoSuccessfully() {
        TemplateFieldValueDTO templateFieldValueDTO = templateFieldValueMapper.toDto(dao);

        assertThat(templateFieldValueDTO, is(notNullValue()));
        assertEquals(dao.getId(), templateFieldValueDTO.getId());
        assertEquals(dao.getTemplateField().getId(), templateFieldValueDTO.getTemplateFieldId());
        assertEquals(dao.getValue(), templateFieldValueDTO.getValue());
        assertEquals(dao.getUuid(), templateFieldValueDTO.getUuid());
    }

    @Test
    public void shouldMapToDaoSuccessfully() {
        TemplateFieldValue templateFieldValue = templateFieldValueMapper.fromDto(dto);

        assertThat(templateFieldValue, is(notNullValue()));
        assertEquals(dto.getId(), templateFieldValue.getId());
        assertEquals(dto.getTemplateFieldId(), templateFieldValue.getTemplateField().getId());
        assertEquals(dto.getValue(), templateFieldValue.getValue());
        assertEquals(dto.getUuid(), templateFieldValue.getUuid());
    }

    private TemplateFieldValueDTO buildTemplateFieldValueDTOObject() {
         TemplateFieldValueDTO templateFieldValueDTO = new TemplateFieldValueDTO()
                .withId(ID)
                .withTemplateFieldId(TEMPLATE_FIELD_ID)
                .withValue(VALUE)
                .withUuid(UUID);
        return templateFieldValueDTO;
    }
}
