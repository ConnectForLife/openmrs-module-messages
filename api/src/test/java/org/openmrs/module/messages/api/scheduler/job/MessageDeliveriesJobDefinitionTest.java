/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.scheduler.job;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.messages.api.mappers.ScheduledGroupMapper;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.model.ScheduledServiceParameter;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.module.messages.api.service.MessagingGroupService;
import org.openmrs.module.messages.api.service.MessagingParameterService;
import org.openmrs.module.messages.api.service.MessagingService;
import org.openmrs.module.messages.api.service.TemplateService;
import org.openmrs.module.messages.builder.PatientTemplateBuilder;
import org.openmrs.module.messages.builder.ScheduledServiceBuilder;
import org.openmrs.module.messages.builder.ScheduledServiceParameterBuilder;
import org.openmrs.module.messages.domain.criteria.ScheduledServiceCriteria;
import org.openmrs.scheduler.SchedulerService;
import org.openmrs.scheduler.TaskDefinition;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.openmrs.module.messages.api.service.DatasetConstants.DEFAULT_CAREGIVER_ID;
import static org.openmrs.module.messages.api.service.DatasetConstants.DEFAULT_CAREGIVER_PATIENT_TEMPLATE_ID;
import static org.openmrs.module.messages.api.service.DatasetConstants.DEFAULT_INACTIVE_PATIENT_ID;
import static org.openmrs.module.messages.api.service.DatasetConstants.DEFAULT_NO_CONSENT_CAREGIVER_ID;
import static org.openmrs.module.messages.api.service.DatasetConstants.DEFAULT_PATIENT_ID;
import static org.openmrs.module.messages.api.service.DatasetConstants.DEFAULT_PATIENT_TEMPLATE_ID;
import static org.openmrs.module.messages.api.service.DatasetConstants.DEFAULT_TEMPLATE;
import static org.openmrs.module.messages.api.service.DatasetConstants.DEFAULT_TEMPLATE_NAME;
import static org.openmrs.module.messages.api.service.DatasetConstants.XML_DATA_SET_PATH;

@ContextConfiguration(
        locations = {"classpath:applicationContext-service.xml", "classpath*:moduleApplicationContext.xml",
                "classpath*:CustomSchedulerServiceApplicationContext.xml"}, inheritLocations = false)
public class MessageDeliveriesJobDefinitionTest extends BaseModuleContextSensitiveTest {

    private static final long DAILY = 3600L * 24;
    private static final String EMPTY_RESULT_SQL = "SELECT * WHERE 1=0";
    private static final String SERVICE_QUERY_WITH_REQUIRED_PARAMS = "select CAST('2019-12-01' as date) as EXECUTION_DATE"
            + ", '1' as MESSAGE_ID, 'Call' as CHANNEL_ID, 'FUTURE' as STATUS_ID";
    private static final String SERVICE_QUERY_WITH_ADDITIONAL_PARAMS = SERVICE_QUERY_WITH_REQUIRED_PARAMS
            + ", 1 as intParam"
            + ", 1.23 as floatParam"
            + ", 'testString' as stringParam";
    private static final int EXPECTED_THREE = 3;
    private static final int CAREGIVER_PATIENT_ACTOR = 674;
    private static final String EXECUTION_CONTEXT = "EXECUTION_CONTEXT";

    @Autowired
    @Qualifier("messages.templateService")
    private TemplateService templateService;

    @Autowired
    @Qualifier("messages.messagingService")
    private MessagingService messagingService;

    @Autowired
    @Qualifier("messages.messagingGroupService")
    private MessagingGroupService messagingGroupService;

    @Autowired
    @Qualifier("messages.messagingParameterService")
    private MessagingParameterService messagingParameterService;

    @Autowired
    @Qualifier("messages.scheduledGroupMapper")
    private ScheduledGroupMapper groupMapper;

    @Autowired
    @Qualifier("schedulerService")
    private SchedulerService schedulerService;

