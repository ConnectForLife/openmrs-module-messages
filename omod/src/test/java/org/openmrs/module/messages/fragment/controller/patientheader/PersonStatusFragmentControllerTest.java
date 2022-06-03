/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.fragment.controller.patientheader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.module.messages.api.dto.PersonStatusDTO;
import org.openmrs.module.messages.api.util.PersonStatusHelper;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class PersonStatusFragmentControllerTest {

    private static final String STYLE = "Example style";

    private static final String TITLE = "Example title";

    private static final String PERSON_ID = "7";

    @Mock
    private FragmentModel fragmentModel;

    @Mock
    private PersonStatusHelper personStatusHelper;

    private PersonStatusFragmentController controller = new PersonStatusFragmentController();

    private PersonStatusDTO status;

    @Test
    public void shouldProperlyAddAttributesWhenPersonStatusIsNull() {
        status = null;
        when(personStatusHelper.getStatus(PERSON_ID)).thenReturn(status);
        controller.controller(fragmentModel, personStatusHelper, PERSON_ID);

        verify(fragmentModel).addAttribute("personIdOrUuid", PERSON_ID);
    }

    @Test
    public void shouldProperlyAddAttributesWhenPersonStatusIsNotNull() {
        status = buildPersonStatus();
        when(personStatusHelper.getStatus(PERSON_ID)).thenReturn(status);
        controller.controller(fragmentModel, personStatusHelper, PERSON_ID);

        verify(fragmentModel).addAttribute("personIdOrUuid", PERSON_ID);
        verify(fragmentModel).addAttribute("personStatusValue", status.getTitle());
        verify(fragmentModel).addAttribute("personStatusMessageStyle", status.getStyle());
    }

    private PersonStatusDTO buildPersonStatus() {
        PersonStatusDTO personStatus = new PersonStatusDTO()
                .setPersonId(Integer.parseInt(PERSON_ID))
                .setStyle(STYLE)
                .setTitle(TITLE);
        return personStatus;
    }
}
