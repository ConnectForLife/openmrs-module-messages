package org.openmrs.module.messages.api.execution;

import org.junit.Test;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.module.messages.builder.DateBuilder;
import org.openmrs.module.messages.builder.ServiceResultBuilder;
import org.openmrs.module.messages.builder.ServiceResultListBuilder;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.openmrs.module.messages.api.execution.ServiceResultGroupHelper.groupByChannelTypePatientActorExecutionDate;

public class ServiceResultGroupHelperTest {

    private static final int ACTOR_1_ID = 1;
    private static final int ACTOR_2_ID = 2;
    private static final int PATIENT_1_ID = 5;
    private static final int PATIENT_TEMPLATE_1_ID = 1;
    private static final int PATIENT_TEMPLATE_2_ID = 2;
    private static final int THREE_SERVICES = 3;
    private static final int ONE_SERVICE = 1;
    private static final int EXPECTED_ONE = 1;
    private static final int EXPECTED_TWO = 2;
    private static final int EXPECTED_THREE = 3;
    private static final int EXPECTED_FOUR = 4;
    private static final String TEST_CHANNEL_TYPE = "Test";

    @Test
    public void shouldGroupByActor() {
        List<ServiceResultList> input = new ArrayList<>();
        ZonedDateTime date = getDate();

        input.add(new ServiceResultListBuilder()
                .withChannelType(TEST_CHANNEL_TYPE)
                .withActorId(ACTOR_1_ID)
                .withServiceResults(ONE_SERVICE, date)
                .build());

        input.add(new ServiceResultListBuilder()
                .withChannelType(TEST_CHANNEL_TYPE)
                .withActorId(ACTOR_2_ID)
                .withServiceResults(THREE_SERVICES, date)
                .build());

        List<GroupedServiceResultList> result = groupByChannelTypePatientActorExecutionDate(input, false);
        assertEquals(EXPECTED_TWO, result.size());
        assertEquals(EXPECTED_ONE, getResultsFor(result, ACTOR_1_ID, date).size());
        assertEquals(EXPECTED_THREE, getResultsFor(result, ACTOR_2_ID, date).size());
    }

    @Test
    public void shouldGetOnlyFutureServicesWhenFlagIsSet() {
        ZonedDateTime date = getDate();

        List<ServiceResultList> input = getServiceResultListsWithOneFutureAndOnePendingStatus(date, ACTOR_1_ID);

        List<GroupedServiceResultList> result = groupByChannelTypePatientActorExecutionDate(input, true);
        assertEquals(1, result.size());
        assertEquals(1, getResultsFor(result, ACTOR_1_ID, date).size());
    }

    @Test
    public void shouldFilterOnlyFutureServices() {
        List<ServiceResultList> input = getServiceResultListsOnlyWithFutureStatuses(ACTOR_2_ID);

        List<GroupedServiceResultList> actual = groupByChannelTypePatientActorExecutionDate(input, true);

        assertNotNull(actual);
        assertEquals(4, actual.get(0).getGroup().size());
    }

    @Test
    public void shouldNotFilterServicesWhenGetOnlyFutureServicesFlagIsNotSet() {
        ZonedDateTime date = getDate();

        List<ServiceResultList> input = getServiceResultListsWithOneFutureAndOnePendingStatus(date, ACTOR_1_ID);

        List<GroupedServiceResultList> result = groupByChannelTypePatientActorExecutionDate(input, false);
        assertEquals(1, result.size());
        assertEquals(2, getResultsFor(result, ACTOR_1_ID, date).size());
    }

