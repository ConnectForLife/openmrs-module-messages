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
