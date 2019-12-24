package org.openmrs.module.messages.api.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.openmrs.api.PersonService;
import org.openmrs.module.messages.ContextSensitiveTest;
import org.openmrs.module.messages.api.execution.ChannelType;
import org.openmrs.module.messages.api.execution.GroupedServiceResultList;
import org.openmrs.module.messages.api.execution.ServiceResult;
import org.openmrs.module.messages.api.execution.ServiceResultList;
import org.openmrs.module.messages.api.model.Range;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.model.ScheduledServiceParameter;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.module.messages.api.util.MapperUtil;
import org.openmrs.module.messages.builder.DateBuilder;
import org.openmrs.module.messages.builder.PatientTemplateBuilder;
import org.openmrs.module.messages.builder.ScheduledServiceBuilder;
import org.openmrs.module.messages.builder.ScheduledServiceGroupBuilder;
import org.openmrs.module.messages.builder.ScheduledServiceParameterBuilder;
import org.openmrs.module.messages.builder.ServiceResultBuilder;
import org.openmrs.module.messages.builder.ServiceResultListBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.openmrs.module.messages.api.execution.ChannelType.CALL;
import static org.openmrs.module.messages.api.execution.ChannelType.SMS;
import static org.openmrs.module.messages.api.service.DatasetConstants.DEFAULT_CAREGIVER_ID;
import static org.openmrs.module.messages.api.service.DatasetConstants.DEFAULT_PATIENT_ID;
import static org.openmrs.module.messages.api.service.DatasetConstants.DEFAULT_PATIENT_TEMPLATE_ID;
import static org.openmrs.module.messages.api.service.DatasetConstants.DEFAULT_PERSON_ID;
import static org.openmrs.module.messages.api.service.DatasetConstants.XML_DATA_SET_PATH;

@SuppressWarnings("checkstyle:magicnumber")
public class MessagesDeliveryServiceITTest extends ContextSensitiveTest {

    @Autowired
    @InjectMocks
    @Qualifier("messages.deliveryService")
    private MessagesDeliveryService deliveryService;

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
    @Qualifier("personService")
    private PersonService personService;

    @Before
    public void setUp() throws Exception {
        executeDataSet(XML_DATA_SET_PATH + "ConceptDataSet.xml");
        executeDataSet(XML_DATA_SET_PATH + "MessageDataSet.xml");
    }

    @Test
    public void shouldSaveScheduledServices() {
        Date date = new DateBuilder().build();
        List<ServiceResult> results = new ArrayList<>();
        results.add(getServiceResult(date, SMS));
        results.add(getServiceResult(date, CALL));
        ServiceResultList resultList = getDefaultPersonResultList(results);

        GroupedServiceResultList group = new GroupedServiceResultList(DEFAULT_PATIENT_ID, date, resultList);
        List<ScheduledService> listBeforeSave = messagingService.getAll(false);
        deliveryService.schedulerDelivery(group);

        ScheduledService expectedSmsService = getSmsService();
        ScheduledService expectedCallService = getCallService();
        List<ScheduledService> newlySaved = getNewlyAddedObjects(
                listBeforeSave, messagingService.getAll(false));
        assertEquals(2, newlySaved.size());
        assertServiceIsCorrect(expectedSmsService, newlySaved.get(0));
        assertServiceIsCorrect(expectedCallService, newlySaved.get(1));
    }

    @Test
    public void shouldSaveScheduledServiceGroupForPatient() {
        ServiceResultList resultList = getDefaultPatientResultList();
        Date date = new DateBuilder().build();
        GroupedServiceResultList group = new GroupedServiceResultList(DEFAULT_PATIENT_ID, date, resultList);

        List<ScheduledServiceGroup> listBeforeSave = messagingGroupService.getAll(false);
        deliveryService.schedulerDelivery(group);

        ScheduledServiceGroup expectedPatientGroup = getPatientServiceGroup(date);
        List<ScheduledServiceGroup> newlySaved = getNewlyAddedObjects(
                listBeforeSave, messagingGroupService.getAll(false));
        assertEquals(1, newlySaved.size());
        assertGroupIsCorrect(expectedPatientGroup, newlySaved.get(0));
    }

    @Test
    public void shouldSaveScheduledServiceGroupForCaregiver() {
        ServiceResultList resultList = getDefaultCaregiverResultList();
        Date date = new DateBuilder().build();
        GroupedServiceResultList group = new GroupedServiceResultList(DEFAULT_CAREGIVER_ID, date, resultList);

        List<ScheduledServiceGroup> listBeforeSave = messagingGroupService.getAll(false);
        deliveryService.schedulerDelivery(group);

        ScheduledServiceGroup expectedPatientGroup = getCaregiverServiceGroup(date);
        List<ScheduledServiceGroup> newlySaved = getNewlyAddedObjects(
                listBeforeSave, messagingGroupService.getAll(false));
        assertEquals(1, newlySaved.size());
        assertGroupIsCorrect(expectedPatientGroup, newlySaved.get(0));
    }