    @Test
    public void shouldGroupByExecutionDateWithAccuracyToSeconds() {
        List<ServiceResultList> input = new ArrayList<>();
        List<ServiceResult> results = new ArrayList<>();

        ZonedDateTime date = getDateWithSec(0);
        results.add(getResultWithDate(date, PATIENT_TEMPLATE_1_ID, ACTOR_1_ID));
        results.add(getResultWithDate(date, PATIENT_TEMPLATE_1_ID, ACTOR_1_ID));

        ZonedDateTime datePlusOneSec = getDateWithSec(1);
        results.add(getResultWithDate(datePlusOneSec, PATIENT_TEMPLATE_1_ID, ACTOR_1_ID));

        input.add(new ServiceResultListBuilder()
                .withChannelType(TEST_CHANNEL_TYPE)
                .withServiceResults(results)
                .withActorId(ACTOR_1_ID)
                .build());

        List<GroupedServiceResultList> result = groupByChannelTypePatientActorExecutionDate(input, false);
        assertEquals(2, result.size());
        assertEquals(2, getResultsFor(result, ACTOR_1_ID, date).size());
        assertEquals(1, getResultsFor(result, ACTOR_1_ID, datePlusOneSec).size());
    }

    @Test
    public void shouldGroupByActorAndExecutionDate() {
        List<ServiceResultList> input = new ArrayList<>();
        ZonedDateTime date = getDateWithSec(0);
        ZonedDateTime datePlusOneSec = getDateWithSec(1);

        List<ServiceResult> resultsForActor1 = new ArrayList<>();
        resultsForActor1.add(getResultWithDate(date, PATIENT_TEMPLATE_1_ID, ACTOR_1_ID));
        resultsForActor1.add(getResultWithDate(datePlusOneSec, PATIENT_TEMPLATE_1_ID, ACTOR_1_ID));
        input.add(new ServiceResultListBuilder()
                .withChannelType(TEST_CHANNEL_TYPE)
                .withServiceResults(resultsForActor1)
                .withActorId(ACTOR_1_ID)
                .build());

        List<ServiceResult> resultsForActor2 = new ArrayList<>();
        resultsForActor2.add(getResultWithDate(date, PATIENT_TEMPLATE_1_ID, ACTOR_2_ID));
        resultsForActor2.add(getResultWithDate(date, PATIENT_TEMPLATE_1_ID, ACTOR_2_ID));
        resultsForActor2.add(getResultWithDate(datePlusOneSec, PATIENT_TEMPLATE_1_ID, ACTOR_2_ID));
        input.add(new ServiceResultListBuilder()
                .withChannelType(TEST_CHANNEL_TYPE)
                .withServiceResults(resultsForActor2)
                .withActorId(ACTOR_2_ID)
                .build());

        List<GroupedServiceResultList> result = groupByChannelTypePatientActorExecutionDate(input, false);
        assertEquals(EXPECTED_FOUR, result.size());
        assertEquals(EXPECTED_ONE, getResultsFor(result, ACTOR_1_ID, date).size());
        assertEquals(EXPECTED_ONE, getResultsFor(result, ACTOR_1_ID, datePlusOneSec).size());
        assertEquals(EXPECTED_TWO, getResultsFor(result, ACTOR_2_ID, date).size());
        assertEquals(EXPECTED_ONE, getResultsFor(result, ACTOR_2_ID, datePlusOneSec).size());
    }

    @Test
    public void shouldConsiderWholeDateNotOnlyDaytime() {
        List<ServiceResultList> input = new ArrayList<>();
        List<ServiceResult> results = new ArrayList<>();

        ZonedDateTime date = getDateWithDay(1);
        results.add(getResultWithDate(date, PATIENT_TEMPLATE_1_ID, ACTOR_1_ID));

        ZonedDateTime datePlusOneDay = getDateWithDay(2);
        results.add(getResultWithDate(datePlusOneDay, PATIENT_TEMPLATE_1_ID, ACTOR_1_ID));
        results.add(getResultWithDate(datePlusOneDay, PATIENT_TEMPLATE_1_ID, ACTOR_1_ID));

        input.add(new ServiceResultListBuilder()
                .withChannelType(TEST_CHANNEL_TYPE)
                .withServiceResults(results)
                .withActorId(ACTOR_1_ID)
                .build());

        List<GroupedServiceResultList> result = groupByChannelTypePatientActorExecutionDate(input, false);
        assertEquals(2, result.size());
        assertEquals(1, getResultsFor(result, ACTOR_1_ID, date).size());
        assertEquals(2, getResultsFor(result, ACTOR_1_ID, datePlusOneDay).size());
    }

