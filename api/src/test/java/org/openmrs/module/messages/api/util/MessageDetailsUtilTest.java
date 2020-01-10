package org.openmrs.module.messages.api.util;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import java.util.ArrayList;
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

    private static final String EXPECTED_DEACTIVATED_STATE = "DEACTIVATED";
    private static final String PATIENT_ACTOR_TYPE = "Patient";
    private static final String CAREGIVER_ACTOR_TYPE = "Caregiver";
    public static final int ACTOR_ONE_ID = 1;
    public static final int ACTOR_TWO_ID = 2;

    @Test
    public void shouldAddDefaultTemplate() {

        MessageDetailsDTO messageDetailsDTO = new MessageDetailsDTO();
        List<Template> templates = Collections.singletonList(new TemplateBuilder().build());
        messageDetailsDTO =
            MessageDetailsUtil.attachDefaultTemplates(messageDetailsDTO, templates, null);

        Assert.assertEquals(1, messageDetailsDTO.getMessages().size());
        Assert.assertThat(messageDetailsDTO.getMessages().get(0).getActorSchedules(),
            contains(
                new ActorScheduleDTO(null, PATIENT_ACTOR_TYPE, EXPECTED_DEACTIVATED_STATE)
        ));
    }

    @Test
    public void shouldNotAddDefaultTemplate() {

        final String name = "template name 1";
        final UserDTO user = new UserDTO("test user");

        MessageDetailsDTO messageDetailsDTO =
            new MessageDetailsDTO(Collections.singletonList(new MessageDTO(name, null, user,
                new ArrayList<>())));
        List<Template> templates = Collections.singletonList(new TemplateBuilder()
            .withName(name)
            .build());

        messageDetailsDTO =
            MessageDetailsUtil.attachDefaultTemplates(messageDetailsDTO, templates, new ArrayList<>());

        Assert.assertThat(messageDetailsDTO.getMessages(), hasSize(1));
        Assert.assertThat(messageDetailsDTO.getMessages().get(0).getAuthor(), equalTo(user));
        Assert.assertThat(messageDetailsDTO.getMessages().get(0).getActorSchedules(),
            contains(
                new ActorScheduleDTO(null, PATIENT_ACTOR_TYPE, EXPECTED_DEACTIVATED_STATE)
        ));
    }

    @Test
    public void shouldAddDefaultTemplatesForMultipleActorsAndReturnInOrder() {

        MessageDetailsDTO messageDetailsDTO = new MessageDetailsDTO();
        List<Template> templates = Collections.singletonList(new TemplateBuilder().build());
        List<Actor> actors = new ArrayList<>();
        actors.add(new ActorBuilder().withActorId(ACTOR_ONE_ID).build());
        actors.add(new ActorBuilder().withActorId(ACTOR_TWO_ID).build());

        messageDetailsDTO =
            MessageDetailsUtil.attachDefaultTemplates(messageDetailsDTO, templates, actors);

        Assert.assertThat(messageDetailsDTO.getMessages(), hasSize(1));
        Assert.assertThat(messageDetailsDTO.getMessages().get(0).getActorSchedules(),
            contains(
                new ActorScheduleDTO(null, PATIENT_ACTOR_TYPE, EXPECTED_DEACTIVATED_STATE),
                new ActorScheduleDTO(ACTOR_ONE_ID, CAREGIVER_ACTOR_TYPE, EXPECTED_DEACTIVATED_STATE),
                new ActorScheduleDTO(ACTOR_TWO_ID, CAREGIVER_ACTOR_TYPE, EXPECTED_DEACTIVATED_STATE))
        );
    }

    @Test
    public void shouldAddDefaultTemplatesForMultipleActorsWithOneExistingAndReturnInOrder() {

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
            MessageDetailsUtil.attachDefaultTemplates(messageDetailsDTO, templates, actors);

        Assert.assertThat(messageDetailsDTO.getMessages(), hasSize(1));
        Assert.assertThat(messageDetailsDTO.getMessages().get(0).getActorSchedules(),
            contains(
                new ActorScheduleDTO(null, PATIENT_ACTOR_TYPE, EXPECTED_DEACTIVATED_STATE),
                new ActorScheduleDTO(ACTOR_ONE_ID, CAREGIVER_ACTOR_TYPE, sampleExistingSchedule),
                new ActorScheduleDTO(ACTOR_TWO_ID, CAREGIVER_ACTOR_TYPE, EXPECTED_DEACTIVATED_STATE))
        );
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
}
