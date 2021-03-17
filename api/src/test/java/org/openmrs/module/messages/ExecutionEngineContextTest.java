/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.PersonName;
import org.openmrs.api.LocationService;
import org.openmrs.api.PatientService;
import org.openmrs.module.messages.api.execution.ExecutionException;
import org.openmrs.module.messages.api.execution.ServiceExecutor;
import org.openmrs.module.messages.api.execution.ServiceResult;
import org.openmrs.module.messages.api.execution.ServiceResultList;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Range;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.model.TemplateFieldType;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.openmrs.module.messages.api.service.TemplateFieldService;
import org.openmrs.module.messages.api.service.TemplateService;
import org.openmrs.module.messages.api.util.EndDateType;
import org.openmrs.module.messages.builder.TemplateFieldBuilder;
import org.openmrs.module.messages.builder.TemplateFieldValueBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.openmrs.module.messages.TestUtil.getMaxTimeForDate;
import static org.openmrs.module.messages.TestUtil.getMinTimeForDate;
import static org.openmrs.module.messages.api.model.TemplateFieldType.END_OF_MESSAGES;
import static org.openmrs.module.messages.api.model.TemplateFieldType.MESSAGING_FREQUENCY_DAILY_OR_WEEKLY_OR_MONTHLY;
import static org.openmrs.module.messages.api.model.TemplateFieldType.START_OF_MESSAGES;

public class ExecutionEngineContextTest extends ContextSensitiveTest {

    private static final Date START_DATE = DateUtils.addYears(new Date(), -2);
    private static final Date END_DATE = DateUtils.addYears(new Date(), 2);

    private static final int YEAR_2020 = 2020;
    private static final int YEAR_2019 = 2019;
    private static final Date DECEMBER_15_2019 = getMinTimeForDate(YEAR_2019, Calendar.DECEMBER, 15);
    private static final String DECEMBER_15_2019_TXT = "2019-12-15";
    private static final Date JANUARY_10 = getMaxTimeForDate(YEAR_2020, Calendar.JANUARY, 10);
    private static final String JANUARY_10_TXT = "2020-01-10";
    private static final Date JANUARY_13 = getMinTimeForDate(YEAR_2020, Calendar.JANUARY, 13);
    private static final String JANUARY_13_TXT = "2020-01-13";

    private static final String SERVICE_NAME = "Service Name";
    // drop mili precision for H2 testing purposes
    private static final Date BIRTH_DATE = DateUtils.setMilliseconds(DateUtils.addYears(new Date(), -1), 0);

    @Autowired
    @Qualifier("messages.ServiceExecutor")
    private ServiceExecutor serviceExecutor;

    @Autowired
    private PatientService patientService;

    @Autowired
    @Qualifier("messages.patientTemplateService")
    private PatientTemplateService patientTemplateService;

    @Autowired
    @Qualifier("messages.templateFieldService")
    private TemplateFieldService templateFieldService;

    @Autowired
    @Qualifier("messages.templateService")
    private TemplateService templateService;

    @Autowired
    private LocationService locationService;

    @Test
    public void shouldSetupContext() throws ExecutionException {
        PatientTemplate patientTemplate = prepareData();
        Range<Date> dateRange = new Range<>(START_DATE, END_DATE);

        ServiceResultList serviceResultList = serviceExecutor.execute(patientTemplate, dateRange);

        assertEquals(patientTemplate.getPatient().getId(), serviceResultList.getPatientId());
        assertEquals(patientTemplate.getPatient().getId(), serviceResultList.getActorId());
        assertEquals(START_DATE, serviceResultList.getStartDate());
        assertEquals(END_DATE, serviceResultList.getEndDate());
        assertEquals(1, serviceResultList.getResults().size());
        assertEquals(SERVICE_NAME, serviceResultList.getServiceName());

        ServiceResult result = serviceResultList.getResults().get(0);
        assertEquals("msg", result.getMessageId());
        assertEquals(Constant.CHANNEL_TYPE_CALL, result.getChannelType());
        assertEquals(ServiceStatus.FUTURE, result.getServiceStatus());
        // query adds 1 year to the birth date
        assertEquals(DateUtils.addYears(BIRTH_DATE, 1).getTime(), result.getExecutionDate().getTime());

        assertEquals(1, result.getAdditionalParams().size());
        assertEquals("M", result.getAdditionalParams().get("GENDER"));
    }

    @Test
    public void shouldReturnPatientTemplateEndDateIfItIsBeforeRangeEndDate() throws ExecutionException {
        PatientTemplate patientTemplate = prepareData2(DECEMBER_15_2019_TXT, JANUARY_10_TXT);
        Range<Date> dateRange = new Range<>(DECEMBER_15_2019, JANUARY_13);

        ServiceResultList serviceResultList = serviceExecutor.execute(patientTemplate, dateRange);

        assertEquals(patientTemplate.getPatient().getId(), serviceResultList.getPatientId());
        assertEquals(patientTemplate.getPatient().getId(), serviceResultList.getActorId());
        assertEquals(DECEMBER_15_2019, serviceResultList.getStartDate());
        assertEquals(JANUARY_10, serviceResultList.getEndDate());
    }

