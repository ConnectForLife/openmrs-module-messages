/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service;

import org.junit.Test;
import org.openmrs.module.messages.ContextSensitiveTest;
import org.openmrs.module.messages.api.dto.TemplateDTO;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.builder.TemplateBuilder;
import org.openmrs.module.messages.builder.TemplateDTOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class TemplateServiceImplTest extends ContextSensitiveTest {

    private static final String SERVICE_QUERY = "SELECT * FROM template";
    private static final String SERVICE_QUERY_TYPE = "SQL";
    private static final String NAME = "Example service";
    @Autowired
    @Qualifier("messages.templateService")
    private TemplateService templateService;
    private Template template;
    private TemplateDTO templateDto;
    private List<TemplateDTO> templateDtos;

    @Test
    public void shouldSaveOrUpdateTemplateSuccessfully() {
        template = new TemplateBuilder().build();
        templateService.saveOrUpdate(template);

        assertThat(template, is(notNullValue()));
        assertEquals(SERVICE_QUERY, template.getServiceQuery());
        assertEquals(SERVICE_QUERY_TYPE, template.getServiceQueryType());
        assertEquals(NAME, template.getName());
    }

    @Test
    public void shouldSaveOrUpdateTemplateDtoSuccessfully() {
        templateDto = new TemplateDTOBuilder().build();
        templateService.saveOrUpdateByDto(templateDto);

        assertThat(templateDto, is(notNullValue()));
        assertEquals(SERVICE_QUERY, templateDto.getServiceQuery());
        assertEquals(SERVICE_QUERY_TYPE, templateDto.getServiceQueryType());
        assertEquals(NAME, templateDto.getName());
    }

    @Test
    public void shouldSaveOrUpdateTemplateDtosSuccessfully() {
        templateDtos = Arrays.asList(new TemplateDTOBuilder().build(), new TemplateDTOBuilder().build());
        templateService.saveOrUpdateByDtos(templateDtos);

        assertEquals(2, templateDtos.size());
        for (TemplateDTO templateDTO : templateDtos) {
            assertThat(templateDTO, is(notNullValue()));
            assertEquals(SERVICE_QUERY, templateDTO.getServiceQuery());
            assertEquals(SERVICE_QUERY_TYPE, templateDTO.getServiceQueryType());
            assertEquals(NAME, templateDTO.getName());
        }
    }
}
