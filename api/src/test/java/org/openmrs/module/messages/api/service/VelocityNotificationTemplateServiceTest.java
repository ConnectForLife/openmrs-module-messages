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

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.ContextSensitiveTest;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Template;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.openmrs.module.messages.api.service.DatasetConstants.XML_DATA_SET_PATH;

public class VelocityNotificationTemplateServiceTest extends ContextSensitiveTest {

    private static final int PATIENT_ID = 666;

    private static final String SIMPLE_TEST_SERVICE_NAME = "test";

    private static final String SIMPLE_TEST_SERVICE_EXPECTED_MESSAGE = "message:\"This is test message.\"";

    private static final String COMPLEX_TEST_SERVICE_NAME = "test service2";

    private static final String COMPLEX_TEST_SERVICE_EXPECTED_MESSAGE = "message:\"This is test messages. "
            + "For patient 666. Value of injected services is administrationService:adminService,notExisting:notExisting. "
            + "This is input service param `This is additional service parameter`\"";

    private static final String COMPLEX_TEST_SERVICE_EXPECTED_MESSAGE_WITHOUT_SERVICE_PARAM =
            "message:\"This is test messages. "
            + "For patient 666. Value of injected services is administrationService:adminService,notExisting:notExisting. "
            + "This is input service param `$test`\"";

    private static final String WHATSAPP_SERVICE_NAME = "whatsapp";
    private static final String WHATSAPP_SERVICE_EXPECTED_TEMPLATE = "id:\"111\",language:\"EN\",patientId:\"666\"";

    private static final String SERVICE_TEST_PROP = "test";

    private static final String SERVICE_PARAMETER = "This is additional service parameter";

    private static final String MIXED_LETTER_CASE_SERVICE_NAME = "TeSt";

    private static final String WRONG_INJECTED_SERVICES_PROP_VALUE = "test]sadad";

    private static final String NOT_EXISTING_SERVICE = "not existing service";

    @Autowired
    private NotificationTemplateService notificationTemplateService;

    @Before
    public void setUp() throws Exception {
        executeDataSet(XML_DATA_SET_PATH + "NotificationTemplateDataset.xml");
    }

    @Test
    public void parseTemplateForServiceShouldReturnExpectedPlainMessage() {
        PatientTemplate patientTemplate = buildPatientTemplate(SIMPLE_TEST_SERVICE_NAME);
        Map<String, String> serviceParam = buildServiceParams();
        String actual = notificationTemplateService.parseTemplate(patientTemplate, serviceParam);
        assertThat(actual, is(SIMPLE_TEST_SERVICE_EXPECTED_MESSAGE));
    }

    @Test
    public void parseTemplateForServiceShouldReturnExpectedComplexMessage() {
        PatientTemplate patientTemplate = buildPatientTemplate(COMPLEX_TEST_SERVICE_NAME);
        Map<String, String> serviceParam = buildServiceParams();
        String actual = notificationTemplateService.parseTemplate(patientTemplate, serviceParam);
        assertThat(actual, is(COMPLEX_TEST_SERVICE_EXPECTED_MESSAGE));
    }

    @Test
    public void parseTemplateForServiceShouldReplaceWhitespaceToDash() {
        PatientTemplate patientTemplate = buildPatientTemplate(COMPLEX_TEST_SERVICE_NAME);
        Map<String, String> serviceParam = buildServiceParams();
        String actual = notificationTemplateService.parseTemplate(patientTemplate, serviceParam);
        assertThat(actual, is(COMPLEX_TEST_SERVICE_EXPECTED_MESSAGE));
    }

