package org.openmrs.module.messages.api.dto;

/**
 * Represents an actor DTO
 */
public class ActorDTO extends BaseDTO {
    private static final long serialVersionUID = 4702818718878938436L;

    private Integer actorId;

    private String actorName;

    private String actorTypeName;

    private Integer actorTypeId;

    private Integer relationshipTypeId;

    private String relationshipTypeUuid;

    public Integer getActorId() {
        return actorId;
    }

    public ActorDTO setActorId(Integer actorId) {
        this.actorId = actorId;
        return this;
    }

    public String getActorName() {
        return actorName;
    }

    public ActorDTO setActorName(String actorName) {
        this.actorName = actorName;
        return this;
    }

    public String getActorTypeName() {
        return actorTypeName;
    }

    public ActorDTO setActorTypeName(String actorTypeName) {
        this.actorTypeName = actorTypeName;
        return this;
    }

    public Integer getActorTypeId() {
        return actorTypeId;
    }

    public ActorDTO setActorTypeId(Integer actorTypeId) {
        this.actorTypeId = actorTypeId;
        return this;
    }

    public Integer getRelationshipTypeId() {
        return relationshipTypeId;
    }

    public ActorDTO setRelationshipTypeId(Integer relationshipTypeId) {
        this.relationshipTypeId = relationshipTypeId;
        return this;
    }

    public String getRelationshipTypeUuid() {
        return relationshipTypeUuid;
    }

    public ActorDTO setRelationshipTypeUuid(String relationshipTypeUuid) {
        this.relationshipTypeUuid = relationshipTypeUuid;
        return this;
    }
}
