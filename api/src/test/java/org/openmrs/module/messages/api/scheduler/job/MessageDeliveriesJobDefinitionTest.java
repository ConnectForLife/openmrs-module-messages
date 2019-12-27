package org.openmrs.module.messages.api.scheduler.job;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.messages.ContextSensitiveTest;
import org.openmrs.module.messages.api.execution.ActorWithDate;
import org.openmrs.module.messages.api.execution.GroupedServiceResultList;
import org.openmrs.module.messages.api.execution.ServiceResultList;
import org.openmrs.module.messages.api.mappers.ScheduledGroupMapper;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.model.ScheduledServiceParameter;
import org.openmrs.module.messages.api.model.ScheduledServicesExecutionContext;
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
import org.openmrs.scheduler.TaskDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.openmrs.module.messages.api.service.DatasetConstants.DEFAULT_CAREGIVER_ID;
import static org.openmrs.module.messages.api.service.DatasetConstants.DEFAULT_CAREGIVER_PATIENT_TEMPLATE_ID;
import static org.openmrs.module.messages.api.service.DatasetConstants.DEFAULT_NO_CONSENT_CAREGIVER_ID;
import static org.openmrs.module.messages.api.service.DatasetConstants.DEFAULT_PATIENT_ID;
import static org.openmrs.module.messages.api.service.DatasetConstants.DEFAULT_PATIENT_TEMPLATE_ID;
import static org.openmrs.module.messages.api.service.DatasetConstants.DEFAULT_TEMPLATE;
import static org.openmrs.module.messages.api.service.DatasetConstants.DEFAULT_TEMPLATE_NAME;
import static org.openmrs.module.messages.api.service.DatasetConstants.XML_DATA_SET_PATH;

@SuppressWarnings("checkstyle:magicnumber")
public class MessageDeliveriesJobDefinitionTest extends ContextSensitiveTest {

    private static final long DAILY = 3600L * 24;
    private static final String EMPTY_RESULT_SQL = "SELECT * WHERE 1=0";
    private static final String SERVICE_QUERY_WITH_REQUIRED_PARAMS = "select CAST('2019-12-01' as date) as EXECUTION_DATE"
            + ", '1' as MESSAGE_ID, 'CALL' as CHANNEL_ID, 'FUTURE' as STATUS_ID";
    private static final String SERVICE_QUERY_WITH_ADDITIONAL_PARAMS = SERVICE_QUERY_WITH_REQUIRED_PARAMS
            + ", 1 as intParam"
            + ", 1.23 as floatParam"
            + ", 'testString' as stringParam";

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

    @Test
    public void shouldBeExecutedWithoutAnyException() {
        job.execute();
    }

    @Test
    public void shouldSaveScheduledServicesGroupWithServicesForPatient() {
        ScheduledService expectedSmsService = getSmsService(DEFAULT_PATIENT_TEMPLATE_ID);
        ScheduledService expectedCallService = getCallService(DEFAULT_PATIENT_TEMPLATE_ID);

        List<ScheduledService> listBeforeSave = findScheduledServicesByActorId(DEFAULT_PATIENT_ID);
        job.execute();
        List<ScheduledService> newlySaved = getNewlyAddedObjects(
                listBeforeSave, findScheduledServicesByActorId(DEFAULT_PATIENT_ID));

        assertEquals(2, newlySaved.size());
        assertServiceIsCorrect(expectedSmsService, newlySaved.get(0));
        assertServiceIsCorrect(expectedCallService, newlySaved.get(1));
    }

    @Test
    public void shouldSaveScheduledServicesGroupWithServicesForCaregiver() {
        ScheduledService expectedSmsService = getSmsService(DEFAULT_CAREGIVER_PATIENT_TEMPLATE_ID);
        ScheduledService expectedCallService = getCallService(DEFAULT_CAREGIVER_PATIENT_TEMPLATE_ID);

        List<ScheduledService> listBeforeSave = findScheduledServicesByActorId(DEFAULT_CAREGIVER_ID);
        job.execute();
        List<ScheduledService> newlySaved = getNewlyAddedObjects(
                listBeforeSave, findScheduledServicesByActorId(DEFAULT_CAREGIVER_ID));

        assertEquals(2, newlySaved.size());
        assertServiceIsCorrect(expectedSmsService, newlySaved.get(0));
        assertServiceIsCorrect(expectedCallService, newlySaved.get(1));
    }

    @Test
    public void shouldNotSaveScheduledServicesGroupForActorWithNoConsent() {
        List<ScheduledService> listBeforeSave = findScheduledServicesByActorId(DEFAULT_NO_CONSENT_CAREGIVER_ID);
        job.execute();
        List<ScheduledService> newlySaved = getNewlyAddedObjects(
                listBeforeSave, findScheduledServicesByActorId(DEFAULT_NO_CONSENT_CAREGIVER_ID));

        assertEquals(0, newlySaved.size());
    }

    @Test
    public void shouldNotSaveScheduledServiceGroupIfNoTasksToSchedule() {
        defaultTemplate.setServiceQuery(EMPTY_RESULT_SQL);
        templateService.saveOrUpdateTemplate(defaultTemplate);

        List<ScheduledServiceGroup> listBeforeSave = messagingGroupService.getAll(false);
        job.execute();
        List<ScheduledServiceGroup> newlySaved = getNewlyAddedObjects(
                listBeforeSave, messagingGroupService.getAll(false));

        assertEquals(0, newlySaved.size());
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
        List<ScheduledServiceParameter> newlySaved = getNewlyAddedObjects(
                listBeforeSave, messagingParameterService.getAll(false));

        assertEquals(6, newlySaved.size()); // for 2 active actors
        assertParameterIsCorrect(expectedParam1, newlySaved.get(0));
        assertParameterIsCorrect(expectedParam2, newlySaved.get(1));
        assertParameterIsCorrect(expectedParam3, newlySaved.get(2));
        assertParameterIsCorrect(expectedParam1, newlySaved.get(3));
        assertParameterIsCorrect(expectedParam2, newlySaved.get(4));
        assertParameterIsCorrect(expectedParam3, newlySaved.get(5));
    }

    private List<ScheduledService> findScheduledServicesByActorId(int actorId) {
        return messagingService.findAllByCriteria(ScheduledServiceCriteria.forActorId(actorId));
    }

    private ScheduledServicesExecutionContext getExecutionContext(Date date, ServiceResultList resultList) {
        GroupedServiceResultList groupResults = new GroupedServiceResultList(
                new ActorWithDate(DEFAULT_PATIENT_ID, date), resultList);
        ScheduledServiceGroup group = groupMapper.fromDto(groupResults);
        group = messagingGroupService.saveOrUpdate(group);
        return new ScheduledServicesExecutionContext(
                group.getScheduledServices(),
                groupResults.getActorWithExecutionDate().getDate(),
                group.getActor());
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

    private ScheduledService getCallService(int patientTemplateId) {
        return getService("CALL", patientTemplateId);
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