    @Test
    public void shouldSaveScheduledParameters() {
        Date date = new DateBuilder().build();
        List<ServiceResult> results = new ArrayList<>();
        Map<String, Object> params1 = new HashMap<>();
        params1.put("1", "one");
        List<String> listObject = new ArrayList<>();
        listObject.add("two");
        params1.put("2", listObject);
        Map<String, Object> params2 = new HashMap<>();
        Range<Integer> rangeObject = new Range<>(10, 30);
        params2.put("3", rangeObject);
        results.add(getServiceResult(date, SMS, params1));
        results.add(getServiceResult(date, CALL, params2));
        ServiceResultList resultList = getDefaultPersonResultList(results);

        GroupedServiceResultList group = new GroupedServiceResultList(DEFAULT_PATIENT_ID, date, resultList);
        List<ScheduledServiceParameter> listBeforeSave = messagingParameterService.getAll(false);
        deliveryService.schedulerDelivery(group);

        ScheduledServiceParameter expectedParam1 = getParam("1", "one");
        ScheduledServiceParameter expectedParam2 = getParam("2", "[two]");
        ScheduledServiceParameter expectedParam3 = getParam("3", "{start=10.0, end=30.0}");
        List<ScheduledServiceParameter> newlySaved = getNewlyAddedObjects(
                listBeforeSave, messagingParameterService.getAll(false));
        assertEquals(3, newlySaved.size());
        assertParameterIsCorrect(expectedParam1, newlySaved.get(0));
        assertParameterIsCorrect(expectedParam2, newlySaved.get(1));
        assertParameterIsCorrect(expectedParam3, newlySaved.get(2));
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

    private ScheduledService getCallService() {
        return getService("CALL");
    }

    private ScheduledService getSmsService() {
        return getService("SMS");
    }

    private ScheduledService getService(String channelType) {
        return new ScheduledServiceBuilder()
                .withService(ServiceResultListBuilder.DUMMY_SERVICE_NAME)
                .withTemplate(new PatientTemplateBuilder().withId(DEFAULT_PATIENT_TEMPLATE_ID).build())
                .withChannelType(channelType)
                .withStatus(ServiceStatus.PENDING)
                .build();
    }

    private ServiceResult getServiceResult(Date date, ChannelType channelType) {
        return getServiceResult(date, channelType, new HashMap<>());
    }

    private ServiceResult getServiceResult(Date date, ChannelType channelType, Map<String, Object> additionalParams) {
        return new ServiceResultBuilder().withExecutionDate(date)
                .withPatientTemplate(DEFAULT_PATIENT_TEMPLATE_ID)
                .withParams(additionalParams)
                .withChannelType(channelType).build();
    }

    private ServiceResultList getDefaultPersonResultList(List<ServiceResult> results) {
        return getResultList(results, DEFAULT_PERSON_ID, DEFAULT_PERSON_ID);
    }

    private ServiceResultList getDefaultPatientResultList() {
        return getResultList(new ArrayList<>(), DEFAULT_PATIENT_ID, DEFAULT_PATIENT_ID);
    }

    private ServiceResultList getDefaultCaregiverResultList() {
        return getResultList(new ArrayList<>(), DEFAULT_CAREGIVER_ID, DEFAULT_PATIENT_ID);
    }

    private ServiceResultList getResultList(List<ServiceResult> results, Integer actorId, Integer patientId) {
        return new ServiceResultListBuilder()
                .withActorId(actorId).withPatientId(patientId)
                .withServiceName(ServiceResultListBuilder.DUMMY_SERVICE_NAME)
                .withServiceResults(results)
                .build();
    }

    private ScheduledServiceGroup getCaregiverServiceGroup(Date date) {
        return getServiceGroup(date, DEFAULT_CAREGIVER_ID);
    }

    private ScheduledServiceGroup getPatientServiceGroup(Date date) {
        return getServiceGroup(date, DEFAULT_PATIENT_ID);
    }

    private ScheduledServiceGroup getServiceGroup(Date date, Integer actorId) {
        return new ScheduledServiceGroupBuilder()
                .withActorId(actorId)
                .withPatientId(DEFAULT_PATIENT_ID)
                .withMsgSendTime(date)
                .withStatus(ServiceStatus.PENDING)
                .withScheduledServices(new ArrayList<>())
                .build();
    }

    private void assertParameterIsCorrect(ScheduledServiceParameter expected, ScheduledServiceParameter actual) {
        assertEquals(expected.getParameterType(), actual.getParameterType());
        assertEquals(expected.getParameterValue(),
                MapperUtil.getGson().fromJson(actual.getParameterValue(), Object.class).toString());
        assertNotNull(actual.getScheduledMessage());
    }

    private void assertServiceIsCorrect(ScheduledService expected, ScheduledService actual) {
        assertEquals(expected.getService(), actual.getService());
        assertEquals(expected.getPatientTemplate().getId(), actual.getPatientTemplate().getId());
        assertEquals(expected.getChannelType(), actual.getChannelType());
        assertEquals(expected.getStatus(), actual.getStatus());
        assertNotNull(actual.getGroup());
    }

    private void assertGroupIsCorrect(ScheduledServiceGroup expected, ScheduledServiceGroup actual) {
        assertEquals(expected.getPatient().getId(), actual.getPatient().getId());
        assertEquals(expected.getActor().getId(), actual.getActor().getId());
        assertEquals(expected.getMsgSendTime(), actual.getMsgSendTime());
        assertEquals(expected.getScheduledServices(), actual.getScheduledServices());
        assertEquals(expected.getStatus(), actual.getStatus());
    }
}
