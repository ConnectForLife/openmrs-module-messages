package org.openmrs.module.messages.api.scheduler.job;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.service.MessagingService;
import org.openmrs.scheduler.TaskDefinition;
import org.openmrs.test.BaseContextMockTest;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class MessageDeliveriesJobDefinitionUnitTest extends BaseContextMockTest {
  private static final Date HOUR_START_START_DATE = Date.from(Instant.parse("2023-02-21T01:00:00.00Z"));
  private static final Long HOURLY_INTERVAL = 3600L;

  @Mock
  private MessagingService messagingServiceMock;

  @Before
  public void setup() throws Exception {
    PowerMockito.mockStatic(Context.class);
    PowerMockito
        .doReturn(messagingServiceMock)
        .when(Context.class, "getRegisteredComponent", MessagesConstants.MESSAGING_SERVICE, MessagingService.class);
  }

  /**
   * Warning: This unit test uses OpenMRS code which calls new Date(), it makes this test potentially sensitive to the
   * execution time.
   * The idea of this unit test is to verify that when Task is supposed to start at 11:00 o'clock and covers 1hr
   * window (because job execution rate is 1hr), but the Task is actually executed a moment past 11:00, then it will still
   * pass proper 11:00 - 12:00 date time range to Message Template executors.
   * When the unit test will be executed exactly at the start of an hour (to the milliseconds precision), it may give a
   * false-positive result or false-negative result.
   */
  @Test
  public void shouldFilterScheduledEventsBasedOnActualTaskExecutionWindow() {
    final TaskDefinition messageDeliveriesTask = new TaskDefinition();
    messageDeliveriesTask.setStarted(true);
    messageDeliveriesTask.setStartTime(HOUR_START_START_DATE);
    messageDeliveriesTask.setRepeatInterval(HOURLY_INTERVAL);

    final MessageDeliveriesJobDefinition messageDeliveriesJobDefinition = new MessageDeliveriesJobDefinition();
    messageDeliveriesJobDefinition.initialize(messageDeliveriesTask);

    messageDeliveriesJobDefinition.execute();

    final ArgumentCaptor<ZonedDateTime> startDateCaptor = ArgumentCaptor.forClass(ZonedDateTime.class);
    final ArgumentCaptor<ZonedDateTime> endDateCaptor = ArgumentCaptor.forClass(ZonedDateTime.class);

    Mockito.verify(messagingServiceMock).retrieveAllServiceExecutions(startDateCaptor.capture(), endDateCaptor.capture());

    final ZonedDateTime startOfTheHour = ZonedDateTime.now().truncatedTo(ChronoUnit.HOURS);
    Assert.assertEquals(startOfTheHour.toInstant(), startDateCaptor.getValue().toInstant());
    Assert.assertEquals(startOfTheHour.plusHours(1).toInstant(), endDateCaptor.getValue().toInstant());
  }
}
