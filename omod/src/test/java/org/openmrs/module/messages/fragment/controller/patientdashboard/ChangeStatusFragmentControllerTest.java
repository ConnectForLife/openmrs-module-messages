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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.Person;
import org.openmrs.api.PersonService;
import org.openmrs.module.messages.Constant;
import org.openmrs.module.messages.api.dto.PersonStatusDTO;
import org.openmrs.module.messages.api.model.PersonStatus;
import org.openmrs.module.messages.api.util.PersonStatusHelper;
import org.openmrs.module.messages.builder.PersonBuilder;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class ChangeStatusFragmentControllerTest {

    private static final String PERSON_ID = "7";

    @Mock
    private FragmentModel fragmentModel;

    @Mock
    private PersonStatusHelper personStatusHelper;

    @Mock
    private PersonService personService;

    private ChangeStatusFragmentController controller = new ChangeStatusFragmentController();

    private List<String> possibleReasons;

    private List<PersonStatus> possibleStatuses;

    private PersonStatusDTO status;

    private Person person;

    @Before
    public void setUp() {
        status = buildPersonStatus();
        person = buildPerson();

        when(personStatusHelper.getStatus(PERSON_ID)).thenReturn(status);
        when(personStatusHelper.getPersonFromDashboardPersonId(PERSON_ID)).thenReturn(person);

        possibleReasons = new ArrayList<>();
        possibleReasons.add(Constant.STATUS_REASON_DECEASED);
        possibleReasons.add(Constant.STATUS_REASON_LOST_FOLLOW_UP);
        possibleReasons.add(Constant.STATUS_REASON_PAUSE);
        possibleReasons.add(Constant.STATUS_REASON_VACATION);
        possibleReasons.add(Constant.STATUS_REASON_TRANSFERRED);
        possibleReasons.add(Constant.STATUS_REASON_OTHER);

        possibleStatuses = new ArrayList<>();
        possibleStatuses.add(PersonStatus.ACTIVATED);
        possibleStatuses.add(PersonStatus.DEACTIVATED);

        when(personStatusHelper.getPossibleReasons()).thenReturn(possibleReasons);
    }

    @Test
    public void shouldProperlyAddAttributesWhenPersonStatusIsNull() {
        controller.controller(fragmentModel, personStatusHelper, PERSON_ID);

        verify(fragmentModel).addAttribute("personStatusValues", possibleStatuses);
        verify(fragmentModel).addAttribute("possibleReasons", possibleReasons);
    }

    @Test
    public void shouldProperlyAddAttributesWhenPersonStatusIsNotNull() {
        controller.controller(fragmentModel, personStatusHelper, PERSON_ID);

        verify(fragmentModel).addAttribute("personStatusValues", possibleStatuses);
        verify(fragmentModel).addAttribute("possibleReasons", possibleReasons);
        verify(fragmentModel).addAttribute("personStatusValue", status.getValue());
        verify(fragmentModel).addAttribute("personStatusReason", status.getReason());
    }

    @Test
    public void shouldProperlyUpdatePersonStatus() {
        PersonStatusDTO updatedStatus = new PersonStatusDTO()
                .setPersonId(Integer.parseInt(PERSON_ID))
                .setValue(PersonStatus.DEACTIVATED.name())
                .setReason(Constant.STATUS_REASON_VACATION);

        controller.update(PERSON_ID, updatedStatus.getValue(), updatedStatus.getReason(), personStatusHelper);
        personStatusHelper.saveStatus(updatedStatus);

        assertEquals(PersonStatus.DEACTIVATED.name(), updatedStatus.getValue());
        assertEquals(Constant.STATUS_REASON_VACATION, updatedStatus.getReason());
    }

    private PersonStatusDTO buildPersonStatus() {
         PersonStatusDTO personStatus = new PersonStatusDTO()
                .setPersonId(Integer.parseInt(PERSON_ID))
                .setValue(PersonStatus.ACTIVATED.name())
                .setReason(Constant.STATUS_REASON_DECEASED);
        return personStatus;
    }

    private Person buildPerson() {
        return new PersonBuilder().withId(Integer.parseInt(PERSON_ID)).build();
    }
}
