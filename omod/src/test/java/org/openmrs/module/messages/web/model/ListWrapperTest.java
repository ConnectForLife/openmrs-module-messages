/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.web.model;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ListWrapperTest {

    private static final int ONE = 1;

    private static final int THREE = 3;

    private List<String> listOfSms;

    private List<String> listOfCalls;

    private ListWrapper listWrapper;

    @Test
    public void shouldCreateInstanceSuccessfully() {
        listOfSms = Arrays.asList("Sms1", "Sms2", "Sms3");
        listWrapper = new ListWrapper(listOfSms);

        assertThat(listWrapper, is(notNullValue()));
        assertEquals(THREE, listWrapper.getRecords().size());
    }

    @Test
    public void shouldSetListSuccessfully() {
        listOfCalls = Arrays.asList("Call1");
        listWrapper = new ListWrapper();
        listWrapper.setRecords(listOfCalls);

        assertThat(listWrapper, is(notNullValue()));
        assertEquals(ONE, listWrapper.getRecords().size());
    }
}
