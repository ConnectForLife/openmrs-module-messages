/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.config.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.messages.ContextSensitiveTest;
import org.openmrs.module.messages.api.config.ConfigService;
import org.openmrs.module.messages.api.strategy.ReschedulingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class ConfigServiceImplTest extends ContextSensitiveTest {

    private static final String XML_DATASET_PATH = "datasets/";

    private static final String XML_ACTOR_TYPES_DATASET = "ConfigDataset.xml";

    private static final String EXPECTED_ACTOR_CONFIGURATION =
            "1286b4bc-2d35-46d6-b645-a1b563aaf62a:A,5b82938d-5cab-43b7-a8f1-e4d6fbb484cc:B";

    private static final String EXPECTED_RESCHEDULING_STRATEGY = "messages.failedMessageReschedulingStrategy";

    private static final int EXPECTED_MAX_NUMBER_OF_RESCHEDULING = 3;

    private static final int EXPECTED_TIME_INTERVAL_TO_NEXT_RESCHEDULE = 900;
    public static final boolean EXPECTED_IS_CONSENT_CONTROL_ENABLED = true;

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
    public void shouldReturnIfConsentControlIsEnabledWhenSetGpToTrue() {
        boolean actual = configService.isConsentControlEnabled();
        assertThat(actual, is(EXPECTED_IS_CONSENT_CONTROL_ENABLED));
    }

    @Test
    public void shouldReturnExpectedActorConfiguration() {
        String actual = configService.getActorTypesConfiguration();
        assertThat(actual, is(EXPECTED_ACTOR_CONFIGURATION));
    }
}
