package org.openmrs.module.messages.api.execution;

import org.junit.Test;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.module.messages.builder.DateBuilder;
import org.openmrs.module.messages.builder.ServiceResultBuilder;
import org.openmrs.module.messages.builder.ServiceResultListBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.openmrs.module.messages.api.execution.ServiceResultGroupHelper.groupByActorAndExecutionDate;

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

    @Test
    public void shouldGroupByActor() {
        List<ServiceResultList> input = new ArrayList<>();
        Date date = getDate();

        input.add(new ServiceResultListBuilder().withServiceResults(ONE_SERVICE, date)
                .withActorId(ACTOR_1_ID).build());

        input.add(new ServiceResultListBuilder().withServiceResults(THREE_SERVICES, date)
                .withActorId(ACTOR_2_ID).build());

        List<GroupedServiceResultList> result = groupByActorAndExecutionDate(input, false);
        assertEquals(EXPECTED_TWO, result.size());
        assertEquals(EXPECTED_ONE, getResultsFor(result, ACTOR_1_ID, date).size());
        assertEquals(EXPECTED_THREE, getResultsFor(result, ACTOR_2_ID, date).size());
    }

    @Test
    public void shouldGetOnlyFutureServicesWhenFlagIsSet() {
        Date date = getDate();

        List<ServiceResultList> input = getServiceResultListsWithOneFutureAndOnePendingStatus(date, ACTOR_1_ID);

        List<GroupedServiceResultList> result = groupByActorAndExecutionDate(input, true);
        assertEquals(1, result.size());
        assertEquals(1, getResultsFor(result, ACTOR_1_ID, date).size());
    }

    @Test
    public void shouldNotFilterServicesWhenGetOnlyFutureServicesFlagIsNotSet() {
        Date date = getDate();

        List<ServiceResultList> input = getServiceResultListsWithOneFutureAndOnePendingStatus(date, ACTOR_1_ID);

        List<GroupedServiceResultList> result = groupByActorAndExecutionDate(input, false);
        assertEquals(1, result.size());
        assertEquals(2, getResultsFor(result, ACTOR_1_ID, date).size());
    }

    @Test
    public void shouldGroupByExecutionDateWithAccuracyToSeconds() {
        List<ServiceResultList> input = new ArrayList<>();
        List<ServiceResult> results = new ArrayList<>();

        Date date = getDateWithSec(0);
        results.add(getResultWithDate(date, PATIENT_TEMPLATE_1_ID));
        results.add(getResultWithDate(date, PATIENT_TEMPLATE_1_ID));

        Date datePlusOneSec = getDateWithSec(1);
        results.add(getResultWithDate(datePlusOneSec, PATIENT_TEMPLATE_1_ID));

        input.add(new ServiceResultListBuilder().withServiceResults(results)
                .withActorId(ACTOR_1_ID).build());

        List<GroupedServiceResultList> result = groupByActorAndExecutionDate(input, false);
        assertEquals(2, result.size());
        assertEquals(2, getResultsFor(result, ACTOR_1_ID, date).size());
        assertEquals(1, getResultsFor(result, ACTOR_1_ID, datePlusOneSec).size());
    }

    @Test
    public void shouldGroupByActorAndExecutionDate() {
        List<ServiceResultList> input = new ArrayList<>();
        Date date = getDateWithSec(0);
        Date datePlusOneSec = getDateWithSec(1);

        List<ServiceResult> resultsForActor1 = new ArrayList<>();
        resultsForActor1.add(getResultWithDate(date, PATIENT_TEMPLATE_1_ID));
        resultsForActor1.add(getResultWithDate(datePlusOneSec, PATIENT_TEMPLATE_1_ID));
        input.add(new ServiceResultListBuilder().withServiceResults(resultsForActor1)
                .withActorId(ACTOR_1_ID).build());

        List<ServiceResult> resultsForActor2 = new ArrayList<>();
        resultsForActor2.add(getResultWithDate(date, PATIENT_TEMPLATE_1_ID));
        resultsForActor2.add(getResultWithDate(date, PATIENT_TEMPLATE_1_ID));
        resultsForActor2.add(getResultWithDate(datePlusOneSec, PATIENT_TEMPLATE_1_ID));
        input.add(new ServiceResultListBuilder().withServiceResults(resultsForActor2)
                .withActorId(ACTOR_2_ID).build());

        List<GroupedServiceResultList> result = groupByActorAndExecutionDate(input, false);
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

        Date date = getDateWithDay(0);
        results.add(getResultWithDate(date, PATIENT_TEMPLATE_1_ID));

        Date datePlusOneDay = getDateWithDay(1);
        results.add(getResultWithDate(datePlusOneDay, PATIENT_TEMPLATE_1_ID));
        results.add(getResultWithDate(datePlusOneDay, PATIENT_TEMPLATE_1_ID));

        input.add(new ServiceResultListBuilder().withServiceResults(results)
                .withActorId(ACTOR_1_ID).build());

        List<GroupedServiceResultList> result = groupByActorAndExecutionDate(input, false);
        assertEquals(2, result.size());
        assertEquals(1, getResultsFor(result, ACTOR_1_ID, date).size());
        assertEquals(2, getResultsFor(result, ACTOR_1_ID, datePlusOneDay).size());
    }

    @Test
    public void shouldNotCreateGroupForEmptyResultsList() {
        List<ServiceResultList> input = new ArrayList<>();
        List<ServiceResult> emptyResults = new ArrayList<>();

        input.add(new ServiceResultListBuilder().withServiceResults(emptyResults)
                .withActorId(ACTOR_1_ID).build());
        input.add(new ServiceResultListBuilder().withServiceResults(emptyResults)
                .withActorId(ACTOR_2_ID).build());

        List<GroupedServiceResultList> result = groupByActorAndExecutionDate(input, false);
        assertEquals(0, result.size());
    }

    @Test
    public void allElementsShouldHaveTheSameDateAsTheGroupObject() {
        List<ServiceResultList> input = new ArrayList<>();
        Date date = getDate();

        input.add(new ServiceResultListBuilder().withServiceResults(THREE_SERVICES, date)
                .withActorId(ACTOR_1_ID).build());

        List<GroupedServiceResultList> result = groupByActorAndExecutionDate(input, false);
        assertEquals(EXPECTED_ONE, result.size());
        assertEquals(EXPECTED_THREE, result.get(0).getGroup().getResults().size());
        for (ServiceResult entry : getResultsFor(result, ACTOR_1_ID, date)) {
            assertEquals(date, entry.getExecutionDate());
        }
    }

    @Test
    public void allElementsShouldHaveTheSameActorAsTheGroupObject() {
        List<ServiceResultList> input = new ArrayList<>();
        Date date = getDate();

        input.add(new ServiceResultListBuilder().withServiceResults(THREE_SERVICES, date)
                .withActorId(ACTOR_1_ID).build());

        List<GroupedServiceResultList> result = groupByActorAndExecutionDate(input, false);
        assertEquals(EXPECTED_ONE, result.size());
        assertEquals(EXPECTED_THREE, result.get(0).getGroup().getResults().size());
        for (GroupedServiceResultList group : result) {
            assertEquals((int) group.getGroup().getActorId(), ACTOR_1_ID);
        }
    }

    @Test
    public void shouldGroupDifferentMessagesForTheSameActor() {
        List<ServiceResultList> input = new ArrayList<>();
        Date date = getDate();

        input.add(new ServiceResultListBuilder()
                .withServiceResults(wrap(getResultWithDate(date, PATIENT_TEMPLATE_1_ID)))
                .withActorId(ACTOR_1_ID)
                .withServiceName("Health Tip")
                .build());
        input.add(new ServiceResultListBuilder()
                .withServiceResults(wrap(getResultWithDate(date, PATIENT_TEMPLATE_2_ID)))
                .withActorId(ACTOR_1_ID)
                .withServiceName("Adherence Daily")
                .build());

        List<GroupedServiceResultList> result = groupByActorAndExecutionDate(input, false);
        assertEquals(1, result.size());
        assertEquals(2, getResultsFor(result, ACTOR_1_ID, date).size());
    }

    @Test
    public void shouldGroupByActorWithProperActorType() {
        List<ServiceResultList> input = new ArrayList<>();
        Date date = getDate();

        input.add(new ServiceResultListBuilder().withServiceResults(ONE_SERVICE, date)
                .withActorId(ACTOR_1_ID)
                .withPatientId(PATIENT_1_ID)
                .build());

        input.add(new ServiceResultListBuilder().withServiceResults(THREE_SERVICES, date)
                .withActorId(ACTOR_2_ID)
                .withPatientId(ACTOR_2_ID)
                .build());

        List<GroupedServiceResultList> result = groupByActorAndExecutionDate(input, false);
        assertEquals(2, result.size());
        assertEquals("Caregiver", result.get(0).getActorWithExecutionDate().getActorType());
        assertEquals("Patient", result.get(1).getActorWithExecutionDate().getActorType());
    }

    private List<ServiceResultList> getServiceResultListsWithOneFutureAndOnePendingStatus(Date date, int actorId) {
        List<ServiceResultList> input = new ArrayList<>();

        input.add(new ServiceResultListBuilder().withServiceResults(2, date)
                .withActorId(actorId).build());

        input.get(0).getResults().get(0).setServiceStatus(ServiceStatus.PENDING);
        input.get(0).getResults().get(1).setServiceStatus(ServiceStatus.FUTURE);
        return input;
    }

    private List<ServiceResult> getResultsFor(List<GroupedServiceResultList> input, int actorId, Date executionDate) {
        for (GroupedServiceResultList entry : input) {
            if (entry.getActorWithExecutionDate().getActorId().equals(actorId)
                    && entry.getActorWithExecutionDate().getDate().equals(executionDate)) {
                return entry.getGroup().getResults();
            }
        }
        return new ArrayList<>();
    }

    private Date getDateWithDay(int day) {
        return new DateBuilder().withDay(day).build();
    }

    private Date getDateWithSec(int sec) {
        return new DateBuilder().withSec(sec).build();
    }

    private Date getDate() {
        return new DateBuilder().build();
    }

    private ServiceResult getResultWithDate(Date date, Integer patientTemplateId) {
        return new ServiceResultBuilder()
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
