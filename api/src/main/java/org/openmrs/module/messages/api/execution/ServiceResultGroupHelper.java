/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.execution;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.module.messages.api.model.types.ServiceStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ServiceResultGroupHelper {

    private ServiceResultGroupHelper() {
    }

    public static List<GroupedServiceResultList> groupByChannelTypePatientActorExecutionDate(List<ServiceResultList> input,
                                                                                             boolean getOnlyFutureServices) {
        final List<ServiceResultList> serviceResultLists = getOnlyFutureServices ? filterOnlyFuture(input) : input;

        final Map<GroupedServiceResultListKey, List<GroupedServiceResult>> groupedLists =
                groupByResultListKey(serviceResultLists);

        final List<GroupedServiceResultList> result = new ArrayList<GroupedServiceResultList>();

        for (Map.Entry<GroupedServiceResultListKey, List<GroupedServiceResult>> serviceResultGroup :
                groupedLists.entrySet()) {
            if (!serviceResultGroup.getValue().isEmpty()) {
                result.add(new GroupedServiceResultList(serviceResultGroup.getKey(), serviceResultGroup.getValue()));
            }
        }

        return result;
    }

    private static List<ServiceResultList> filterOnlyFuture(List<ServiceResultList> input) {
        List<ServiceResultList> result = new ArrayList<ServiceResultList>();

        for (ServiceResultList srl : input) {
            result.add(ServiceResultList.withEmptyResults(srl).withResults(filterOnlyFutureResults(srl.getResults())));
        }

        return result;
    }

    private static List<ServiceResult> filterOnlyFutureResults(List<ServiceResult> serviceResults) {
        List<ServiceResult> result = new ArrayList<ServiceResult>();
        for (ServiceResult sr : serviceResults) {
            if (ServiceStatus.FUTURE.equals(sr.getServiceStatus())) {
                result.add(sr);
            }
        }
        return result;
    }

    private static Map<GroupedServiceResultListKey, List<GroupedServiceResult>> groupByResultListKey(
            final List<ServiceResultList> serviceResultLists) {
        final Map<GroupedServiceResultListKey, List<GroupedServiceResult>> groupedLists =
                new HashMap<GroupedServiceResultListKey, List<GroupedServiceResult>>();

        for (ServiceResultList serviceResultList : serviceResultLists) {
            if (serviceResultList.getResults().isEmpty()) {
                continue;
            }

            groupServiceResultList(serviceResultList, groupedLists);
        }

        return groupedLists;
    }

    private static void groupServiceResultList(ServiceResultList serviceResultList,
                                               Map<GroupedServiceResultListKey, List<GroupedServiceResult>> groupedLists) {

        for (ServiceResult serviceResult : serviceResultList.getResults()) {
            for (String singleChannelType : StringUtils.split(serviceResult.getChannelType(), ',')) {
                final GroupedServiceResultListKey currentServiceResultListKey =
                        new GroupedServiceResultListKey(singleChannelType, serviceResult, serviceResultList.getActorType());

                List<GroupedServiceResult> serviceResultGroup = groupedLists.get(currentServiceResultListKey);

                if (serviceResultGroup == null) {
                    serviceResultGroup = new ArrayList<GroupedServiceResult>();
                    groupedLists.put(currentServiceResultListKey, serviceResultGroup);
                }

                serviceResultGroup.add(new GroupedServiceResult(serviceResultList.getServiceName(), serviceResult));
            }
        }

    }
}
