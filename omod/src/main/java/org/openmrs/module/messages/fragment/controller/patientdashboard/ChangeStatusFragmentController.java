/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.fragment.controller.patientdashboard;

import org.openmrs.Person;
import org.openmrs.module.messages.api.dto.PersonStatusDTO;
import org.openmrs.module.messages.api.model.PersonStatus;
import org.openmrs.module.messages.api.util.PersonStatusHelper;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;

/**
 * Controller for managing of person status
 */
public class ChangeStatusFragmentController {

    public void controller(FragmentModel model,
            @SpringBean(value = "messages.personStatusHelper") PersonStatusHelper personStatusHelper,
            @RequestParam(value = "patientId", required = false) String personIdOrUuid) {
        model.addAttribute("personStatusValues", Arrays.asList(PersonStatus.ACTIVATED, PersonStatus.DEACTIVATED));
        model.addAttribute("possibleReasons", personStatusHelper.getPossibleReasons());
        PersonStatusDTO personStatus = personStatusHelper.getStatus(personIdOrUuid);
        if (personStatus != null) {
            model.addAttribute("personStatusValue", personStatus.getValue());
            model.addAttribute("personStatusReason", personStatus.getReason());
        }
    }

    /**
     * Updates the person status
     *
     * @param personIdOrUuid person DB id or UUID
     * @param personStatusValue status value
     * @param personStatusReason status reason value
     * @param personStatusHelper bean for PersonStatusHelper
     */
    public void update(@RequestParam(value = "personIdOrUuid", required = false) String personIdOrUuid,
            @RequestParam(value = "personStatusValue", required = false) String personStatusValue,
            @RequestParam(value = "personStatusReason", required = false) String personStatusReason,
            @SpringBean(value = "messages.personStatusHelper") PersonStatusHelper personStatusHelper) {
        Person person = personStatusHelper.getPersonFromDashboardPersonId(personIdOrUuid);
        PersonStatusDTO status = new PersonStatusDTO()
                .setPersonId(person.getPersonId())
                .setPersonUuid(person.getUuid())
                .setValue(personStatusValue)
                .setReason(personStatusReason);
        personStatusHelper.saveStatus(status);
    }
}
