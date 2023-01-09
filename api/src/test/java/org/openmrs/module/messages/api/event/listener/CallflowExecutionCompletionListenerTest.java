/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.event.listener;

import com.antkorwin.xsync.XSync;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.BaseTest;
import org.openmrs.module.messages.api.event.CallFlowParamConstants;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;
import org.openmrs.module.messages.api.model.Message;
import org.openmrs.module.messages.api.service.ConfigService;
import org.openmrs.module.messages.api.service.DatasetConstants;
import org.openmrs.module.messages.api.service.MessagesExecutionService;
import org.openmrs.module.messages.api.util.Properties;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class CallflowExecutionCompletionListenerTest extends BaseTest {

  private static final String PARAM_CALL_ID = "callId";

  private static final String EXPECTED_CHANNEL_TYPE = "Call";
  private static final String DEFAULT_CALL_ID = "123";
  private static final String PARAM_STATUS = "status";

  private static final String IN_PROGRESS_STATUS = "IN_PROGRESS";
  private static final String COMPLETED_STATUS = "COMPLETED";

  private CallflowExecutionCompletionListener listener;

  @Mock
  private MessagesExecutionService messagesExecutionService;

  @Mock
  private ConfigService configService;

  @Before
  public void setUp() throws Exception {
    mockStatic(Context.class);
    when(Context.getRegisteredComponent(anyString(), eq(MessagesExecutionService.class))).thenReturn(
        messagesExecutionService);
    when(Context.getRegisteredComponent(anyString(), eq(ConfigService.class))).thenReturn(configService);
    when(Context.getRegisteredComponent(anyString(), eq(XSync.class))).thenReturn(new XSync());

    when(configService.getStatusesEndingCallflow()).thenReturn(wrap(COMPLETED_STATUS));

    listener = new CallflowExecutionCompletionListener();
  }

  @Test
  public void getSubjectShouldReturnCallStatusChangeEventSubject() {
    assertThat(listener.getSubject(), is("callflows-call-status"));
  }

  @Test
  public void handleEventShouldInvokeExecutionCompletedWithRefKeyAndCallId() {
    final Object refKey = String.valueOf(DatasetConstants.SCHEDULED_SERVICE_GROUP);
    final Object callId = DEFAULT_CALL_ID;
    final int expectedGroupId = DatasetConstants.SCHEDULED_SERVICE_GROUP;
    final String expectedExecutionId = DEFAULT_CALL_ID;

    Properties properties = getProperties(refKey, callId, COMPLETED_STATUS);
    listener.handleEvent(properties);

    verify(messagesExecutionService).executionCompleted(eq(expectedGroupId), eq(expectedExecutionId),
        eq(EXPECTED_CHANNEL_TYPE));
  }

  @Test(expected = MessagesRuntimeException.class)
  public void handleEventShouldThrowExceptionWhenRefKeyIsNotSet() {
    Properties properties = getProperties(null, DEFAULT_CALL_ID, COMPLETED_STATUS);
    listener.handleEvent(properties);
  }

  @Test(expected = MessagesRuntimeException.class)
  public void handleEventShouldThrowExceptionWhenStatusIsNotSet() {
    Properties properties = getProperties(DatasetConstants.SCHEDULED_SERVICE_GROUP, DEFAULT_CALL_ID, null);
    listener.handleEvent(properties);
  }

  @Test(expected = MessagesRuntimeException.class)
  public void handleEventShouldThrowExceptionWhenRefKeyIsNull() {
    final Object refKey = null;
    final Object callId = DEFAULT_CALL_ID;

    Properties properties = getProperties(refKey, callId, COMPLETED_STATUS);
    listener.handleEvent(properties);
  }

  @Test
  public void handleEventShouldBeSkippedWhenStatusHasEndingValue() {
    Properties properties = getProperties(DatasetConstants.SCHEDULED_SERVICE_GROUP, DEFAULT_CALL_ID, IN_PROGRESS_STATUS);
    listener.handleEvent(properties);

    verify(messagesExecutionService, times(0)).executionCompleted(anyInt(), anyString(), anyString());
  }

  @Test
  public void handleEventShouldWorkWithNullCallId() {
    final Object refKey = String.valueOf(DatasetConstants.SCHEDULED_SERVICE_GROUP);
    final Object callId = null;
    final int expectedGroupId = DatasetConstants.SCHEDULED_SERVICE_GROUP;
    final String expectedExecutionId = null;

    Properties properties = getProperties(refKey, callId, COMPLETED_STATUS);
    listener.handleEvent(properties);

    verify(messagesExecutionService).executionCompleted(eq(expectedGroupId), eq(expectedExecutionId),
        eq(EXPECTED_CHANNEL_TYPE));
  }

  @Test
  public void handleEventShouldParseUnknownCallIdToNull() {
    final Object refKey = String.valueOf(DatasetConstants.SCHEDULED_SERVICE_GROUP);
    final Object callId = "unknown";
    final int expectedGroupId = DatasetConstants.SCHEDULED_SERVICE_GROUP;
    final String expectedExecutionId = null;

    Properties properties = getProperties(refKey, callId, COMPLETED_STATUS);
    listener.handleEvent(properties);

    verify(messagesExecutionService).executionCompleted(eq(expectedGroupId), eq(expectedExecutionId),
        eq(EXPECTED_CHANNEL_TYPE));
  }

  @Test
  public void handleEventShouldBeSkippedWhenCallIsNotFromMessages() {
    Properties properties = getProperties(2, DEFAULT_CALL_ID, COMPLETED_STATUS, false);
    listener.handleEvent(properties);

    verify(configService, times(0)).getStatusesEndingCallflow();
    verify(messagesExecutionService, times(0)).executionCompleted(anyInt(), anyString(), anyString());
  }

  @Test
  public void handleEventShouldBeSaveFromRaceConditions() throws InterruptedException {
    final Object refKey = String.valueOf(DatasetConstants.SCHEDULED_SERVICE_GROUP);

    // CallId used as a way to differentiate events
    final Properties firstProperties = getProperties(refKey, 1, COMPLETED_STATUS);
    final Properties secondProperties = getProperties(refKey, 2, COMPLETED_STATUS);

    final List<String> events = Collections.synchronizedList(new LinkedList<>());
    final MessagesExecutionServiceVerifier verifier = new MessagesExecutionServiceVerifier(events);

    when(Context.getRegisteredComponent(anyString(), eq(MessagesExecutionService.class))).thenReturn(verifier);

    Thread firstThread = new Thread(() -> listener.handleEvent(firstProperties));
    Thread secondThread = new Thread(() -> listener.handleEvent(secondProperties));

    firstThread.start();
    secondThread.start();

    firstThread.join();
    secondThread.join();

    Assert.assertEquals("There should be 4 events recorded, 2 for each thread.", 4, events.size());
    Assert.assertEquals("First event should be followed by the same thread event.", events.get(0), events.get(1));
    Assert.assertEquals("Second event should be followed by the same thread event.", events.get(2), events.get(3));
  }

  public Properties getProperties(Object refKey, Object callId, Object status) {
    return getProperties(refKey, callId, status, true);
  }

  public Properties getProperties(Object refKey, Object callId, Object status, boolean messagesCall) {
    HashMap<String, Object> nestedParams = new HashMap<>();
    nestedParams.put(CallFlowParamConstants.REF_KEY, refKey);

    if (messagesCall) {
      List<Map<String, Object>> messages = new ArrayList<>();
      Message message = new Message("visit-reminder", 1, new HashMap<>());
      messages.add(message.toPrimitivesMap());
      nestedParams.put(CallFlowParamConstants.MESSAGES, messages);
    }

    HashMap<String, Object> map = new HashMap<>();
    map.put(PARAM_CALL_ID, callId);
    map.put(PARAM_STATUS, status);
    map.put(CallFlowParamConstants.ADDITIONAL_PARAMS, nestedParams);
    return new Properties(map);
  }

  private <T> List<T> wrap(T toWrap) {
    ArrayList<T> list = new ArrayList<>();
    list.add(toWrap);
    return list;
  }

  static class MessagesExecutionServiceVerifier implements MessagesExecutionService {
    private AtomicInteger counter = new AtomicInteger(0);
    private List<String> events;

    MessagesExecutionServiceVerifier(List<String> events) {
      this.events = events;
    }

    @Override
    public void executionCompleted(Integer groupId, String executionId, String channelType) {
      events.add(executionId);

      if (counter.getAndIncrement() == 0) {
        try {
          Thread.sleep(500);
        } catch (InterruptedException ie) {
          return;
        }
      }

      events.add(executionId);
    }
  }
}
