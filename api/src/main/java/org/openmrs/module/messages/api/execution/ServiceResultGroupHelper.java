package org.openmrs.module.messages.api.execution;

import org.apache.commons.lang3.time.DateUtils;
import org.openmrs.module.messages.api.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class ServiceResultGroupHelper {

    public static List<ServiceResultList> groupByActorIdAndExecutionDate(List<ServiceResultList> input,
                                                                         int groupingPeriod) {
        List<ServiceResultList> groups = new ArrayList<>();
        for (ServiceResultList resultList : input) {
            List<ServiceResult> allResults = resultList.getResults();
            List<ServiceResult> gropedResults = new ArrayList<>();
            for (ServiceResult ignored : allResults) {
                ServiceResultList group = ServiceResultList.withEmptyResults(resultList);
                List<ServiceResult> notAssignedResults = getNotAssignedResults(gropedResults, allResults);
                if (notAssignedResults.isEmpty()) {
                    break;
                }
                Date earliestDate = getEarliestDate(notAssignedResults);
                List<ServiceResult> resultsBeforeDate = getResultsNotAfterDate(notAssignedResults,
                        DateUtils.addSeconds(earliestDate, groupingPeriod));
                gropedResults.addAll(resultsBeforeDate);
                group.setResults(resultsBeforeDate);
                groups.add(group);
            }
        }

        return groups;
    }

    public static Date getEarliestDate(List<ServiceResult> results) {
        Date min = results.get(0).getExecutionDate();
        for (ServiceResult result : results) {
            if (result.getExecutionDate().before(min)) {
                min = result.getExecutionDate();
            }
        }
        return min;
    }

    private static List<ServiceResult> getNotAssignedResults(List<ServiceResult> alreadyAssigned,
                                                             List<ServiceResult> all) {
        List<ServiceResult> result = new ArrayList<>();

        for (ServiceResult serviceResult : all) {
            if (!alreadyAssigned.contains(serviceResult)) {
                result.add(serviceResult);
            }
        }

        return result;
    }

    private static List<ServiceResult> getResultsNotAfterDate(List<ServiceResult> input, Date date) {
        List<ServiceResult> result = new ArrayList<>();
        for (ServiceResult entry : input) {
            if (DateUtil.isNotAfter(entry.getExecutionDate(), date)) {
                result.add(entry);
            }
        }
        return result;
    }

    private ServiceResultGroupHelper() {
    }
}
