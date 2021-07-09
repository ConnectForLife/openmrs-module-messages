package org.openmrs.module.messages.api.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openmrs.module.messages.api.model.ScheduledExecutionContext;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DateUtil.class)
public class ScheduledExecutionContextUtilTest {

    @Test
    public void shouldReadJSONWithCorrectDateUTC() {
        // given
        final Instant dateFromJson = Instant.parse("2021-07-09T19:18:59.00Z");
        final String jsonWithDate = "{\n" + //
                "  \"serviceIdsToExecute\": [\n" + //
                "    17024\n" + //
                "  ],\n" + //
                "  \"executionDate\": \"Friday, July 9, 2021 7:18:59 PM UTC\",\n" + //
                "  \"actorId\": 1307,\n" + //
                "  \"patientId\": 1307,\n" + //
                "  \"actorType\": \"Patient\",\n" + //
                "  \"groupId\": 11001\n" + //
                "}";

        // when
        final ScheduledExecutionContext context = ScheduledExecutionContextUtil.fromJson(jsonWithDate);

        // then
        assertEquals(dateFromJson, context.getExecutionDate());
    }

    @Test
    public void shouldReadJSONWithCorrectDateCEST() {
        // given
        final Instant dateFromJson = Instant.parse("2021-07-08T11:00:00.00Z");
        final String jsonWithDate = "{\n" + //
                "  \"serviceIdsToExecute\": [\n" + //
                "    1\n" + //
                "  ],\n" + //
                "  \"channelType\": \"SMS\",\n" + //
                "  \"executionDate\": \"Thursday, July 8, 2021 1:00:00 PM CEST\",\n" + //
                "  \"actorId\": 7,\n" + //
                "  \"patientId\": 7,\n" + //
                "  \"actorType\": \"Patient\",\n" + //
                "  \"groupId\": 1,\n" + //
                "  \"channelConfiguration\": {\n" + //
                "    \"templateValue\": \"{ \\\"message\\\":\\\"Simple message here.\\\" }\"\n" + //
                "  }\n" + //
                "}";

        // when
        final ScheduledExecutionContext context = ScheduledExecutionContextUtil.fromJson(jsonWithDate);

        // then
        assertEquals(dateFromJson, context.getExecutionDate());
    }

    @Test
    public void shouldWriteJSONWithCorrectDateUTC() throws IOException, IllegalAccessException {
        // "Mock" the system zone to UTC
        PowerMockito.field(DateUtil.class, "clock").set(null, Clock.fixed(Instant.now(), ZoneId.of("UTC")));

        // given
        final String expectedDateString = "Friday, July 9, 2021 11:00:00 AM UTC";
        final Instant expectedDate = Instant.parse("2021-07-09T11:00:00.00Z");
        final ScheduledExecutionContext context = new ScheduledExecutionContext();
        context.setExecutionDate(expectedDate);

        // when
        final String json = ScheduledExecutionContextUtil.toJson(context);

        // then
        final Map jsonAsMap = new ObjectMapper().readValue(json, Map.class);

        assertEquals(expectedDateString, jsonAsMap.get("executionDate"));
    }
}
