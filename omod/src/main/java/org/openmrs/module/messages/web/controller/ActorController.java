package org.openmrs.module.messages.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.openmrs.Person;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.dto.ActorDTO;
import org.openmrs.module.messages.api.dto.ActorTypeDTO;
import org.openmrs.module.messages.api.dto.ContactTimeDTO;
import org.openmrs.module.messages.api.dto.DefaultContactTimeDTO;
import org.openmrs.module.messages.api.exception.ValidationException;
import org.openmrs.module.messages.api.mappers.ActorMapper;
import org.openmrs.module.messages.api.mappers.ActorTypeMapper;
import org.openmrs.module.messages.api.service.ActorService;
import org.openmrs.module.messages.api.util.BestContactTimeHelper;
import org.openmrs.module.messages.web.model.DefaultContactTimeWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * Exposes the endpoints related to managing data for actor
 */
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
    @Qualifier("messages.actorTypeMapper")
    private ActorTypeMapper actorTypeMapper;

    @Autowired
    @Qualifier("personService")
    private PersonService personService;

    /**
     * Fetches all actors for person
     *
     * @param personId id of person
     * @param isPatient identifies whether person is a patient
     * @return list of DTO objects containing all actors related to the person
     */
    @RequestMapping(value = "/{personId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ActorDTO> getAllForPerson(
            @PathVariable String personId,
            @RequestParam(value = "isPatient", defaultValue = "true") boolean isPatient) {
        validateId(personId);
        Person person = personService.getPerson(Integer.parseInt(personId));
        if (person == null) {
            throw new ValidationException(String.format("Person with %s id doesn't exist.", personId));
        }
        Context.refreshEntity(person); //person caching issue fix
        return actorMapper.toDtos(actorService.getAllActorsForPersonId(person.getId(), isPatient));
    }

    /**
     * Fetches best contact time for person
     *
     * @param personId id of person
     * @return best contact time as a text
     */
    @RequestMapping(value = "/{personId}/contact-time", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getBestContactTime(@PathVariable String personId) {
        validateId(personId);
        return actorService.getContactTime(Integer.parseInt(personId));
    }

    /**
     * Fetches best contact times for people
     *
     * @param personIds list of people ids
     * @return list of DTO objects containing best contact times data
     */
    @RequestMapping(value = "/contact-times", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ContactTimeDTO> getBestContactTimes(@RequestParam(value = "personIds[]") List<Integer> personIds) {
        return actorService.getContactTimes(personIds);
    }

    /**
     * Fetches all actor types
     *
     * @return list of DTO objects containing available actor types
     */
    @RequestMapping(value = "/types", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ActorTypeDTO> getAllActorTypes() {
        return actorTypeMapper.toDtos(actorService.getAllActorTypes());
    }

    /**
     * Fetches default best contact times
     *
     * @return list of DTO objects containing default best contact times data
     */
    @RequestMapping(value = "/contact-times/default", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<DefaultContactTimeDTO> getBestContactTimes() {
        return BestContactTimeHelper.getDefaultContactTimes();
    }

    /**
     * Saves default best contact times
     *
     * @param contactTimes list of default best contact times
     */
    @RequestMapping(value = "/contact-times/default", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void setBestContactTimes(@RequestBody DefaultContactTimeWrapper contactTimes) {
        BestContactTimeHelper.setDefaultContactTimes(contactTimes.getRecords());
    }

    /**
     * Saves best contact time
     *
     * @param contactTimeDTO DTO object containing best contact time data
     */
    @RequestMapping(value = "/contact-time", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void setBestContactTime(@RequestBody ContactTimeDTO contactTimeDTO) {
        actorService.saveContactTime(contactTimeDTO);
    }

    /**
     * Saves best contact times
     *
     * @param contactTimeDTOs list of DTO objects containing best contact times data
     */
    @RequestMapping(value = "/contact-times", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void setBestContactTimes(@RequestBody List<ContactTimeDTO> contactTimeDTOs) {
        List<ContactTimeDTO> contactTimes = convertContactTimeDTOs(contactTimeDTOs);
        actorService.saveContactTimes(contactTimes);
    }

    private void validateId(String id) {
        if (StringUtils.isBlank(id) || !StringUtils.isNumeric(id)) {
            throw new ValidationException("Missing id parameter.");
        }
    }

    private List<ContactTimeDTO> convertContactTimeDTOs(List<ContactTimeDTO> contactTimeDTOs) {
        List<ContactTimeDTO> result = contactTimeDTOs;
        // OpenMRS by default convert requested list into List<LinkedHashMap>
        if (!contactTimeDTOs.isEmpty() && !(contactTimeDTOs.get(0) instanceof ContactTimeDTO)) {
            ObjectMapper mapper = new ObjectMapper();
            result = mapper.convertValue(contactTimeDTOs, new TypeReference<List<ContactTimeDTO>>() {
            });
        }
        return result;
    }
}
