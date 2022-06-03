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

import org.junit.Test;
import org.openmrs.module.messages.api.dto.MessageDetailsDTO;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.util.StaticMappersProvider;
import org.openmrs.module.messages.builder.PatientBuilder;
import org.openmrs.module.messages.builder.PatientTemplateBuilder;
import org.openmrs.module.messages.builder.TemplateBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MessageDetailsMapperTest {

    private static final int PATIENT_2_ID = 682;
    private static final int PATIENT_1_ID = 328;
    private static final int TEMPLATE_1_ID = 284;
    private static final int TEMPLATE_2_ID = 720;

    private MessageDetailsMapper messageDetailsMapper = StaticMappersProvider.getMessageDetailsMapper();

    @Test
    public void shouldAccumulateMessagesWithTheSameTemplate() {
        List<PatientTemplate> templates = new ArrayList<>();

        Template template = new TemplateBuilder()
                .withId(TEMPLATE_1_ID).build();

        templates.add(buildPatientTemplate(PATIENT_1_ID, template));
        templates.add(buildPatientTemplate(PATIENT_2_ID, template));

        MessageDetailsDTO result = messageDetailsMapper.toDto(templates);

        assertEquals(1, result.getMessages().size());
        assertEquals(2, result.getMessages().get(0).getActorSchedules().size());
    }

    @Test
    public void shouldNotAccumulateMessagesWithDifferentTemplate() {
        List<PatientTemplate> templates = new ArrayList<>();

        Template template1 = new TemplateBuilder()
                .withId(TEMPLATE_1_ID).build();

        Template template2 = new TemplateBuilder()
                .withId(TEMPLATE_2_ID).build();

        templates.add(buildPatientTemplate(PATIENT_1_ID, template1));
        templates.add(buildPatientTemplate(PATIENT_2_ID, template2));

        MessageDetailsDTO result = messageDetailsMapper.toDto(templates);

        assertEquals(2, result.getMessages().size());
        assertEquals(1, result.getMessages().get(0).getActorSchedules().size());
        assertEquals(1, result.getMessages().get(1).getActorSchedules().size());
    }

    private PatientTemplate buildPatientTemplate(int patientId, Template baseTemplate) {
        return new PatientTemplateBuilder()
                .withPatient(new PatientBuilder()
                        .withId(patientId)
                        .build())
                .withTemplate(baseTemplate)
                .build();
    }
}
