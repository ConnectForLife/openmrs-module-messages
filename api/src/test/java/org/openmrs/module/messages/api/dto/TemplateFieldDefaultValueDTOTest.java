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
import org.openmrs.module.messages.api.model.RelationshipTypeDirection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class TemplateFieldDefaultValueDTOTest {

    private static final Integer ID = 1;

    private static final Integer RELATIONSHIP_TYPE_ID = 3;

    private static final Integer TEMPLATE_FIELD_ID = 22;

    private static final RelationshipTypeDirection DIRECTION = RelationshipTypeDirection.A;

    private static final String DEFAULT_VALUE = "Weekly";

    private TemplateFieldDefaultValueDTO templateFieldDefaultValueDTO;

    @Test
    public void shouldBuildDtoObjectSuccessfully() {
        templateFieldDefaultValueDTO = buildTemplateFieldDefaultValueDTOObject();

        assertThat(templateFieldDefaultValueDTO, is(notNullValue()));
        assertEquals(ID, templateFieldDefaultValueDTO.getId());
        assertEquals(RELATIONSHIP_TYPE_ID, templateFieldDefaultValueDTO.getRelationshipTypeId());
        assertEquals(TEMPLATE_FIELD_ID, templateFieldDefaultValueDTO.getTemplateFieldId());
        assertEquals(DIRECTION, templateFieldDefaultValueDTO.getDirection());
        assertEquals(DEFAULT_VALUE, templateFieldDefaultValueDTO.getDefaultValue());
    }

    private TemplateFieldDefaultValueDTO buildTemplateFieldDefaultValueDTOObject() {
        TemplateFieldDefaultValueDTO dto = new TemplateFieldDefaultValueDTO()
                .setId(ID)
                .setRelationshipTypeId(RELATIONSHIP_TYPE_ID)
                .setTemplateFieldId(TEMPLATE_FIELD_ID)
                .setDirection(DIRECTION)
                .setDefaultValue(DEFAULT_VALUE);
        return dto;
    }
}
