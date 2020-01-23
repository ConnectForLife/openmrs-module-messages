/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.messages.ContextSensitiveTest;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.messages.api.dto.PersonStatusConfigDTO;
import org.openmrs.module.messages.api.model.PersonStatus;
import org.openmrs.module.messages.api.model.RelationshipTypeDirection;
import org.openmrs.module.messages.api.strategy.ReschedulingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class ConfigServiceImplTest extends ContextSensitiveTest {

    private static final String XML_DATASET_PATH = "datasets/";

    private static final String XML_CONFIG_DATASET = "ConfigDataset.xml";

    private static final String EXPECTED_ACTOR_CONFIGURATION =
            "1286b4bc-2d35-46d6-b645-a1b563aaf62a:A,5b82938d-5cab-43b7-a8f1-e4d6fbb484cc:B";

    private static final String SMS_CHANNEL_RESCHEDULING_STRATEGY = "messages.failedMessageReschedulingStrategy";
    private static final String CALL_CHANNEL_RESCHEDULING_STRATEGY =
            "messages.failedAndItsPendingMessagesReschedulingStrategy";
    private static final String SMS_CHANNEL_NAME = "SMS";
    private static final String CALL_CHANNEL_NAME = "Call";
    private static final String CHANNEL_NAME_WITHOUT_RESCHEDULING_STRATEGY = "channelNameWithoutReschedulingStrategy";

    private static final int EXPECTED_MAX_NUMBER_OF_RESCHEDULING = 3;

    private static final int EXPECTED_TIME_INTERVAL_TO_NEXT_RESCHEDULE = 900;

    private static final boolean EXPECTED_IS_CONSENT_CONTROL_ENABLED = true;

    private static final String EXPECTED_DEFAULT_RELATIONSHIP_DIRECTION = RelationshipTypeDirection.A.name();

    private static final String EXPECTED_ACTIVATED_STYLE =
            "background-color: #51a351; border-color: #51a351; color: #f5f5f5;";

    private static final String EXPECTED_MISSING_STYLE =
            "background-color: #EEA616; border-color: #EEA616; color: #f5f5f5;";

    private static final String EXPECTED_NO_CONSENT_STYLE =
            "background-color: #EEA616; border-color: #EEA616; color: #f5f5f5;";

    private static final int EXPECTED_SIE_OF_PERSON_STATUS_REASONS = 6;

    private PersonStatusConfigDTO noConsentConfig;

    private List<PersonStatusConfigDTO> personStatusConfigDTOS = new ArrayList<>();

    @Autowired
    @Qualifier(ConfigConstants.DEFAULT_RESCHEDULING_STRATEGY)
    private ReschedulingStrategy defaultReschedulingStrategy;

    @Autowired
    @Qualifier(CALL_CHANNEL_RESCHEDULING_STRATEGY)
    private ReschedulingStrategy callChannelReschedulingStrategy;

    @Autowired
    @Qualifier(SMS_CHANNEL_RESCHEDULING_STRATEGY)
    private ReschedulingStrategy smsChannelReschedulingStrategy;

    @Autowired
    @Qualifier("messages.configService")
    private ConfigService configService;

    @Before
    public void setUp() throws Exception {
        executeDataSet(XML_DATASET_PATH + XML_CONFIG_DATASET);
        buildExpectedStatusConfig();
    }

    @Test
    public void shouldReturnReschedulingStrategyForCallChannel() {
        ReschedulingStrategy actual = configService.getReschedulingStrategy(CALL_CHANNEL_NAME);

        assertThat(actual, is(callChannelReschedulingStrategy));
    }

    @Test
    public void shouldReturnReschedulingStrategyForSmsChannel() {
        ReschedulingStrategy actual = configService.getReschedulingStrategy(SMS_CHANNEL_NAME);

        assertThat(actual, is(smsChannelReschedulingStrategy));
    }

    @Test
    public void shouldReturnDefaultReschedulingStrategyIfNotDefinedInGp() {
        ReschedulingStrategy actual = configService.getReschedulingStrategy(CHANNEL_NAME_WITHOUT_RESCHEDULING_STRATEGY);

        assertThat(actual, is(defaultReschedulingStrategy));
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

    @Test
    public void shouldReturnExpectedDefaultRelationshipDirection() {
        String actual = configService.getDefaultActorRelationDirection();
        assertThat(actual, is(EXPECTED_DEFAULT_RELATIONSHIP_DIRECTION));
    }

    @Test
    public void shouldReturnExpectedPersonStatusConfigurations() {
        List<PersonStatusConfigDTO> actual = configService.getPersonStatusConfigurations();
        assertThat(actual, is(personStatusConfigDTOS));
    }

    @Test
    public void shouldReturnExpectedPersonStatusConfiguration() {
        PersonStatusConfigDTO actual = configService.getPersonStatusConfiguration(PersonStatus.NO_CONSENT);
        assertThat(actual, is(noConsentConfig));
    }

    @Test
    public void shouldReturnNullPersonStatusConfigurationIfMissing() {
        PersonStatusConfigDTO actual = configService.getPersonStatusConfiguration(PersonStatus.DEACTIVATED);
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void shouldReturnExpectedPossibleReasons() {
        List<String> actual = configService.getPersonStatusPossibleChangeReasons();
        assertThat(actual, is(notNullValue()));
        assertThat(actual.size(), is(EXPECTED_SIE_OF_PERSON_STATUS_REASONS));
    }

    private void buildExpectedStatusConfig() {
        personStatusConfigDTOS = new ArrayList<>();
        noConsentConfig = new PersonStatusConfigDTO()
                .setName(PersonStatus.NO_CONSENT.name())
                .setStyle(EXPECTED_NO_CONSENT_STYLE);
        personStatusConfigDTOS.add(noConsentConfig);
        personStatusConfigDTOS.add(
                new PersonStatusConfigDTO()
                        .setName(PersonStatus.ACTIVATED.name())
                        .setStyle(EXPECTED_ACTIVATED_STYLE));
        personStatusConfigDTOS.add(
                new PersonStatusConfigDTO()
                        .setName(PersonStatus.MISSING_VALUE.name())
                        .setStyle(EXPECTED_MISSING_STYLE));
    }
}