    private MessageDeliveriesJobDefinition job = new MessageDeliveriesJobDefinition();
    private Template defaultTemplate;

    @Before
    public void setUp() throws Exception {
        executeDataSet(XML_DATA_SET_PATH + "ConceptDataSet.xml");
        executeDataSet(XML_DATA_SET_PATH + "MessageDataSet.xml");

        TaskDefinition taskDefinition = new TaskDefinition();
        taskDefinition.setRepeatInterval(DAILY);
        job.initialize(taskDefinition);

        defaultTemplate = templateService.getById(DEFAULT_TEMPLATE);
    }

    @After
    public void cleanUp() {
        schedulerService.onShutdown();
    }

    @Test(expected = Test.None.class /* no exception expected */)
    public void shouldBeExecutedWithoutAnyException() {
        job.execute();
    }

    @Test
    public void shouldSaveScheduledServicesGroupWithServicesForPatient() {
        ScheduledService expectedSmsService = getSmsService(DEFAULT_PATIENT_TEMPLATE_ID);
        ScheduledService expectedCallService = getCallService(DEFAULT_PATIENT_TEMPLATE_ID);

        List<ScheduledService> listBeforeSave = findScheduledServicesByDefaultPatientAndActorId(DEFAULT_PATIENT_ID);
        job.execute();
        List<ScheduledService> newlySaved = getNewlyAddedObjects(
                listBeforeSave, findScheduledServicesByDefaultPatientAndActorId(DEFAULT_PATIENT_ID));

        assertEquals(6, newlySaved.size());
        assertServiceIsCorrect(expectedSmsService, newlySaved.get(4));
        assertServiceIsCorrect(expectedCallService, newlySaved.get(5));
    }

    @Test
    public void shouldSaveScheduledServicesGroupWithServicesForCaregiver() {
        ScheduledService expectedSmsService = getSmsService(DEFAULT_CAREGIVER_PATIENT_TEMPLATE_ID);
        ScheduledService expectedCallService = getCallService(DEFAULT_CAREGIVER_PATIENT_TEMPLATE_ID);

        List<ScheduledService> listBeforeSave = findScheduledServicesByDefaultPatientAndActorId(DEFAULT_CAREGIVER_ID);
        job.execute();
        List<ScheduledService> newlySaved = getNewlyAddedObjects(
                listBeforeSave, findScheduledServicesByDefaultPatientAndActorId(DEFAULT_CAREGIVER_ID));

        assertEquals(2, newlySaved.size());
        assertServiceIsCorrect(expectedSmsService, newlySaved.get(0));
        assertServiceIsCorrect(expectedCallService, newlySaved.get(1));
    }

    @Test
    public void shouldNotSaveScheduledServicesGroupWithServicesForCaregiverWhenPatientIsNotActive() throws Exception {
        executeDataSet(XML_DATA_SET_PATH + "ConfigDataset.xml"); // loading GP which enables consent control

        List<ScheduledService> listBeforeSave = findScheduledServicesByPatientIdAndActorId(DEFAULT_CAREGIVER_ID,
                DEFAULT_INACTIVE_PATIENT_ID);
        job.execute();
        List<ScheduledService> newlySaved = getNewlyAddedObjects(
                listBeforeSave, findScheduledServicesByPatientIdAndActorId(DEFAULT_CAREGIVER_ID,
                        DEFAULT_INACTIVE_PATIENT_ID));

        assertEquals(0, getServicesForPatient(newlySaved, DEFAULT_INACTIVE_PATIENT_ID).size());
    }

    @Test
    public void shouldSaveScheduledServicesGroupForActorWithNoConsentWhenConsentControlNotEnabled() {
        Context.getAdministrationService().setGlobalProperty(ConfigConstants.CONSENT_CONTROL_KEY, "false");
        List<ScheduledService> listBeforeSave = findScheduledServicesByDefaultPatientAndActorId(
                DEFAULT_NO_CONSENT_CAREGIVER_ID);
        job.execute();
        List<ScheduledService> newlySaved = getNewlyAddedObjects(
                listBeforeSave, findScheduledServicesByDefaultPatientAndActorId(
                        DEFAULT_NO_CONSENT_CAREGIVER_ID));

        assertThat(newlySaved.size(), greaterThan(0));
    }

