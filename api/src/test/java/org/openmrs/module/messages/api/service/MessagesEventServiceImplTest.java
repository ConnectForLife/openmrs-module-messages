package org.openmrs.module.messages.api.service;

import org.junit.Test;
import org.openmrs.module.messages.ContextSensitiveTest;
import org.openmrs.module.messages.api.event.MessagesEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.HashMap;
import java.util.Map;

import static org.powermock.api.mockito.PowerMockito.verifyStatic;

public class MessagesEventServiceImplTest extends ContextSensitiveTest {

    @Autowired
    @Qualifier("messages.messagesEventService")
    private MessagesEventService messagesEventService;

    @Test
    public void shouldSendEventMessage() {
        messagesEventService.sendEventMessage(buildTestMessagesEvent());

        verifyStatic();
    }

    private MessagesEvent buildTestMessagesEvent() {
        Map<String, Object> params = new HashMap<>();
        params.put("param1", "value1");
        params.put("param2", "value2");

        return new MessagesEvent("testSubject", params);
    }
}
