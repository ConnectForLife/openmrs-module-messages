package org.openmrs.module.messages.api.dto;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HealthTipDTOTest {

    @Test
    public void shouldBuildHealthTipDTOUsingConstructor() {
        Object[] results = new Object[] { 10, "Test category name", 50, "Test content of health tip" };
        HealthTipDTO healthTipDTO = new HealthTipDTO(results);

        assertNotNull(healthTipDTO);
        assertEquals(Integer.valueOf(10), healthTipDTO.getHealthTipCategoryId());
        assertEquals("Test category name", healthTipDTO.getHealthTipCategoryName());
        assertEquals(Integer.valueOf(50), healthTipDTO.getHealthTipId());
        assertEquals("Test content of health tip", healthTipDTO.getHealthTipText());
    }

    @Test
    public void shouldBuildHealthTipDTOUsingSetters() {
        HealthTipDTO healthTipDTO = new HealthTipDTO();
        healthTipDTO.setHealthTipCategoryId(10);
        healthTipDTO.setHealthTipCategoryName("Test category name");
        healthTipDTO.setHealthTipId(50);
        healthTipDTO.setHealthTipText("Test content of health tip");

        assertNotNull(healthTipDTO);
        assertEquals(Integer.valueOf(10), healthTipDTO.getHealthTipCategoryId());
        assertEquals("Test category name", healthTipDTO.getHealthTipCategoryName());
        assertEquals(Integer.valueOf(50), healthTipDTO.getHealthTipId());
        assertEquals("Test content of health tip", healthTipDTO.getHealthTipText());
    }
}