    @Test
    public void shouldReturnRangeEndDateIfItIsBeforePatientTemplateEndDate() throws ExecutionException {
        PatientTemplate patientTemplate = prepareData2(DECEMBER_15_2019_TXT, JANUARY_13_TXT);
        Range<Date> dateRange = new Range<>(DECEMBER_15_2019, JANUARY_10);

        ServiceResultList serviceResultList = serviceExecutor.execute(patientTemplate, dateRange);

        assertEquals(patientTemplate.getPatient().getId(), serviceResultList.getPatientId());
        assertEquals(patientTemplate.getPatient().getId(), serviceResultList.getActorId());
        assertEquals(DECEMBER_15_2019, serviceResultList.getStartDate());
        assertEquals(JANUARY_10, serviceResultList.getEndDate());
    }

    private PatientTemplate prepareData() {
        PatientIdentifierType idType = new PatientIdentifierType();
        idType.setName("Test ID");
        idType = patientService.savePatientIdentifierType(idType);

        Patient patient = new Patient();
        patient.addName(new PersonName("Test", "John", "Boe"));
        patient.setGender("M");
        patient.setBirthdate(BIRTH_DATE);

        PatientIdentifier id = new PatientIdentifier("XXX", idType, locationService.getDefaultLocation());
        patient.addIdentifier(id);

        patient = patientService.savePatient(patient);

        Template template = new Template();
        template.setServiceQuery("SELECT DATEADD('YEAR', 1, per.birthdate) AS EXECUTION_DATE, 'msg' AS MESSAGE_ID, " +
                "'Call' AS CHANNEL_ID, per.gender AS GENDER " +
                "FROM patient p " +
                "JOIN person per ON per.person_id = p.patient_id  " +
                "WHERE per.birthdate > :startDateTime AND per.birthdate < :endDateTime");
        template.setCalendarServiceQuery("SELECT CALENDAR SERVICE QUERY");
        template.setServiceQueryType("SQL");
        template.setName(SERVICE_NAME);
        template.setShouldUseOptimizedQuery(false);
        template = templateService.saveOrUpdate(template);

        PatientTemplate patientTemplate = new PatientTemplate();
        patientTemplate.setPatient(patient);
        patientTemplate.setActor(patient);

        // TODO incorrect schema
        TemplateField templateField = new TemplateField();
        templateField.setTemplate(template);
        templateField.setMandatory(false);
        templateField.setName("This relation is wrong");
        templateField.setTemplateFieldType(TemplateFieldType.SERVICE_TYPE);

        TemplateFieldValue templateFieldValue = new TemplateFieldValue();
        templateFieldValue.setTemplateField(templateField);
        patientTemplate.setTemplate(template);

        return patientTemplateService.saveOrUpdate(patientTemplate);
    }

    private PatientTemplate prepareData2(String start, String end) {
        Template template = new Template();
        template.setServiceQuery("SELECT DATEADD('YEAR', 1, per.birthdate) AS EXECUTION_DATE, 'msg' AS MESSAGE_ID, " +
                "'Call' AS CHANNEL_ID, per.gender AS GENDER " +
                "FROM patient p " +
                "JOIN person per ON per.person_id = p.patient_id  " +
                "WHERE per.birthdate > :startDateTime AND per.birthdate < :startDateTime");
        template.setCalendarServiceQuery("SELECT CALENDAR SERVICE QUERY");
        template.setServiceQueryType("SQL");
        template.setName(SERVICE_NAME);
        template.setShouldUseOptimizedQuery(false);
        template = templateService.saveOrUpdate(template);

        PatientTemplate patientTemplate = prepareBase();

        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(MESSAGING_FREQUENCY_DAILY_OR_WEEKLY_OR_MONTHLY,
            "Daily",
                template, patientTemplate));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, start,
                template, patientTemplate));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES, EndDateType.DATE_PICKER.getName() + "|" + end,
                template, patientTemplate));

        patientTemplate.setTemplateFieldValues(values);
        patientTemplate.setTemplate(template);

        return patientTemplateService.saveOrUpdate(patientTemplate);
    }

    private TemplateFieldValue buildTemplateFieldWithValue(TemplateFieldType type,
                                                           String value,
                                                           Template template,
                                                           PatientTemplate patientTemplate) {
        TemplateField field = templateFieldService.saveOrUpdate(new TemplateFieldBuilder()
                .withTemplateFieldType(type)
                .withTemplate(template)
                .build());
        return new TemplateFieldValueBuilder()
                .withPatientTemplate(patientTemplate)
                .withTemplateField(field)
                .withValue(value)
                .build();
    }

    private PatientTemplate prepareBase() {
        PatientIdentifierType idType = new PatientIdentifierType();
        idType.setName("Test ID");
        idType = patientService.savePatientIdentifierType(idType);

        Patient patient = new Patient();
        patient.addName(new PersonName("Test", "John", "Boe"));
        patient.setGender("M");
        patient.setBirthdate(BIRTH_DATE);

        PatientIdentifier id = new PatientIdentifier("XXX", idType, locationService.getDefaultLocation());
        patient.addIdentifier(id);

        patient = patientService.savePatient(patient);

        PatientTemplate patientTemplate = new PatientTemplate();
        patientTemplate.setPatient(patient);
        patientTemplate.setActor(patient);

        return patientTemplate;
    }
}
