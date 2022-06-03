/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.util;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.openmrs.module.messages.Constant.CAREGIVER_RELATIONSHIP;
import static org.openmrs.module.messages.Constant.DEACTIVATED_SCHEDULE_MESSAGE;
import static org.openmrs.module.messages.Constant.PATIENT_RELATIONSHIP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.openmrs.module.messages.api.dto.ActorScheduleDTO;
import org.openmrs.module.messages.api.dto.MessageDTO;
import org.openmrs.module.messages.api.dto.MessageDetailsDTO;
import org.openmrs.module.messages.api.dto.UserDTO;
import org.openmrs.module.messages.api.model.Actor;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.builder.ActorBuilder;
import org.openmrs.module.messages.builder.TemplateBuilder;

public class MessageDetailsUtilTest {

    private static final int ACTOR_ONE_ID = 1;
    private static final int ACTOR_TWO_ID = 2;
    private static final String EXISTING_TEMPLATE_NAME = "template EXISTING_TEMPLATE_NAME 1";
    private static final UserDTO EXISTING_USER = new UserDTO("test EXISTING_USER");

    @Test
    public void shouldAddDefaultMessageDetails() {

        MessageDetailsDTO messageDetailsDTO = new MessageDetailsDTO();
        List<Template> templates = Collections.singletonList(new TemplateBuilder().build());
        messageDetailsDTO =
            MessageDetailsUtil.attachDefaultMessageDetails(messageDetailsDTO, templates, null);

        Assert.assertThat(messageDetailsDTO.getMessages(), hasSize(1));
        MessageDTO firstMessage = messageDetailsDTO.getMessages().get(0);
        Assert.assertThat(firstMessage.getActorSchedules(),
            contains(
                new ActorScheduleDTO(null, PATIENT_RELATIONSHIP,
                    DEACTIVATED_SCHEDULE_MESSAGE)
            ));

        assertIsAdditionalDefaultMessageDetail(firstMessage);
    }

    @Test
    public void shouldReturnAdditionalDefaultMessageDetails() {
        MessageDetailsDTO messageDetailsDTO =
            new MessageDetailsDTO(Collections.singletonList(new MessageDTO(EXISTING_TEMPLATE_NAME, null, EXISTING_USER,
                new ArrayList<>())));

        Template template = new TemplateBuilder()
            .withName(EXISTING_TEMPLATE_NAME)
            .build();

        Template template2 = new TemplateBuilder().build();

        List<Template> templates = Arrays.asList(template, template2);

        messageDetailsDTO =
            MessageDetailsUtil.attachDefaultMessageDetails(messageDetailsDTO, templates, new ArrayList<>());

        Assert.assertThat(messageDetailsDTO.getMessages(), hasSize(2));
        MessageDTO firstMessage = messageDetailsDTO.getMessages().get(0);
        MessageDTO secondMessage = messageDetailsDTO.getMessages().get(1);

        assertIsExistingPatientTemplateMessageDetail(firstMessage);
        assertIsAdditionalDefaultMessageDetail(secondMessage);
    }

    @Test
    public void shouldNotAddDefaultMessageDetails() {
        MessageDetailsDTO messageDetailsDTO =
            new MessageDetailsDTO(Collections.singletonList(new MessageDTO(EXISTING_TEMPLATE_NAME, null, EXISTING_USER,
                new ArrayList<>())));
        List<Template> templates = Collections.singletonList(new TemplateBuilder()
            .withName(EXISTING_TEMPLATE_NAME)
            .build());

        messageDetailsDTO =
            MessageDetailsUtil.attachDefaultMessageDetails(messageDetailsDTO, templates, new ArrayList<>());

        Assert.assertThat(messageDetailsDTO.getMessages(), hasSize(1));
        MessageDTO firstMessage = messageDetailsDTO.getMessages().get(0);
        Assert.assertThat(firstMessage.getAuthor(), equalTo(EXISTING_USER));
        Assert.assertThat(firstMessage.getActorSchedules(),
            contains(
                new ActorScheduleDTO(null, PATIENT_RELATIONSHIP, DEACTIVATED_SCHEDULE_MESSAGE)
        ));

        assertIsExistingPatientTemplateMessageDetail(firstMessage);
    }