    @Test
    public void shouldNotSaveScheduledServicesGroupForActorWithNoConsentWhenConsentControlIsEnabled() throws Exception {
        executeDataSet(XML_DATA_SET_PATH + "ConfigDataset.xml"); // loading GP which enables consent control

        List<ScheduledService> listBeforeSave = findScheduledServicesByDefaultPatientAndActorId(
                DEFAULT_NO_CONSENT_CAREGIVER_ID);
        job.execute();
        List<ScheduledService> newlySaved = getNewlyAddedObjects(
                listBeforeSave, findScheduledServicesByDefaultPatientAndActorId(
                        DEFAULT_NO_CONSENT_CAREGIVER_ID));

        assertEquals(0, newlySaved.size());
    }

    @Test
    public void shouldNotSaveScheduledServiceGroupIfNoTasksToSchedule() {
        defaultTemplate.setServiceQuery(EMPTY_RESULT_SQL);
        defaultTemplate = templateService.saveOrUpdateTemplate(defaultTemplate);

        List<ScheduledServiceGroup> listBeforeSave = messagingGroupService.getAll(false);
        job.execute();
        List<ScheduledServiceGroup> newlySaved = filterByDefaultPatient(
                getNewlyAddedObjects(listBeforeSave, messagingGroupService.getAll(false)));

        assertEquals(1, newlySaved.size());
    }

    @Test
    public void shouldSaveScheduledParametersForSqlQuery() {
        ScheduledServiceParameter expectedParam1 = getParam("FLOATPARAM", "1.23");
        ScheduledServiceParameter expectedParam2 = getParam("INTPARAM", "1");
        ScheduledServiceParameter expectedParam3 = getParam("STRINGPARAM", "testString");

        defaultTemplate.setServiceQuery(SERVICE_QUERY_WITH_ADDITIONAL_PARAMS);
        templateService.saveOrUpdateTemplate(defaultTemplate);

        List<ScheduledServiceParameter> listBeforeSave = messagingParameterService.getAll(false);
        job.execute();
        List<ScheduledServiceParameter> newlySaved = filterByDefaultPatientTemplate(
                getNewlyAddedObjects(listBeforeSave, messagingParameterService.getAll(false)));

        assertEquals(EXPECTED_THREE, newlySaved.size());
        assertParameterIsCorrect(expectedParam1, newlySaved.get(0));
        assertParameterIsCorrect(expectedParam2, newlySaved.get(1));
        assertParameterIsCorrect(expectedParam3, newlySaved.get(2));
    }

    @Test
    public void shouldSaveTwoScheduledServicesForCaregiver() {
        List<ScheduledService> listServicesBeforeSave =
                findScheduledServicesByDefaultPatientAndActorId(CAREGIVER_PATIENT_ACTOR);
        List<TaskDefinition> listTasksBeforeSave = getScheduledTaskForActor(CAREGIVER_PATIENT_ACTOR);
        job.execute();
        List<ScheduledService> newlySaved = getNewlyAddedObjects(
                listServicesBeforeSave, findScheduledServicesByActorId(CAREGIVER_PATIENT_ACTOR));
        assertEquals(EXPECTED_THREE, newlySaved.size());

        //verify if each scheduled service group has own scheduled task
        List<TaskDefinition> newlySavedTasks = getNewlyAddedObjects(listTasksBeforeSave,
                getScheduledTaskForActor(CAREGIVER_PATIENT_ACTOR));
        assertEquals(newlySaved.size(), newlySavedTasks.size());
    }

    private List<ScheduledService> findScheduledServicesByDefaultPatientAndActorId(int actorId) {
        return messagingService.findAllByCriteria(
                ScheduledServiceCriteria.forActorAndPatientIds(actorId, DEFAULT_PATIENT_ID));
    }