    @Test
    public void shouldNotCreateGroupForEmptyResultsList() {
        List<ServiceResultList> input = new ArrayList<>();
        List<ServiceResult> emptyResults = new ArrayList<>();

        input.add(new ServiceResultListBuilder()
                .withChannelType(TEST_CHANNEL_TYPE)
                .withServiceResults(emptyResults)
                .withActorId(ACTOR_1_ID)
                .build());
        input.add(new ServiceResultListBuilder()
                .withChannelType(TEST_CHANNEL_TYPE)
                .withServiceResults(emptyResults)
                .withActorId(ACTOR_2_ID)
                .build());

        List<GroupedServiceResultList> result = groupByChannelTypePatientActorExecutionDate(input, false);
        assertEquals(0, result.size());
    }

    @Test
    public void allElementsShouldHaveTheSameDateAsTheGroupObject() {
        List<ServiceResultList> input = new ArrayList<>();
        ZonedDateTime date = getDate();

        input.add(new ServiceResultListBuilder()
                .withChannelType(TEST_CHANNEL_TYPE)
                .withServiceResults(THREE_SERVICES, date)
                .withActorId(ACTOR_1_ID)
                .build());

        List<GroupedServiceResultList> result = groupByChannelTypePatientActorExecutionDate(input, false);
        assertEquals(EXPECTED_ONE, result.size());
        assertEquals(EXPECTED_THREE, result.get(0).getGroup().size());
        for (ServiceResult entry : getResultsFor(result, ACTOR_1_ID, date)) {
            assertEquals(date, entry.getExecutionDate());
        }
    }

    @Test
    public void allElementsShouldHaveTheSameActorAsTheGroupObject() {
        List<ServiceResultList> input = new ArrayList<>();
        ZonedDateTime date = getDate();

        input.add(new ServiceResultListBuilder()
                .withChannelType(TEST_CHANNEL_TYPE)
                .withActorId(ACTOR_1_ID)
                .withServiceResults(THREE_SERVICES, date)
                .build());

        List<GroupedServiceResultList> result = groupByChannelTypePatientActorExecutionDate(input, false);
        assertEquals(EXPECTED_ONE, result.size());
        assertEquals(EXPECTED_THREE, result.get(0).getGroup().size());
        for (GroupedServiceResultList group : result) {
            assertEquals((int) group.getKey().getActorId(), ACTOR_1_ID);
        }
    }

    @Test
    public void shouldGroupDifferentMessagesForTheSameActor() {
        List<ServiceResultList> input = new ArrayList<>();
        ZonedDateTime date = getDate();

        input.add(new ServiceResultListBuilder()
                .withChannelType(TEST_CHANNEL_TYPE)
                .withServiceResults(wrap(getResultWithDate(date, PATIENT_TEMPLATE_1_ID, ACTOR_1_ID)))
                .withActorId(ACTOR_1_ID)
                .withServiceName("Health Tip")
                .build());
        input.add(new ServiceResultListBuilder()
                .withChannelType(TEST_CHANNEL_TYPE)
                .withServiceResults(wrap(getResultWithDate(date, PATIENT_TEMPLATE_2_ID, ACTOR_1_ID)))
                .withActorId(ACTOR_1_ID)
                .withServiceName("Adherence Daily")
                .build());

        List<GroupedServiceResultList> result = groupByChannelTypePatientActorExecutionDate(input, false);
        assertEquals(1, result.size());
        assertEquals(2, getResultsFor(result, ACTOR_1_ID, date).size());
    }

