package org.openmrs.module.messages.api.util;

import java.time.Duration;
import java.time.Instant;

/** Simple stopwatch. */
public final class StopwatchUtil {
  private static final ThreadLocal<Instant> startTime = ThreadLocal.withInitial(Instant::now);
  private static final ThreadLocal<Instant> lapStartTime = ThreadLocal.withInitial(Instant::now);

  public StopwatchUtil() {
    start();
  }

  public void start() {
    final Instant now = Instant.now();
    startStopwatch(now);
    startLap(now);
  }

  public Duration restart() {
    final Instant now = Instant.now();
    final Duration previousDuration = getDurationOf(startTime, now);
    startStopwatch(now);
    startLap(now);
    return previousDuration;
  }

  public Duration lap() {
    final Instant now = Instant.now();
    final Duration lapDuration = getDurationOf(lapStartTime, now);
    startLap(now);
    return lapDuration;
  }

  public Duration stop() {
    final Instant now = Instant.now();
    final Duration previousDuration = getDurationOf(startTime, now);
    startTime.remove();
    lapStartTime.remove();
    return previousDuration;
  }


  private void startStopwatch(Instant now) {
    startTime.set(now);
  }

  private void startLap(Instant now) {
    lapStartTime.set(now);
  }

  private Duration getDurationOf(ThreadLocal<Instant> startTimeHolder, Instant now) {
    return Duration.between(startTimeHolder.get(), now);
  }
}