    private List<ScheduledService> findScheduledServicesByPatientIdAndActorId(int actorId, int patientId) {
        return messagingService.findAllByCriteria(
                ScheduledServiceCriteria.forActorAndPatientIds(actorId, patientId));
    }

    private List<ScheduledService> findScheduledServicesByActorId(int actorId) {
        return messagingService.findAllByCriteria(
                ScheduledServiceCriteria.forActorAndPatientIds(actorId, null));
    }

    private List<TaskDefinition> getScheduledTaskForActor(int actorId) {
        List<TaskDefinition> tasks = new ArrayList<>();
        for (TaskDefinition t : schedulerService.getRegisteredTasks()) {
            if (t.getProperty(EXECUTION_CONTEXT).contains(String.format("\"actorId\":%d", actorId))) {
                tasks.add(t);
            }
        }
        return tasks;
    }

    private ScheduledServiceParameter getParam(String key, String value) {
        return new ScheduledServiceParameterBuilder()
                .withType(key)
                .withValue(value)
                .build();
    }

    private <T> List<T> getNewlyAddedObjects(List<T> listBeforeSave, List<T> listAfterSave) {
        List<T> result = new ArrayList<>();

        for (T object : listAfterSave) {
            if (!listBeforeSave.contains(object)) {
                result.add(object);
            }
        }

        return result;
    }

    private List<ScheduledServiceParameter> filterByDefaultPatientTemplate(List<ScheduledServiceParameter> params) {
        List<ScheduledServiceParameter> result = new ArrayList<>();

        for (ScheduledServiceParameter param : params) {
            if (param.getScheduledMessage().getPatientTemplate().getId().equals(DEFAULT_PATIENT_TEMPLATE_ID)) {
                result.add(param);
            }
        }

        return result;
    }

    private List<ScheduledServiceGroup> filterByDefaultPatient(List<ScheduledServiceGroup> groups) {
        List<ScheduledServiceGroup> result = new ArrayList<>();

        for (ScheduledServiceGroup group : groups) {
            if (group.getActor().getId().equals(DEFAULT_PATIENT_ID)
                    && group.getPatient().getId().equals(DEFAULT_PATIENT_ID)) {
                result.add(group);
            }
        }

        return result;
    }

    private List<ScheduledService> getServicesForPatient(List<ScheduledService> services, int patientId) {
        return services.stream()
                .filter(s -> s.getGroup()
                        .getPatient()
                        .getId()
                        .equals(patientId))
                .collect(Collectors.toList());
    }

    private ScheduledService getCallService(int patientTemplateId) {
        return getService("Call", patientTemplateId);
    }

    private ScheduledService getSmsService(int patientTemplateId) {
        return getService("SMS", patientTemplateId);
    }

    private ScheduledService getService(String channelType, int patientTemplateId) {
        return new ScheduledServiceBuilder()
                .withService(DEFAULT_TEMPLATE_NAME)
                .withTemplate(new PatientTemplateBuilder().withId(patientTemplateId).build())
                .withChannelType(channelType)
                .withStatus(ServiceStatus.PENDING)
                .build();
    }

    private void assertParameterIsCorrect(ScheduledServiceParameter expected, ScheduledServiceParameter actual) {
        assertEquals(expected.getParameterType(), actual.getParameterType());
        assertEquals(expected.getParameterValue(), actual.getParameterValue());
        assertNotNull(actual.getScheduledMessage());
    }

    private void assertServiceIsCorrect(ScheduledService expected, ScheduledService actual) {
        assertEquals(expected.getService(), actual.getService());
        assertEquals(expected.getPatientTemplate().getId(), actual.getPatientTemplate().getId());
        assertEquals(expected.getChannelType(), actual.getChannelType());
        assertEquals(expected.getStatus(), actual.getStatus());
        assertNotNull(actual.getGroup());
    }
}
