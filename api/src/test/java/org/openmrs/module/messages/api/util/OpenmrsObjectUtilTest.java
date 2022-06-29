/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.util;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.module.messages.BaseTest;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;
import org.openmrs.module.messages.builder.PatientBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class OpenmrsObjectUtilTest extends BaseTest {

    public static final int ID_1 = 1;
    public static final int ID_2 = 2;
    public static final int ID_3 = 3;

    @Test
    public void shouldGetIds() {
        ArrayList<Patient> patients = new ArrayList<>();
        patients.add(new PatientBuilder().withId(ID_1).build());
        patients.add(new PatientBuilder().withId(ID_2).build());
        patients.add(new PatientBuilder().withId(ID_3).build());

        List<Integer> ids = OpenmrsObjectUtil.getIds(patients);

        assertEquals(patients.size(), ids.size());
        assertThat(ids, Matchers.contains(ID_1, ID_2, ID_3));
    }

    @Test(expected = MessagesRuntimeException.class)
    public void shouldThrowExceptionIfIdIsNull() {
        ArrayList<Patient> patients = new ArrayList<>();
        patients.add(new PatientBuilder().build());
        patients.add(new PatientBuilder().buildAsNew());

        OpenmrsObjectUtil.getIds(patients);
    }
}
