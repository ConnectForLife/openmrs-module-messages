package org.openmrs.module.messages.api.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AdherenceFeedbackBuilderTest {

    @Test
    public void shouldBuildNonFailedAdherenceFeedback() {
        AdherenceFeedback actual = buildTestAdherenceFeedback();

        assertNotNull(actual);
        assertEquals("Adherence daily", actual.getServiceName());
        assertEquals(70, actual.getCurrentAdherence());
        assertEquals(5, actual.getBenchmarkAdherence());
        assertEquals(7, actual.getNumberOfDays());
        assertEquals(3, actual.getNumberOfDaysWithPositiveAnswer());
        assertEquals("rising", actual.getAdherenceTrend());
        assertEquals("medium", actual.getAdherenceLevel());
    }

    @Test
    public void shouldBuildFailedAdherenceFeedback() {
        AdherenceFeedback actual = AdherenceFeedbackBuilder.createFailed("Adherence weekly", "testError");

        assertNotNull(actual);
        assertEquals("Adherence weekly", actual.getServiceName());
        assertEquals(0, actual.getCurrentAdherence());
        assertEquals(0, actual.getBenchmarkAdherence());
        assertEquals(0, actual.getNumberOfDays());
        assertEquals(0, actual.getNumberOfDaysWithPositiveAnswer());
        assertEquals("", actual.getAdherenceTrend());
        assertEquals("", actual.getAdherenceLevel());
        assertTrue(actual.isCalculationFailed());
        assertEquals("testError", actual.getErrorText());
    }

    private AdherenceFeedback buildTestAdherenceFeedback() {
        return new AdherenceFeedbackBuilder()
                .withServiceName("Adherence daily")
                .withCurrentAdherence(70)
                .withBenchmarkAdherence(5)
                .withNumberOfDays(7)
                .withNumberOfDaysWithPositiveAnswer(3)
                .withAdherenceTrend("rising")
                .withAdherenceLevel("medium")
                .build();
    }
}