    @Test
    public void parseTemplateForServiceShouldChangeServiceNameToLowerCase() {
        PatientTemplate patientTemplate = buildPatientTemplate(MIXED_LETTER_CASE_SERVICE_NAME);
        Map<String, String> serviceParam = buildServiceParams();
        String actual = notificationTemplateService.parseTemplate(patientTemplate, serviceParam);
        assertThat(actual, is(SIMPLE_TEST_SERVICE_EXPECTED_MESSAGE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseTemplateForServiceShouldThrowWhenServiceMapInWrongFormat() {
        Context.getAdministrationService().setGlobalProperty(ConfigConstants.NOTIFICATION_TEMPLATE_INJECTED_SERVICES,
                WRONG_INJECTED_SERVICES_PROP_VALUE);
        PatientTemplate patientTemplate = buildPatientTemplate(SIMPLE_TEST_SERVICE_NAME);
        Map<String, String> serviceParam = buildServiceParams();
        notificationTemplateService.parseTemplate(patientTemplate, serviceParam);
    }

    @Test
    public void parseTemplateForServiceShouldReturnNullWhenTemplateDefinitionNotExists() {
        PatientTemplate patientTemplate = buildPatientTemplate(NOT_EXISTING_SERVICE);
        Map<String, String> serviceParam = buildServiceParams();
        String actual = notificationTemplateService.parseTemplate(patientTemplate, serviceParam);
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void parseTemplateForServiceShouldReturnNullWhenPatientTemplateIsNull() {
        Map<String, String> serviceParam = buildServiceParams();
        String actual = notificationTemplateService.parseTemplate(null, serviceParam);
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void parseTemplateForServiceShouldReturnExpectedComplexMessageWhenServiceParamAreNull() {
        PatientTemplate patientTemplate = buildPatientTemplate(COMPLEX_TEST_SERVICE_NAME);
        String actual = notificationTemplateService.parseTemplate(patientTemplate, null);
        assertThat(actual, is(COMPLEX_TEST_SERVICE_EXPECTED_MESSAGE_WITHOUT_SERVICE_PARAM));
    }

    @Test
    public void parseTemplateForServiceShouldReturnExpectedComplexMessageWhenServiceParamAreEmpty() {
        PatientTemplate patientTemplate = buildPatientTemplate(COMPLEX_TEST_SERVICE_NAME);
        Map<String, String> serviceParam = new HashMap<>();
        String actual = notificationTemplateService.parseTemplate(patientTemplate, serviceParam);
        assertThat(actual, is(COMPLEX_TEST_SERVICE_EXPECTED_MESSAGE_WITHOUT_SERVICE_PARAM));
    }

    @Test
    public void parseTemplateForServiceShouldReturnNullIfMissingTemplate() {
        PatientTemplate patientTemplate = buildPatientTemplate(COMPLEX_TEST_SERVICE_NAME);
        patientTemplate.setTemplate(null);
        Map<String, String> serviceParam = buildServiceParams();
        String actual = notificationTemplateService.parseTemplate(patientTemplate, serviceParam);
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void parseTemplateForWhatsappServiceShouldReturnExpectedResult() {
        PatientTemplate patientTemplate = buildPatientTemplate(WHATSAPP_SERVICE_NAME);
        Map<String, String> serviceParam = buildServiceParams();
        String actual = notificationTemplateService.parseTemplate(patientTemplate, serviceParam);
        assertThat(actual, is(WHATSAPP_SERVICE_EXPECTED_TEMPLATE));
    }

    @Test
    public void shouldBuildMessageByGlobalProperty() {
        Map<String, Object> testParams = new HashMap<>();
        testParams.put("param1", "value1");
        testParams.put("param2", "value2");

        String actual = notificationTemplateService.buildMessageByGlobalProperty(testParams, "messages.notificationTemplate.test");

        assertNotNull(actual);
        assertEquals("message:\"This is test message.\"", actual);
    }

    private PatientTemplate buildPatientTemplate(String serviceName) {
        PatientTemplate patientTemplate = new PatientTemplate();
        patientTemplate.setPatient(new Patient(PATIENT_ID));
        Template template = new Template();
        template.setName(serviceName);
        patientTemplate.setTemplate(template);
        return patientTemplate;
    }

    private Map<String, String> buildServiceParams() {
        Map<String, String> serviceParam = new HashMap<>();
        serviceParam.put(SERVICE_TEST_PROP, SERVICE_PARAMETER);
        return serviceParam;
    }
}
