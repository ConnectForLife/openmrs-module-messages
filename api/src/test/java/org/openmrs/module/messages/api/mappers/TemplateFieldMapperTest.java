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
import org.openmrs.module.messages.api.dto.TemplateFieldDTO;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.builder.TemplateFieldBuilder;
import org.openmrs.module.messages.builder.TemplateFieldDTOBuilder;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class TemplateFieldMapperTest {

    private static final String EXPECTED_POSSIBLE_VALUES = "Deactivate service|SMS|Call";

    private static final List<String> EXPECTED_POSSIBLE_VALUES_LIST = Arrays.asList("Deactivate service",
            "SMS", "Call");

    private TemplateField dao;

    private TemplateFieldDTO dto;

    private TemplateFieldMapper templateFieldMapper;

    private TemplateFieldDefaultValueMapper templateFieldDefaultValueMapper;

    @Before
    public void setUp() {
        templateFieldMapper = new TemplateFieldMapper();
        templateFieldDefaultValueMapper = new TemplateFieldDefaultValueMapper();
        templateFieldMapper.setTemplateFieldDefaultValueMapper(templateFieldDefaultValueMapper);
        dao = new TemplateFieldBuilder().build();
        dto = new TemplateFieldDTOBuilder().build();
    }

    @Test
    public void shouldMapToDtoSuccessfully() {
        TemplateFieldDTO actual = templateFieldMapper.toDto(dao);
        assertThat(actual, is(notNullValue()));
        assertEquals(dao.getId(), actual.getId());
        assertEquals(dao.getName(), actual.getName());
        assertEquals(dao.getMandatory(), actual.getMandatory());
        assertEquals(dao.getDefaultValue(), actual.getDefaultValue());
        assertEquals(dao.getTemplateFieldType().name(), actual.getType());
        assertEquals(dao.getUuid(), actual.getUuid());
        assertEquals(EXPECTED_POSSIBLE_VALUES_LIST, actual.getPossibleValues());
    }

    @Test
    public void shouldMapToDaoSuccessfully() {
        TemplateField actual = templateFieldMapper.fromDto(dto);
        assertThat(actual, is(notNullValue()));
        assertEquals(dto.getId(), actual.getId());
        assertEquals(dto.getName(), actual.getName());
        assertEquals(dto.getMandatory(), actual.getMandatory());
        assertEquals(dto.getDefaultValue(), actual.getDefaultValue());
        assertEquals(dto.getType(), actual.getTemplateFieldType().name());
        assertEquals(dto.getUuid(), actual.getUuid());
        assertEquals(EXPECTED_POSSIBLE_VALUES, actual.getPossibleValues());
    }
}
