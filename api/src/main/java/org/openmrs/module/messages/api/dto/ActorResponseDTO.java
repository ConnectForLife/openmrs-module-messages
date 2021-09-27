package org.openmrs.module.messages.api.dto;

/**
 * Represents an actor response DTO
 */
public class ActorResponseDTO extends BaseDTO {

    private static final long serialVersionUID = -2384469932221205221L;

    private String response;

    private String over;

    private long responseCount;

    public String getResponse() {
        return response;
    }

    public ActorResponseDTO setResponse(String response) {
        this.response = response;
        return this;
    }

    public String getOver() {
        return over;
    }

    public ActorResponseDTO setOver(String over) {
        this.over = over;
        return this;
    }

    public long getResponseCount() {
        return responseCount;
    }

    public ActorResponseDTO setResponseCount(long responseCount) {
        this.responseCount = responseCount;
        return this;
    }
}
