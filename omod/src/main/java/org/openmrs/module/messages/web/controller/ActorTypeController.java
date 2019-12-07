package org.openmrs.module.messages.web.controller;

import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.module.messages.api.actor.ActorService;
import org.openmrs.module.messages.api.dto.ActorDTO;
import org.openmrs.module.messages.api.exception.ValidationException;
import org.openmrs.module.messages.api.mappers.ActorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Controller
@RequestMapping(value = "/messages/actor")
public class ActorTypeController extends BaseRestController {

    @Autowired
    @Qualifier("messages.actorService")
    private ActorService actorService;

    @Autowired
    @Qualifier("messages.actorMapper")
    private ActorMapper actorMapper;

    @Autowired
    @Qualifier("patientService")
    private PatientService patientService;

    @RequestMapping(value = "/{patientId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ActorDTO> getAllForPatient(@PathVariable Integer patientId) {
        Patient patient = patientService.getPatient(patientId);
        if (patient == null) {
            throw new ValidationException(String.format("Patient with %s id doesn't exist.", patientId));
        }
        return actorMapper.toDtos(actorService.getAllActorsForPatient(patient));
    }
}
