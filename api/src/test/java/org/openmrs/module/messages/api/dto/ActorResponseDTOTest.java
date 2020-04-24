package org.openmrs.module.messages.api.dto;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ActorResponseDTOTest {

    private static final String RESPONSE = "YES";

    private static final String OVER = "0";

    private static final long COUNT = 10;

    private ActorResponseDTO actorResponseDTO;

    @Test
    public void shouldBuildDtoObjectSuccessfully() {
        actorResponseDTO = buildActorResponseDTOObject();

        assertThat(actorResponseDTO, is(notNullValue()));
        assertEquals(RESPONSE, actorResponseDTO.getResponse());
        assertEquals(COUNT, actorResponseDTO.getResponseCount());
        assertEquals(OVER, actorResponseDTO.getOver());
    }

    private ActorResponseDTO buildActorResponseDTOObject() {
        ActorResponseDTO dto = new ActorResponseDTO()
                .setResponse(RESPONSE)
                .setResponseCount(COUNT)
                .setOver(OVER);
        return dto;
    }
}
