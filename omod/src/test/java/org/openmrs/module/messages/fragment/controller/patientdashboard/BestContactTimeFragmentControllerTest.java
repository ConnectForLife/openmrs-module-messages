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
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.messages.api.service.ActorService;
import org.openmrs.module.messages.api.service.MessagesAdministrationService;
import org.openmrs.module.messages.api.service.impl.MessagesAdministrationServiceImpl;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.fragment.FragmentConfiguration;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class BestContactTimeFragmentControllerTest {

    private static final String TIMEZONE = "Europe/Brussels";

    private static final Integer PERSON_ID = 25;

    private static final String PERSON_UUID = "99abe053-3295-49bc-b469-cdfa5c7236d7";

    private BestContactTimeFragmentController controller = new BestContactTimeFragmentController();

    private Person person;

    private List<SimpleObject> bestContactTimes = new ArrayList<>();

    @Mock
    private FragmentModel fragmentModel;

    @Mock
    private FragmentConfiguration fragmentConfiguration;

    @Mock
    private UiUtils uiUtils;

    @Mock
    private ActorService actorService;

    @Mock
    protected AdministrationService administrationService;

    @Before
    public void setUp() {
        mockStatic(Context.class);
        PowerMockito.when(Context.getAdministrationService()).thenReturn(administrationService);
        PowerMockito.when(Context.getService(MessagesAdministrationService.class)).thenReturn(new MessagesAdministrationServiceImpl());
    }

    @Test
    public void shouldProperlyAddAttributesIfPersonIsNotNull() {
        when(administrationService.getGlobalProperty(ConfigConstants.DEFAULT_USER_TIMEZONE)).thenReturn(TIMEZONE);

        person = buildPerson();
        controller.controller(fragmentModel, fragmentConfiguration, uiUtils, person, actorService);

        assertThat(person, is(notNullValue()));
        verify(fragmentModel).addAttribute("bestContactTimes", bestContactTimes);
        verify(fragmentModel).addAttribute("personId", person.getId());
        verify(fragmentModel).addAttribute("personUuid", person.getUuid());
        verify(fragmentModel).addAttribute("timezone", TIMEZONE);
    }

    @Test
    public void shouldProperlyAddAttributesIfPersonIsNull() {
        when(administrationService.getGlobalProperty(ConfigConstants.DEFAULT_USER_TIMEZONE)).thenReturn(TIMEZONE);

        person = null;
        controller.controller(fragmentModel, fragmentConfiguration, uiUtils, person, actorService);

        assertThat(person, is(nullValue()));
        verify(fragmentModel).addAttribute("personId", null);
        verify(fragmentModel).addAttribute("personUuid", null);
        verify(fragmentModel).addAttribute("timezone", TIMEZONE);
    }

    private Person buildPerson() {
        Person aPerson = new Person();
        aPerson.setId(PERSON_ID);
        aPerson.setUuid(PERSON_UUID);

        return aPerson;
    }
}
