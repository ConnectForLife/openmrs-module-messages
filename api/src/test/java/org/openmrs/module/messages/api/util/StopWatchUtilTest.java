package org.openmrs.module.messages.api.util;

import org.junit.Test;

import java.time.Duration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class StopWatchUtilTest {

    @Test
    public void shouldReturnNonNegativeDurationBetweenStartAndRestart() throws InterruptedException {
        StopwatchUtil stopwatchUtil = new StopwatchUtil();
        Thread.sleep(2000);
        Duration duration = stopwatchUtil.restart();

        assertNotNull(duration);
        assertTrue(duration.toMillis() >= 2000);
    }

    @Test
    public void shouldReturnNonNegativeDurationBetweenStartAndStop() throws InterruptedException {
        StopwatchUtil stopwatchUtil = new StopwatchUtil();
        Thread.sleep(2000);
        Duration duration = stopwatchUtil.stop();

        assertNotNull(duration);
        assertTrue(duration.toMillis() >= 2000);
    }

    @Test
    public void shouldReturnDurationLaps() throws InterruptedException {
        StopwatchUtil stopwatchUtil = new StopwatchUtil();

        Thread.sleep(1000);
        Duration lap1 = stopwatchUtil.lap();
        assertNotNull(lap1);
        assertTrue(lap1.toMillis() >= 1000);

        Thread.sleep(2000);
        Duration lap2 = stopwatchUtil.lap();
        assertNotNull(lap2);
        assertTrue(lap2.toMillis() >= 2000);
    }

}