    @Test
    public void shouldGroupByActorWithProperActorType() {
        List<ServiceResultList> input = new ArrayList<>();
        ZonedDateTime date = getDate();

        input.add(new ServiceResultListBuilder()
                .withChannelType(TEST_CHANNEL_TYPE)
                .withServiceResults(ONE_SERVICE, date)
                .withActorId(ACTOR_1_ID)
                .withPatientId(PATIENT_1_ID)
                .build());

        input.add(new ServiceResultListBuilder()
                .withChannelType(TEST_CHANNEL_TYPE)
                .withServiceResults(THREE_SERVICES, date)
                .withActorId(ACTOR_2_ID)
                .withPatientId(ACTOR_2_ID)
                .build());

        List<GroupedServiceResultList> result = groupByChannelTypePatientActorExecutionDate(input, false);
        assertEquals(2, result.size());
        for (GroupedServiceResultList group : result) {
            GroupedServiceResultListKey actorWithDate = group.getKey();
            if ("Caregiver".equals(actorWithDate.getActorType())) {
                assertEquals(Integer.valueOf(ACTOR_1_ID), actorWithDate.getActorId());
                assertEquals(Integer.valueOf(PATIENT_1_ID), actorWithDate.getPatientId());
            } else {
                assertEquals(Integer.valueOf(ACTOR_2_ID), actorWithDate.getActorId());
                assertEquals(Integer.valueOf(ACTOR_2_ID), actorWithDate.getPatientId());
            }
        }
    }

    private List<ServiceResultList> getServiceResultListsWithOneFutureAndOnePendingStatus(ZonedDateTime date, int actorId) {
        List<ServiceResultList> input = new ArrayList<>();

        input.add(new ServiceResultListBuilder()
                .withChannelType(TEST_CHANNEL_TYPE)
                .withServiceResults(2, date)
                .withActorId(actorId)
                .build());

        input.get(0).getResults().get(0).setServiceStatus(ServiceStatus.PENDING);
        input.get(0).getResults().get(1).setServiceStatus(ServiceStatus.FUTURE);
        return input;
    }

    private List<ServiceResultList> getServiceResultListsOnlyWithFutureStatuses(int actorId) {
        List<ServiceResultList> input = new ArrayList<>();

        input.add(new ServiceResultListBuilder()
                .withChannelType(TEST_CHANNEL_TYPE)
                .withServiceResults(4, getDate())
                .withActorId(actorId)
                .build());

        input.get(0).getResults().get(0).setServiceStatus(ServiceStatus.FUTURE);
        input.get(0).getResults().get(1).setServiceStatus(ServiceStatus.FUTURE);
        input.get(0).getResults().get(2).setServiceStatus(ServiceStatus.FUTURE);
        input.get(0).getResults().get(3).setServiceStatus(ServiceStatus.FUTURE);
        return input;
    }

    private List<ServiceResult> getResultsFor(List<GroupedServiceResultList> input, int actorId, ZonedDateTime executionDate) {
        for (GroupedServiceResultList entry : input) {
            if (entry.getKey().getActorId().equals(actorId) && entry.getKey().getDate().isEqual(executionDate)) {
                return mapToServiceResult(entry.getGroup());
            }
        }
        return new ArrayList<>();
    }

    private List<ServiceResult> mapToServiceResult(Collection<GroupedServiceResult> groupedServiceResults) {
        final List<ServiceResult> result = new ArrayList<>();
        for (GroupedServiceResult groupedServiceResult : groupedServiceResults) {
            result.add(groupedServiceResult.getServiceResult());
        }
        return result;
    }

    private ZonedDateTime getDateWithDay(int day) {
        return new DateBuilder().withDay(day).build();
    }

    private ZonedDateTime getDateWithSec(int sec) {
        return new DateBuilder().withSec(sec).build();
    }

    private ZonedDateTime getDate() {
        return new DateBuilder().build();
    }

    private ServiceResult getResultWithDate(ZonedDateTime date, Integer patientTemplateId, Integer actorAndPatientId) {
        return new ServiceResultBuilder()
                .withActorId(actorAndPatientId)
                .withPatientId(actorAndPatientId)
                .withChannelType(TEST_CHANNEL_TYPE)
                .withExecutionDate(date)
                .withPatientTemplate(patientTemplateId)
                .build();
    }

    private <T> List<T> wrap(T toWrap) {
        ArrayList<T> list = new ArrayList<>();
        list.add(toWrap);
        return list;
    }
}
