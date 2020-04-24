package org.openmrs.module.messages.web.model;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class MessagingParamsTest {

    private static final Integer PERSON_ID = 5;

    private static final boolean IS_PATIENT = true;

    private MessagingParams messagingParams;

    private MessagingParams messagingParams2;

    @Before
    public void setUp() {
        messagingParams = new MessagingParams()
                .setPersonId(PERSON_ID)
                .setIsPatient(IS_PATIENT);

        messagingParams2 = new MessagingParams()
                .setPersonId(PERSON_ID)
                .setIsPatient(!IS_PATIENT);
    }

    @Test
    public void shouldCreateInstancesSuccessfully() {
        assertThat(messagingParams, is(notNullValue()));
        assertEquals(PERSON_ID, messagingParams.getPersonId());
        assertTrue(messagingParams.isPatient());

        assertThat(messagingParams2, is(notNullValue()));
        assertEquals(PERSON_ID, messagingParams2.getPersonId());
        assertFalse(messagingParams2.isPatient());

        assertFalse(messagingParams.equals(messagingParams2));
    }
}
