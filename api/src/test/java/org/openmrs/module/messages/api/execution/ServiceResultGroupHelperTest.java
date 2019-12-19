package org.openmrs.module.messages.api.execution;

import org.junit.Test;
import org.openmrs.module.messages.builder.DateBuilder;
import org.openmrs.module.messages.builder.ServiceResultBuilder;
import org.openmrs.module.messages.builder.ServiceResultListBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.openmrs.module.messages.api.execution.ServiceResultGroupHelper.groupByActorAndExecutionDate;

@SuppressWarnings("checkstyle:magicnumber")
public class ServiceResultGroupHelperTest {

    private static final int ACTOR_1_ID = 1;
    private static final int ACTOR_2_ID = 2;

    @Test
    public void shouldGroupByActor() {
        List<ServiceResultList> input = new ArrayList<>();
        Date date = getDate();

        input.add(new ServiceResultListBuilder().withServiceResults(1, date)
                .withActorId(ACTOR_1_ID).build());

        input.add(new ServiceResultListBuilder().withServiceResults(3, date)
                .withActorId(ACTOR_2_ID).build());

        List<GroupedServiceResultList> result = groupByActorAndExecutionDate(input);
        assertEquals(2, result.size());
        assertEquals(1, getResultsFor(result, ACTOR_1_ID, date).size());
        assertEquals(3, getResultsFor(result, ACTOR_2_ID, date).size());
    }

    @Test
    public void shouldGroupByExecutionDateWithAccuracyToSeconds() {
        List<ServiceResultList> input = new ArrayList<>();
        List<ServiceResult> results = new ArrayList<>();

        Date date = getDateWithSec(0);
        results.add(getResultWithDate(date));
        results.add(getResultWithDate(date));

        Date datePlusOneSec = getDateWithSec(1);
        results.add(getResultWithDate(datePlusOneSec));

        input.add(new ServiceResultListBuilder().withServiceResults(results)
                .withActorId(ACTOR_1_ID).build());

        List<GroupedServiceResultList> result = groupByActorAndExecutionDate(input);
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
        resultsForActor1.add(getResultWithDate(date));
        resultsForActor1.add(getResultWithDate(datePlusOneSec));
        input.add(new ServiceResultListBuilder().withServiceResults(resultsForActor1)
                .withActorId(ACTOR_1_ID).build());

        List<ServiceResult> resultsForActor2 = new ArrayList<>();
        resultsForActor2.add(getResultWithDate(date));
        resultsForActor2.add(getResultWithDate(date));
        resultsForActor2.add(getResultWithDate(datePlusOneSec));
        input.add(new ServiceResultListBuilder().withServiceResults(resultsForActor2)
                .withActorId(ACTOR_2_ID).build());

        List<GroupedServiceResultList> result = groupByActorAndExecutionDate(input);
        assertEquals(4, result.size());
        assertEquals(1, getResultsFor(result, ACTOR_1_ID, date).size());
        assertEquals(1, getResultsFor(result, ACTOR_1_ID, datePlusOneSec).size());
        assertEquals(2, getResultsFor(result, ACTOR_2_ID, date).size());
        assertEquals(1, getResultsFor(result, ACTOR_2_ID, datePlusOneSec).size());
    }

    @Test
    public void shouldConsiderWholeDateNotOnlyDaytime() {
        List<ServiceResultList> input = new ArrayList<>();
        List<ServiceResult> results = new ArrayList<>();

        Date date = getDateWithDay(0);
        results.add(getResultWithDate(date));

        Date datePlusOneDay = getDateWithDay(1);
        results.add(getResultWithDate(datePlusOneDay));
        results.add(getResultWithDate(datePlusOneDay));

        input.add(new ServiceResultListBuilder().withServiceResults(results)
                .withActorId(ACTOR_1_ID).build());

        List<GroupedServiceResultList> result = groupByActorAndExecutionDate(input);
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

        List<GroupedServiceResultList> result = groupByActorAndExecutionDate(input);
        assertEquals(0, result.size());
    }

    @Test
    public void allElementsShouldHaveTheSameDateAsTheGroupObject() {
        List<ServiceResultList> input = new ArrayList<>();
        Date date = getDate();

        input.add(new ServiceResultListBuilder().withServiceResults(3, date)
                .withActorId(ACTOR_1_ID).build());

        List<GroupedServiceResultList> result = groupByActorAndExecutionDate(input);
        assertEquals(1, result.size());
        assertEquals(3, result.get(0).getGroup().getResults().size());
        for (ServiceResult entry : getResultsFor(result, ACTOR_1_ID, date)) {
            assertEquals(date, entry.getExecutionDate());
        }
    }

    @Test
    public void allElementsShouldHaveTheSameActorAsTheGroupObject() {
        List<ServiceResultList> input = new ArrayList<>();
        Date date = getDate();

        input.add(new ServiceResultListBuilder().withServiceResults(3, date)
                .withActorId(ACTOR_1_ID).build());

        List<GroupedServiceResultList> result = groupByActorAndExecutionDate(input);
        assertEquals(1, result.size());
        assertEquals(3, result.get(0).getGroup().getResults().size());
        for (GroupedServiceResultList group : result) {
            assertEquals((int) group.getGroup().getActorId(), ACTOR_1_ID);
        }
    }

    private List<ServiceResult> getResultsFor(List<GroupedServiceResultList> input, int actorId, Date executionDate) {
        for (GroupedServiceResultList entry : input) {
            if (entry.getActorId().equals(actorId) && entry.getExecutionDate().equals(executionDate)) {
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

    private ServiceResult getResultWithDate(Date date) {
        return new ServiceResultBuilder().withExecutionDate(date).build();
    }
}
