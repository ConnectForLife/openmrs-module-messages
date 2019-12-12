package org.openmrs.module.messages.web.controller;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.PatientService;
import org.openmrs.api.PersonService;
import org.openmrs.module.messages.api.actor.ActorService;
import org.openmrs.module.messages.api.dto.ActorDTO;
import org.openmrs.module.messages.api.exception.ValidationException;
import org.openmrs.module.messages.api.mappers.ActorMapper;
import org.openmrs.module.messages.api.util.ConfigConstants;
import org.openmrs.module.messages.api.util.PersonAttributeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping(value = "/messages/actor")
public class ActorController extends BaseRestController {

    @Autowired
    @Qualifier("messages.actorService")
    private ActorService actorService;

    @Autowired
    @Qualifier("messages.actorMapper")
    private ActorMapper actorMapper;

    @Autowired
    @Qualifier("patientService")
    private PatientService patientService;

    @Autowired
    @Qualifier("personService")
    private PersonService personService;

    private static Pattern timePtr = Pattern.compile("^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$");

    @RequestMapping(value = "/{patientId}", method = RequestMethod.GET) //TODO should be changed into PatientUUID
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ActorDTO> getAllForPatient(@PathVariable Integer patientId) {
        Patient patient = patientService.getPatient(patientId);
        if (patient == null) {
            throw new ValidationException(String.format("Patient with %s id doesn't exist.", patientId));
        }
        return actorMapper.toDtos(actorService.getAllActorsForPatient(patient));
    }

    @RequestMapping(value = "/{personId}/contact-time", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getBestContactTime(@PathVariable Integer personId) {
        Person person = personService.getPerson(personId);
        if (person == null) {
            throw new ValidationException(String.format("Person with %d id doesn't exist.", personId));
        }
        PersonAttribute contactTime = person.getAttribute(ConfigConstants.PERSON_CONTACT_TIME_ATTRIBUTE_TYPE_NAME);
        if (contactTime == null || StringUtils.isBlank(contactTime.getValue())) {
            return null;
        }
        return contactTime.getValue();
    }

    @RequestMapping(value = "/{personId}/contact-time", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void setBestContactTime(@PathVariable Integer personId, @RequestParam String time) {
        Person person = personService.getPerson(personId);
        validateContactTimeRequest(personId, time, person);
        createOrUpdateAttriuteValue(time, person);
        personService.savePerson(person);
    }

    private void createOrUpdateAttriuteValue(String time, Person person) {
        PersonAttribute contactTime = PersonAttributeUtil.getBestContactTimeAttribute(person);
        if (contactTime == null) {
            PersonAttributeType attributeType =
                    personService.getPersonAttributeTypeByUuid(ConfigConstants.PERSON_CONTACT_TIME_TYPE_UUID);
            contactTime = new PersonAttribute(attributeType, time);
            person.addAttribute(contactTime);
        } else {
            contactTime.setValue(time);
        }
    }

    private void validateContactTimeRequest(Integer personId, String time, Person person) {
        if (person == null) {
            throw new ValidationException(String.format("Person with %d id doesn't exist.", personId));
        }
        if (StringUtils.isBlank(time)) {
            throw new ValidationException("Missing date parameter");
        }
        Matcher mtch = timePtr.matcher(time);
        if (!mtch.matches()) {
            throw new ValidationException(String.format("Date %s is not match the required format HH:mm", time));
        }
    }
}
