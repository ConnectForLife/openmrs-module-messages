package org.openmrs.module.messages.api.config.impl;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.messages.ContextSensitiveTest;
import org.openmrs.module.messages.api.config.ConfigService;
import org.openmrs.module.messages.api.strategy.ReschedulingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ConfigServiceImplTest extends ContextSensitiveTest {

    private static final String XML_DATASET_PATH = "datasets/";

    private static final String XML_ACTOR_TYPES_DATASET = "ConfigDataset.xml";

    private static final String EXPECTED_ACTOR_CONFIGURATION =
            "1286b4bc-2d35-46d6-b645-a1b563aaf62a:A,5b82938d-5cab-43b7-a8f1-e4d6fbb484cc:B";

    private static final String EXPECTED_RESCHEDULING_STRATEGY = "messages.failedMessageReschedulingStrategy";

    private static final int EXPECTED_MAX_NUMBER_OF_RESCHEDULING = 3;

    private static final int EXPECTED_TIME_INTERVAL_TO_NEXT_RESCHEDULE = 900;

    @Autowired(required = false)
    @Qualifier(EXPECTED_RESCHEDULING_STRATEGY)
    private ReschedulingStrategy reschedulingStrategy;

    @Autowired
    @Qualifier("messages.configService")
    private ConfigService configService;

    @Before
    public void setUp() throws Exception {
        executeDataSet(XML_DATASET_PATH + XML_ACTOR_TYPES_DATASET);
    }

    @Test
    public void shouldReturnExpectedReschedulingStrategy() {
        ReschedulingStrategy actual = configService.getReschedulingStrategy();

        assertThat(actual, is(reschedulingStrategy));
    }

    @Test
    public void shouldReturnExpectedMaxNumberOfRescheduling() {
        int actual = configService.getMaxNumberOfRescheduling();
        assertThat(actual, is(EXPECTED_MAX_NUMBER_OF_RESCHEDULING));
    }

    @Test
    public void shouldReturnExpectedTimeIntervalToNextReschedule() {
        int actual = configService.getTimeIntervalToNextReschedule();
        assertThat(actual, is(EXPECTED_TIME_INTERVAL_TO_NEXT_RESCHEDULE));
    }

    @Test
    public void shouldReturnExpectedActorConfiguration() {
        String actual = configService.getActorTypesConfiguration();
        assertThat(actual, is(EXPECTED_ACTOR_CONFIGURATION));
    }
}
