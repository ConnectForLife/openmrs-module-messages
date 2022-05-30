package org.openmrs.module.messages.api.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class ValidationUtilTest {

    @Mock
    private MessageSourceService messageSourceService;

    @Before
    public void setUp() {
        mockStatic(Context.class);

        when(Context.getMessageSourceService()).thenReturn(messageSourceService);
    }

    @Test
    public void shouldGetErrorMessage() {
        ValidationUtil.resolveErrorCode("error1", "value1", "value2");
        verifyStatic();
    }

}
