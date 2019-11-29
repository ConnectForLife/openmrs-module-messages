package org.openmrs.module.messages.api.actor.impl;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.RelationshipType;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.actor.ActorService;
import org.openmrs.module.messages.api.model.Actor;
import org.openmrs.module.messages.api.model.ActorType;
import org.openmrs.module.messages.api.model.RelationshipTypeDirection;
import org.openmrs.module.messages.api.util.ConfigConstants;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ActorServiceImplTest extends BaseModuleContextSensitiveTest {

    private static final String XML_DATASET_PATH = "datasets/";

    private static final String XML_ACTOR_TYPES_DATASET = XML_DATASET_PATH + "ActorTypesDataset.xml";

    private static final String CAREGIVER_PERSON_UUID = "64bb6ca5-0c4a-4f48-89e0-884955e85329";

    private static final String CAREGIVER_PERSON_RELATIONSHIP_UUID = "6a9b2da5-608e-4bd4-92a1-4729cc13d685";

    private Actor caregiverPerson;

    private static final String FATHER_PERSON_UUID = "982fbff4-2d3d-4e32-acf6-3ffc47f4def2";

    private static final String FATHER_PERSON_RELATIONSHIP_UUID = "08edc099-471e-4f0d-a5f2-b4b88d9d20de";

    private Actor fatherPerson;

    private static final String PATIENT_UUID = "7321f17d-bdef-4571-8f9b-c2ec868dc251";

    private Patient patient;

    private static final String CAREGIVER_UUID = "1286b4bc-2d35-46d6-b645-a1b563aaf62a";

    private RelationshipType caregiver;

    private static final String FATHER_UUID = "5b82938d-5cab-43b7-a8f1-e4d6fbb484cc";

    private RelationshipType father;

    private static final String DEFAULT_CONFIGURATION = CAREGIVER_UUID + "," + FATHER_UUID + ":B";

    private static final String EMPTY_CONFIGURATION = "";

    private static final String NOT_EXIST_RELATION_TYPE = "79c51b05-0bd6-40af-b680-aea91b50dac8";

    private static final String WRONG_CONFIGURATION = NOT_EXIST_RELATION_TYPE;

    private static final String REVERSED_RELATION_CONFIGURATION = CAREGIVER_UUID + ":B," + FATHER_UUID + ":B";

    @Autowired
    @Qualifier("messages.actorService")
    private ActorService actorService;

    @Before
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    public void setUp() throws Exception {
        executeDataSet(XML_ACTOR_TYPES_DATASET);
        Context.getAdministrationService().setGlobalProperty(ConfigConstants.ACTOR_TYPES_KEY, DEFAULT_CONFIGURATION);
        caregiverPerson = new Actor(Context.getPersonService().getPersonByUuid(CAREGIVER_PERSON_UUID),
                Context.getPersonService().getRelationshipByUuid(CAREGIVER_PERSON_RELATIONSHIP_UUID));
        fatherPerson = new Actor(Context.getPersonService().getPersonByUuid(FATHER_PERSON_UUID),
                Context.getPersonService().getRelationshipByUuid(FATHER_PERSON_RELATIONSHIP_UUID));
        patient = Context.getPatientService().getPatientByUuid(PATIENT_UUID);
        caregiver = Context.getPersonService().getRelationshipTypeByUuid(CAREGIVER_UUID);
        father = Context.getPersonService().getRelationshipTypeByUuid(FATHER_UUID);
    }

    @Test
    public void shouldSuccessfullyReturnActorTypes() {
        List<ActorType> actual = actorService.getAllActorTypes();
        assertThat(actual, hasItems(createActorType(caregiver, RelationshipTypeDirection.A),
                createActorType(father, RelationshipTypeDirection.B)));
    }

    @Test
    public void shouldSuccessfullyReturnActors() {
        List<Actor> actual = actorService.getAllActorsForPatient(patient);
        assertThat(actual, hasItems(caregiverPerson, fatherPerson));
    }

    @Test
    public void shouldReturnEmptyListOfTypesWhenTheConfigIsEmpty() {
        Context.getAdministrationService().setGlobalProperty(ConfigConstants.ACTOR_TYPES_KEY, EMPTY_CONFIGURATION);
        List<ActorType> actual = actorService.getAllActorTypes();
        assertThat(actual, is(empty()));
    }

    @Test
    public void shouldReturnEmptyListOfActorsWhenTheConfigIsEmpty() {
        Context.getAdministrationService().setGlobalProperty(ConfigConstants.ACTOR_TYPES_KEY, EMPTY_CONFIGURATION);
        List<Actor> actual = actorService.getAllActorsForPatient(patient);
        assertThat(actual, is(empty()));
    }

    @Test
    public void shouldReturnEmptyListOfActorWhenRelationNotExist() {
        Context.getAdministrationService().setGlobalProperty(ConfigConstants.ACTOR_TYPES_KEY, WRONG_CONFIGURATION);
        List<Actor> actual = actorService.getAllActorsForPatient(patient);
        assertThat(actual, is(empty()));
    }

    @Test
    public void shouldReturnOnlyActorsNotBeingTargetPatient() {
        Context.getAdministrationService().setGlobalProperty(ConfigConstants.ACTOR_TYPES_KEY,
                REVERSED_RELATION_CONFIGURATION);
        List<Actor> actual = actorService.getAllActorsForPatient(patient);
        assertThat(actual, is(notNullValue()));
        assertThat(actual.size(), is(1));
        assertThat(actual.get(0), is(caregiverPerson));
    }

    private ActorType createActorType(RelationshipType relationshipType, RelationshipTypeDirection direction) {
        return new ActorType(relationshipType, direction);
    }
}
