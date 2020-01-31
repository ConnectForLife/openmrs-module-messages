/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.event.listener;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.BaseTest;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;
import org.openmrs.module.messages.api.service.DatasetConstants;
import org.openmrs.module.messages.api.service.MessagesExecutionService;
import org.openmrs.module.messages.api.util.Properties;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class CallflowExecutionCompletionListenerTest extends BaseTest {

    private static final String PARAM_CALL_ID = "callId";
    private static final String PARAM_REF_KEY = "refKey";
    private static final String PARAM_PARAMS = "params";

    private static final String EXPECTED_CHANNEL_TYPE = "Call";
    private static final String DEFAULT_CALL_ID = "123";

    private CallflowExecutionCompletionListener listener;

    @Mock
    private MessagesExecutionService messagesExecutionService;

    @Before
    public void setUp() throws Exception {
        mockStatic(Context.class);
        when(Context.getRegisteredComponent(anyString(), eq(MessagesExecutionService.class)))
            .thenReturn(messagesExecutionService);

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

        Properties properties = getProperties(refKey, callId);
        listener.handleEvent(properties);

        Mockito.verify(messagesExecutionService).executionCompleted(
                eq(expectedGroupId),
                eq(expectedExecutionId),
                eq(EXPECTED_CHANNEL_TYPE));
    }

    @Test(expected = MessagesRuntimeException.class)
    public void handleEventShouldThrowExceptionWhenPropertiesAreEmpty() {
        Properties properties = new Properties(new HashMap<>());
        listener.handleEvent(properties);
    }

    @Test(expected = MessagesRuntimeException.class)
    public void handleEventShouldThrowExceptionWhenRefKeyIsNull() {
        final Object refKey = null;
        final Object callId = DEFAULT_CALL_ID;

        Properties properties = getProperties(refKey, callId);
        listener.handleEvent(properties);
    }

    @Test
    public void handleEventShouldWorkWithNullCallId() {
        final Object refKey = String.valueOf(DatasetConstants.SCHEDULED_SERVICE_GROUP);
        final Object callId = null;
        final int expectedGroupId = DatasetConstants.SCHEDULED_SERVICE_GROUP;
        final String expectedExecutionId = null;

        Properties properties = getProperties(refKey, callId);
        listener.handleEvent(properties);

        Mockito.verify(messagesExecutionService).executionCompleted(
                eq(expectedGroupId),
                eq(expectedExecutionId),
                eq(EXPECTED_CHANNEL_TYPE));
    }

    @Test
    public void handleEventShouldParseUnknownCallIdToNull() {
        final Object refKey = String.valueOf(DatasetConstants.SCHEDULED_SERVICE_GROUP);
        final Object callId = "unknown";
        final int expectedGroupId = DatasetConstants.SCHEDULED_SERVICE_GROUP;
        final String expectedExecutionId = null;

        Properties properties = getProperties(refKey, callId);
        listener.handleEvent(properties);

        Mockito.verify(messagesExecutionService).executionCompleted(
                eq(expectedGroupId),
                eq(expectedExecutionId),
                eq(EXPECTED_CHANNEL_TYPE));
    }

    public Properties getProperties(Object refKey, Object callId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put(PARAM_REF_KEY, refKey);

        HashMap<String, Object> map = new HashMap<>();
        map.put(PARAM_CALL_ID, callId);
        map.put(PARAM_PARAMS, params);
        return new Properties(map);
    }
}
