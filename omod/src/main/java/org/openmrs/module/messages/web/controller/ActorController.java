package org.openmrs.module.messages.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.openmrs.Patient;
import org.openmrs.api.PatientService;
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
    @Qualifier("patientService")
    private PatientService patientService;

    @RequestMapping(value = "/{patientId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ActorDTO> getAllForPatient(@PathVariable String patientId) {
        validateId(patientId);
        Patient patient = patientService.getPatient(Integer.parseInt(patientId));
        if (patient == null) {
            throw new ValidationException(String.format("Patient with %s id doesn't exist.", patientId));
        }
        return actorMapper.toDtos(actorService.getAllActorsForPatient(patient));
    }

    @RequestMapping(value = "/{personId}/contact-time", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getBestContactTime(@PathVariable String personId) {
        validateId(personId);
        return actorService.getContactTime(Integer.parseInt(personId));
    }

    @RequestMapping(value = "/contact-times", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ContactTimeDTO> getBestContactTimes(@RequestParam(value = "personIds[]") List<Integer> personIds) {
        return actorService.getContactTimes(personIds);
    }

    @RequestMapping(value = "/types", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ActorTypeDTO> getAllActorTypes() {
        return actorTypeMapper.toDtos(actorService.getAllActorTypes());
    }

    @RequestMapping(value = "/contact-times/default", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<DefaultContactTimeDTO> getBestContactTimes() {
        return BestContactTimeHelper.getDefaultContactTimes();
    }

    @RequestMapping(value = "/contact-times/default", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void setBestContactTimes(@RequestBody DefaultContactTimeWrapper contactTimes) {
        BestContactTimeHelper.setDefaultContactTimes(contactTimes.getRecords());
    }

    @RequestMapping(value = "/contact-time", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void setBestContactTime(@RequestBody ContactTimeDTO contactTimeDTO) {
        actorService.saveContactTime(contactTimeDTO);
    }

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
            result = mapper.convertValue(contactTimeDTOs, new TypeReference<List<ContactTimeDTO>>() { });
        }
        return result;
    }
}
