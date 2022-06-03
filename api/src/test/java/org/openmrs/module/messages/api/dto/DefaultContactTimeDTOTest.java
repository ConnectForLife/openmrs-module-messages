/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.dto;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class DefaultContactTimeDTOTest {

    private static final String ACTOR = "Caregiver";

    private static final String TIME = "09:30:00";

    private DefaultContactTimeDTO defaultContactTimeDTO;

    @Test
    public void shouldBuildDtoObjectSuccessfully() {
        defaultContactTimeDTO = new DefaultContactTimeDTO(ACTOR, TIME);

        assertThat(defaultContactTimeDTO, is(notNullValue()));
        assertEquals(ACTOR, defaultContactTimeDTO.getActor());
        assertEquals(TIME, defaultContactTimeDTO.getTime());
    }
}
