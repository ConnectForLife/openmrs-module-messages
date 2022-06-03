/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.dto;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class TemplateFieldValueDTOTest {

    private static final Integer ID = 1;

    private static final Integer TEMPLATE_FIELD_ID = 5;

    private static final String VALUE = "Deactivate service";

    private static final String UUID = "1b6ebd50-0444-4358-9f24-ee2cce78fe63";

    private TemplateFieldValueDTO templateFieldValueDTO;

    @Test
    public void shouldBuildDtoObjectSuccessfully() {
        templateFieldValueDTO = buildTemplateFieldValueDTOObject();

        assertThat(templateFieldValueDTO, is(notNullValue()));
        assertEquals(ID, templateFieldValueDTO.getId());
        assertEquals(TEMPLATE_FIELD_ID, templateFieldValueDTO.getTemplateFieldId());
        assertEquals(VALUE, templateFieldValueDTO.getValue());
        assertEquals(UUID, templateFieldValueDTO.getUuid());
    }

    private TemplateFieldValueDTO buildTemplateFieldValueDTOObject() {
        TemplateFieldValueDTO dto = new TemplateFieldValueDTO()
                .withId(ID)
                .withTemplateFieldId(TEMPLATE_FIELD_ID)
                .withValue(VALUE)
                .withUuid(UUID);
        return dto;
    }
}
