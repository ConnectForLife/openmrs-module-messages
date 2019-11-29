package org.openmrs.module.messages.api.actor.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.Relationship;
import org.openmrs.RelationshipType;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.actor.ActorService;
import org.openmrs.module.messages.api.config.ConfigService;
import org.openmrs.module.messages.api.model.Actor;
import org.openmrs.module.messages.api.model.ActorType;
import org.openmrs.module.messages.api.model.RelationshipTypeDirection;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ActorServiceImpl implements ActorService {

    private static final Log LOGGER = LogFactory.getLog(ActorServiceImpl.class);

    private static final String COMMA_SEPARATOR = ",";

    private static final String SEMICOLON_SEPARATOR = ":";

    private static final int RELATIONSHIP_TYPE_POSITION = 0;

    private static final int RELATIONSHIP_DIRECTION_POSITION = 1;

    private ConfigService configService;

    @Override
    public List<Actor> getAllActorsForPatient(Patient patient) {
        List<ActorType> actorTypes = getAllActorTypes();
        Set<Actor> results = new LinkedHashSet<>();
        if (patient != null) {
            for (ActorType actorType : actorTypes) {
                results.addAll(getRelationshipsBasedOnType(patient, actorType));
            }
        }
        return new LinkedList<>(results);
    }

    @Override
    public List<ActorType> getAllActorTypes() {
        String actorTypeConfig = configService.getActorTypesConfiguration();
        if (StringUtils.isBlank(actorTypeConfig)) {
            return Collections.emptyList();
        }
        String[] parts = separateTheActorTypes(actorTypeConfig);
        return parseTheActorTypes(parts);
    }

    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    private Set<Actor> getRelationshipsBasedOnType(Patient patient, ActorType actorType) {
        Set<Actor> results = Collections.emptySet();
        if (RelationshipTypeDirection.A.equals(actorType.getDirection())) {
            results = getRelationshipsByToPerson(patient, actorType.getRelationshipType());
        } else if (RelationshipTypeDirection.B.equals(actorType.getDirection())) {
            results = getRelationshipsByFromPerson(patient, actorType.getRelationshipType());
        }
        return results;
    }

    private Set<Actor> getRelationshipsByToPerson(Patient patient, RelationshipType relationshipType) {
        Set<Actor> results = new LinkedHashSet<>();
        List<Relationship> relationships = Context.getPersonService().getRelationships(null,
                patient, relationshipType);
        for (Relationship relationship : relationships) {
            results.add(new Actor(relationship.getPersonA(), relationship));
        }
        return results;
    }

    private Set<Actor> getRelationshipsByFromPerson(Patient patient, RelationshipType relationshipType) {
        Set<Actor> results = new LinkedHashSet<>();
        List<Relationship> relationships = Context.getPersonService().getRelationships(patient,
                null, relationshipType);
        for (Relationship relationship : relationships) {
            results.add(new Actor(relationship.getPersonB(), relationship));
        }
        return results;
    }

    private String[] separateTheActorTypes(String actorTypeConfig) {
        String[] parts = { actorTypeConfig };
        if (actorTypeConfig.contains(COMMA_SEPARATOR)) {
            parts = actorTypeConfig.split(COMMA_SEPARATOR);
        }
        return parts;
    }

    private List<ActorType> parseTheActorTypes(String[] actorTypes) {
        List<ActorType> result = new LinkedList<>();
        for (String actorTypeDefinition : actorTypes) {
            ActorType actorType = parseActorType(actorTypeDefinition);
            if (actorType != null) {
                result.add(actorType);
            }
        }
        return result;
    }

    private ActorType parseActorType(String actorTypeDefinition) {
        RelationshipType relationshipType = parseRelationshipType(actorTypeDefinition);
        RelationshipTypeDirection direction = parseRelationshipTypeDirection(actorTypeDefinition);
        if (relationshipType == null) {
            LOGGER.warn(String.format("Not exist relationship described by this definition: %s", actorTypeDefinition));
            return null;
        }
        return new ActorType(relationshipType, direction);
    }

    private RelationshipType parseRelationshipType(String actorTypeDefinition) {
        RelationshipType relationshipType = null;
        String relationshipTypeUUID = actorTypeDefinition;
        if (actorTypeDefinition.contains(SEMICOLON_SEPARATOR)) {
            relationshipTypeUUID = actorTypeDefinition.split(SEMICOLON_SEPARATOR)[RELATIONSHIP_TYPE_POSITION];
        }
        relationshipType = Context.getPersonService().getRelationshipTypeByUuid(relationshipTypeUUID);
        return relationshipType;
    }

    private RelationshipTypeDirection parseRelationshipTypeDirection(String actorTypeDefinition) {
        String direction = configService.getDefaultActorRelationDirection();
        if (actorTypeDefinition.contains(SEMICOLON_SEPARATOR)) {
            String[] actorDefinition = actorTypeDefinition.split(SEMICOLON_SEPARATOR);
            if (actorDefinition.length > 1) {
                try {
                    RelationshipTypeDirection.valueOf(actorDefinition[RELATIONSHIP_DIRECTION_POSITION]);
                    direction = actorDefinition[RELATIONSHIP_DIRECTION_POSITION];
                } catch (IllegalArgumentException exception) {
                    LOGGER.warn(String.format(
                            "The %s isn't correct value for RelationshipTypeDirection enum. Default will be used.",
                            actorDefinition[RELATIONSHIP_DIRECTION_POSITION]),
                            exception);
                }
            }
        }
        return RelationshipTypeDirection.valueOf(direction);
    }
}
