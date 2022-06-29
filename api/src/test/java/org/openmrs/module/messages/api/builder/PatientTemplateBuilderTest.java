/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.builder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openmrs.Patient;
import org.openmrs.module.messages.api.model.Actor;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.model.TemplateFieldType;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.api.util.DateUtil;
import org.openmrs.module.messages.builder.ActorBuilder;
import org.openmrs.module.messages.builder.PatientBuilder;
import org.openmrs.module.messages.builder.TemplateBuilder;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DateUtil.class})
public class PatientTemplateBuilderTest {

    private static final ZonedDateTime EXPECTED_START_OF_MESSAGES =
            ZonedDateTime.ofInstant(Instant.ofEpochSecond(1625756392L), ZoneId.of("UTC"));

    @Before
    public void setUp() throws Exception {
        PowerMockito.spy(DateUtil.class);
        // Sets DateUtil's clock to predefined and fixed point in time
        PowerMockito
                .field(DateUtil.class, "clock")
                .set(null, Clock.fixed(EXPECTED_START_OF_MESSAGES.toInstant(), EXPECTED_START_OF_MESSAGES.getZone()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateBuilderForNullTemplate() {
        new PatientTemplateBuilder(null, new Patient());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateBuilderForNullPatient() {
        new PatientTemplateBuilder(new Template(), null);
    }

    @Test(expected = Test.None.class /* no exception expected */)
    public void shouldCreateBuilderForNullActor() {
        new PatientTemplateBuilder(new Template(), new Patient());
    }

    @Test
    public void shouldBuildPatientTemplateProperlyForPatient() {
        Template template = new TemplateBuilder().build();
        Patient patient = new PatientBuilder().build();
        PatientTemplate patientTemplate = new PatientTemplateBuilder(template, patient).build();
        assertThat(patientTemplate, is(notNullValue()));
        assertThat(patientTemplate.getPatient().getId(), is(patient.getId()));
        assertThat(patientTemplate.getActor(), is(patient.getPerson()));
        for (TemplateFieldValue tfv : patientTemplate.getTemplateFieldValues()) {
            assertThat(tfv.getValue(), is(getExpectedPatientDefaultValue(tfv, template)));
        }
    }

    @Test
    public void shouldBuildPatientTemplateProperlyForActor() {
        Template template = new TemplateBuilder().build();
        Patient patient = new PatientBuilder().build();
        Actor actor = new ActorBuilder().build();
        PatientTemplate patientTemplate = new PatientTemplateBuilder(template, actor, patient).build();
        assertThat(patientTemplate, is(notNullValue()));
        assertThat(patientTemplate.getPatient().getId(), is(patient.getId()));
        assertThat(patientTemplate.getActor(), is(actor.getTarget()));
        for (TemplateFieldValue tfv : patientTemplate.getTemplateFieldValues()) {
            assertThat(tfv.getValue(), is(getExpectedActorDefaultValue(tfv, template, actor)));
        }
    }

    private String getExpectedPatientDefaultValue(TemplateFieldValue tfv, Template template) {
        TemplateFieldType type = tfv.getTemplateField().getTemplateFieldType();
        if (TemplateFieldType.START_OF_MESSAGES.equals(type)) {
            return DateUtil.formatToServerSideDateTime(EXPECTED_START_OF_MESSAGES);
        }
        TemplateField templateField = getTemplateFieldByType(type, template);
        return templateField != null ? templateField.getDefaultValue() : null;
    }

    private String getExpectedActorDefaultValue(TemplateFieldValue tfv, Template template, Actor actor) {
        TemplateFieldType type = tfv.getTemplateField().getTemplateFieldType();
        if (TemplateFieldType.START_OF_MESSAGES.equals(type)) {
            return DateUtil.formatToServerSideDateTime(EXPECTED_START_OF_MESSAGES);
        }
        TemplateField templateField = getTemplateFieldByType(type, template);
        return templateField != null ? templateField.getDefaultValueForSpecificActorOrGeneral(actor) : null;
    }

    private TemplateField getTemplateFieldByType(TemplateFieldType templateFieldType, Template template) {
        for (TemplateField tf : template.getTemplateFields()) {
            if (tf.getTemplateFieldType().equals(templateFieldType)) {
                return tf;
            }
        }
        return null;
    }

}