    @Test
    public void shouldAddDefaultDetailsForMultipleActorsAndReturnInOrder() {

        MessageDetailsDTO messageDetailsDTO = new MessageDetailsDTO();
        List<Template> templates = Collections.singletonList(new TemplateBuilder().build());
        List<Actor> actors = new ArrayList<>();
        actors.add(new ActorBuilder().withActorId(ACTOR_ONE_ID).build());
        actors.add(new ActorBuilder().withActorId(ACTOR_TWO_ID).build());

        messageDetailsDTO =
            MessageDetailsUtil.attachDefaultMessageDetails(messageDetailsDTO, templates, actors);

        Assert.assertThat(messageDetailsDTO.getMessages(), hasSize(1));
        MessageDTO firstMessage = messageDetailsDTO.getMessages().get(0);
        Assert.assertThat(firstMessage.getActorSchedules(),
            contains(
                new ActorScheduleDTO(null, PATIENT_RELATIONSHIP, DEACTIVATED_SCHEDULE_MESSAGE),
                new ActorScheduleDTO(ACTOR_ONE_ID, CAREGIVER_RELATIONSHIP, DEACTIVATED_SCHEDULE_MESSAGE),
                new ActorScheduleDTO(ACTOR_TWO_ID, CAREGIVER_RELATIONSHIP, DEACTIVATED_SCHEDULE_MESSAGE))
        );

        assertIsAdditionalDefaultMessageDetail(firstMessage);
    }

    @Test
    public void shouldAddDefaultDetailsForMultipleActorsWithOneExistingAndReturnInOrder() {

        final String sampleService = "Some service";
        final String sampleExistingSchedule = "Start date: 30 Dec 2019";

        List<Template> templates =
            Collections.singletonList(new TemplateBuilder().withName(sampleService).build());
        List<Actor> actors = new ArrayList<>();
        Actor actor = new ActorBuilder().build();
        actors.add(new ActorBuilder().withActorId(ACTOR_ONE_ID).build());
        actors.add(new ActorBuilder().withActorId(ACTOR_TWO_ID).build());

        MessageDetailsDTO messageDetailsDTO = new MessageDetailsDTO(
            Collections.singletonList(buildMessageForActor(sampleService, sampleExistingSchedule, actor)));

        messageDetailsDTO =
            MessageDetailsUtil.attachDefaultMessageDetails(messageDetailsDTO, templates, actors);

        Assert.assertThat(messageDetailsDTO.getMessages(), hasSize(1));
        MessageDTO firstMessage = messageDetailsDTO.getMessages().get(0);
        Assert.assertThat(firstMessage.getActorSchedules(),
            contains(
                new ActorScheduleDTO(null, PATIENT_RELATIONSHIP, DEACTIVATED_SCHEDULE_MESSAGE),
                new ActorScheduleDTO(ACTOR_ONE_ID, CAREGIVER_RELATIONSHIP, sampleExistingSchedule),
                new ActorScheduleDTO(ACTOR_TWO_ID, CAREGIVER_RELATIONSHIP, DEACTIVATED_SCHEDULE_MESSAGE))
        );

        assertIsAdditionalDefaultMessageDetail(firstMessage);
    }

    private MessageDTO buildMessageForActor(String serviceName, String schedule, Actor actor) {
        ActorScheduleDTO actorSchedule =
            new ActorScheduleDTO(actor.getRelationship().getPersonA().getId(),
                actor.getRelationship().getRelationshipType().getaIsToB(),
                schedule);
        List<ActorScheduleDTO> schedules = new ArrayList<>();
        schedules.add(actorSchedule);
        return new MessageDTO(serviceName, null, null, schedules);
    }

    private void assertIsExistingPatientTemplateMessageDetail(MessageDTO messageDetailsDTO) {
        Assert.assertNotNull(messageDetailsDTO.getAuthor());
    }

    private void assertIsAdditionalDefaultMessageDetail(MessageDTO messageDetailsDTO) {
        Assert.assertNull(messageDetailsDTO.getAuthor());
    }
}
