package org.openmrs.module.messages.api.dto;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class UserDTOTest {

    @Test
    public void shouldGetBlankUsername() {
        UserDTO actual = new UserDTO();

        assertNotNull(actual);
        assertNull(actual.getUsername());
    }

    @Test
    public void shouldGetNonBlankUsername() {
        UserDTO actual = new UserDTO("admin");

        assertNotNull(actual);
        assertEquals("admin", actual.getUsername());
    }
}
