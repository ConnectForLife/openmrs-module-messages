package org.openmrs.module.messages.api.execution;

import org.junit.Test;
import org.openmrs.module.messages.builder.DateBuilder;
import org.openmrs.module.messages.builder.ServiceResultBuilder;
import org.openmrs.module.messages.builder.ServiceResultListBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.openmrs.module.messages.api.execution.ServiceResultGroupHelper.groupByActorIdAndExecutionDate;

@SuppressWarnings("checkstyle:magicnumber")
public class ServiceResultGroupHelperTest {

    private static final int ACTOR_1_ID = 1;
    private static final int ACTOR_2_ID = 2;

    private static final int FIVE_MINUTES_IN_SECONDS = 5 * 60;
    private static final int TWO_HOURS_IN_SECONDS = 2 * 60 * 60;

    @Test
    public void shouldNotExpandDateForGroup() {
        List<ServiceResultList> input = new ArrayList<>();
        List<ServiceResult> results = new ArrayList<>();

        //should be in group 1 because it's the earliest date
        results.add(getResultWithMin(0));

        //should be in group 1 because it's 4m later
        results.add(getResultWithMin(4));

        //should be in group 2 because it's more than 5m after the beginning of the group 1
        results.add(getResultWithMin(6));

        input.add(new ServiceResultListBuilder()
                .withActorId(ACTOR_1_ID)
                .withServiceResults(results)
                .build());

        List<ServiceResultList> result = groupByActorIdAndExecutionDate(input, FIVE_MINUTES_IN_SECONDS);
        assertEquals(2, result.size());
        assertEquals(2, result.get(0).getResults().size());
        assertEquals(1, result.get(1).getResults().size());
    }

    @Test
    public void shouldGroupWithAccuracyToSeconds() {
        List<ServiceResultList> input = new ArrayList<>();
        List<ServiceResult> results = new ArrayList<>();

        //should be in group 1 because it's the earliest date
        results.add(getResultWithMin(0));

        //should be in group 1 because it's exactly 5m later
        results.add(getResultWithMin(5));

        //should be in group 2 because it's at least 1s more after 5m of the beginning of the group 1
        results.add(getResultWithMinAndSec(5, 1));

        input.add(new ServiceResultListBuilder()
                .withActorId(ACTOR_1_ID)
                .withServiceResults(results)
                .build());

        List<ServiceResultList> result = groupByActorIdAndExecutionDate(input, FIVE_MINUTES_IN_SECONDS);
        assertEquals(2, result.size());
        assertEquals(2, result.get(0).getResults().size());
        assertEquals(1, result.get(1).getResults().size());
    }

    @Test
    public void shouldApplyNextResultsToFollowingGroups() {
        List<ServiceResultList> input = new ArrayList<>();
        List<ServiceResult> results = new ArrayList<>();

        //should be in group 1 because it's the earliest date
        results.add(getResultWithHrs(0));

        //should be in group 1 because it's 1h later
        results.add(getResultWithHrs(2));

        //should be in group 2 because it's more than 2h after the beginning of the group 1
        results.add(getResultWithHrs(6));

        //should be in group 2 because it's less than 2h after the beginning of the group 2
        results.add(getResultWithHrs(7));
        
        //should be in group 3 because it's less more than 2h after the beginning of the group 2
        results.add(getResultWithHrs(9));

        input.add(new ServiceResultListBuilder()
                .withActorId(ACTOR_1_ID)
                .withServiceResults(results)
                .build());

        List<ServiceResultList> result = groupByActorIdAndExecutionDate(input, TWO_HOURS_IN_SECONDS);
        assertEquals(3, result.size());
        assertEquals(2, result.get(0).getResults().size());
        assertEquals(2, result.get(1).getResults().size());
        assertEquals(1, result.get(2).getResults().size());
    }
    
    @Test
    public void shouldCreateSeparateGroupsForTheSameResultsButDifferentActors() {
        Date sameDate = new DateBuilder().build();

        List<ServiceResultList> input = new ArrayList<>();
        List<ServiceResult> results = new ArrayList<>();
        results.add(new ServiceResultBuilder().withExecutionDate(sameDate).build());

        ServiceResultList listForActor1 = new ServiceResultListBuilder()
                .withServiceResults(results)
                .withActorId(ACTOR_1_ID).build();
        input.add(listForActor1);

        ServiceResultList listForActor2 = new ServiceResultListBuilder()
                .withServiceResults(results)
                .withActorId(ACTOR_2_ID).build();
        input.add(listForActor2);

        List<ServiceResultList> result = groupByActorIdAndExecutionDate(input, FIVE_MINUTES_IN_SECONDS);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getResults().size());
        assertEquals(1, result.get(1).getResults().size());
    }

    @Test
    public void shouldConsiderWholeDateNotOnlyDaytime() {
        List<ServiceResultList> input = new ArrayList<>();
        List<ServiceResult> results = new ArrayList<>();

        results.add(new ServiceResultBuilder().withExecutionDate(
                new DateBuilder().build()).build());

        results.add(new ServiceResultBuilder().withExecutionDate(
                new DateBuilder().withDate(DateBuilder.DEFAULT_DATE + 1).build()).build());

        results.add(new ServiceResultBuilder().withExecutionDate(
                new DateBuilder().withMonth(DateBuilder.DEFAULT_MONTH + 1).build()).build());

        results.add(new ServiceResultBuilder().withExecutionDate(
                new DateBuilder().withYear(DateBuilder.DEFAULT_YEAR + 1).build()).build());

        ServiceResultList resultList = new ServiceResultListBuilder()
                .withServiceResults(results)
                .withActorId(ACTOR_1_ID).build();
        input.add(resultList);

        List<ServiceResultList> result = groupByActorIdAndExecutionDate(input, FIVE_MINUTES_IN_SECONDS);
        assertEquals(4, result.size());
        assertEquals(1, result.get(0).getResults().size());
        assertEquals(1, result.get(1).getResults().size());
        assertEquals(1, result.get(2).getResults().size());
        assertEquals(1, result.get(3).getResults().size());
    }

    private ServiceResult getResultWithHrs(int hrs) {
        return new ServiceResultBuilder().withExecutionDate(
                new DateBuilder().withHrs(hrs).build()).build();
    }
    
    private ServiceResult getResultWithMin(int min) {
        return new ServiceResultBuilder().withExecutionDate(
                new DateBuilder().withMin(min).build()).build();
    }
    
    private ServiceResult getResultWithMinAndSec(int min, int sec) {
        return new ServiceResultBuilder().withExecutionDate(
                new DateBuilder().withMin(min).withSec(sec).build()).build();
    }
}
