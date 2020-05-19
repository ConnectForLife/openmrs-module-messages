package org.openmrs.module.messages.api.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.Relationship;
import org.openmrs.RelationshipType;
import org.openmrs.api.PatientService;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.messages.api.dto.ContactTimeDTO;
import org.openmrs.module.messages.api.exception.ValidationException;
import org.openmrs.module.messages.api.model.Actor;
import org.openmrs.module.messages.api.model.ActorType;
import org.openmrs.module.messages.api.model.ErrorMessage;
import org.openmrs.module.messages.api.model.RelationshipTypeDirection;
import org.openmrs.module.messages.api.service.ActorService;
import org.openmrs.module.messages.api.service.ConfigService;
import org.openmrs.module.messages.api.util.PersonAttributeUtil;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implements methods related to the actor service management
 */
public class ActorServiceImpl implements ActorService {

    private static final Log LOGGER = LogFactory.getLog(ActorServiceImpl.class);

    private static final Pattern TIME_PATTERN = Pattern.compile("^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$");

    private static final String COMMA_SEPARATOR = ",";

    private static final String SEMICOLON_SEPARATOR = ":";

    private static final int RELATIONSHIP_TYPE_POSITION = 0;

    private static final int RELATIONSHIP_DIRECTION_POSITION = 1;

    private ConfigService configService;

    private PersonService personService;

    private PatientService patientService;

    @Override
    public List<Actor> getAllActorsForPatient(Patient patient) {
        return getAllActorsForPerson(patient, true);
    }

    @Override
    public List<Actor> getAllActorsForPatientId(Integer patientId) {
        Patient patient = patientService.getPatient(patientId);
        if (patient == null) {
            throw new ValidationException(String.format("Patient with %s id doesn't exist.", patientId));
        }
        return getAllActorsForPerson(patient, true);
    }

    @Override
    public List<Actor> getAllActorsForPerson(Person person, boolean isPatient) {
        List<ActorType> actorTypes = getAllActorTypes();
        Set<Actor> results = new TreeSet<>();
        if (person != null) {
            for (ActorType actorType : actorTypes) {
                results.addAll(getRelationshipsBasedOnType(person, actorType, isPatient));
            }
        }
        return new LinkedList<>(results);
    }

    @Override
    public List<Actor> getAllActorsForPersonId(Integer personId, boolean isPatient) {
        Person person = personService.getPerson(personId);
        if (person == null) {
            throw new ValidationException(String.format("Person with %s id doesn't exist.", personId));
        }
        if (person != null) {
            Context.refreshEntity(person); //person caching issue fix
        }
        return getAllActorsForPerson(person, isPatient);
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

    @Override
    public String getContactTime(Integer personId) throws ValidationException {
        Person person = personService.getPerson(personId);
        if (person == null) {
            throw new ValidationException(String.format("Person with %d id doesn't exist.", personId));
        }
        Context.refreshEntity(person); //person caching issue fix
        return getContactTimeValue(person);
    }

    @Override
    public List<ContactTimeDTO> getContactTimes(List<Integer> personIds) {
        List<ContactTimeDTO> result = new LinkedList<>();
        for (Integer id : personIds) {
            String value;
            try {
                value = getContactTime(id);
            } catch (ValidationException ex) {
                LOGGER.debug(String.format("The following error occurs: %s \n Empty value will be used",
                        ex.getMessage()));
                value = "";
            }
            result.add(new ContactTimeDTO().setPersonId(id).setTime(value));
        }
        return result;
    }

    @Override
    public void saveContactTime(ContactTimeDTO contactTimeDTO) {
        Person person = personService.getPerson(contactTimeDTO.getPersonId());
        if (person != null) {
            Context.refreshEntity(person); //person caching issue fix
        }
        validateContactTimeRequest(contactTimeDTO.getPersonId(), contactTimeDTO.getTime(), person);
        createOrUpdateAttributeValue(person, contactTimeDTO.getTime());
    }

    @Override
    public void saveContactTimes(List<ContactTimeDTO> contactTimeDTOs) {
        for (ContactTimeDTO contactTimeDTO : contactTimeDTOs) {
            saveContactTime(contactTimeDTO);
        }
    }

    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    public ActorServiceImpl setPersonService(PersonService personService) {
        this.personService = personService;
        return this;
    }

    public ActorServiceImpl setPatientService(PatientService patientService) {
        this.patientService = patientService;
        return this;
    }

    private Set<Actor> getRelationshipsBasedOnType(Person person, ActorType actorType, boolean isPatient) {
        Set<Actor> results = Collections.emptySet();
        RelationshipType relationshipType = actorType.getRelationshipType();
        if (RelationshipTypeDirection.A == actorType.getDirection()) {
            results = isPatient ?
                    this.getByToPerson(person, relationshipType) :
                    this.getByFromPerson(person, relationshipType);
        } else if (RelationshipTypeDirection.B == actorType.getDirection()) {
            results = isPatient ?
                    this.getByFromPerson(person, relationshipType) :
                    this.getByToPerson(person, relationshipType);
        }
        return results;
    }

    private Set<Actor> getByToPerson(Person person, RelationshipType relationshipType) {
        Set<Actor> results = new LinkedHashSet<>();
        List<Relationship> relationships = Context.getPersonService().getRelationships(null,
                person, relationshipType);
        for (Relationship relationship : relationships) {
            if (!relationship.getPersonA().isVoided()) {
                results.add(new Actor(relationship.getPersonA(), relationship));
            }
        }
        return results;
    }

    private Set<Actor> getByFromPerson(Person person, RelationshipType relationshipType) {
        Set<Actor> results = new LinkedHashSet<>();
        List<Relationship> relationships = Context.getPersonService().getRelationships(person,
                null, relationshipType);
        for (Relationship relationship : relationships) {
            if (!relationship.getPersonB().isVoided()) {
                results.add(new Actor(relationship.getPersonB(), relationship));
            }
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

    private String getContactTimeValue(Person person) {
        PersonAttribute contactTime = person.getAttribute(ConfigConstants.PERSON_CONTACT_TIME_ATTRIBUTE_TYPE_NAME);
        String value = null;
        if (contactTime != null && StringUtils.isNotBlank(contactTime.getValue())) {
            value = contactTime.getValue();
        }
        return value;
    }

    private void validateContactTimeRequest(Integer personId, String time, Person person) {
        List<ErrorMessage> errors = new LinkedList<>();
        if (person == null) {
            errors.add(new ErrorMessage(personId.toString(),
                    String.format("Person with %d id doesn't exist.", personId)));
        }
        if (StringUtils.isBlank(time)) {
            errors.add(new ErrorMessage("time",
                    String.format("Missing time parameter for person id %d", personId)));
        } else {
            Matcher matcher = TIME_PATTERN.matcher(time);
            if (!matcher.matches()) {
                errors.add(new ErrorMessage("time",
                        String.format("Time %s is not match the required format HH:mm for person id %d", time, personId)));
            }
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private void createOrUpdateAttributeValue(Person person, String time) {
        PersonAttribute contactTime = PersonAttributeUtil.getBestContactTimeAttribute(person);
        if (contactTime == null) {
            PersonAttributeType attributeType =
                    personService.getPersonAttributeTypeByUuid(ConfigConstants.PERSON_CONTACT_TIME_TYPE_UUID);
            contactTime = new PersonAttribute(attributeType, time);
            person.addAttribute(contactTime);
        } else {
            contactTime.setValue(time);
        }
        personService.savePerson(person);
        Context.flushSession(); //person caching issue fix
    }
}
