package org.openmrs.module.messages.web.controller;

import org.openmrs.module.messages.api.dto.PersonStatusDTO;
import org.openmrs.module.messages.api.exception.MessagesObjectNotFound;
import org.openmrs.module.messages.api.util.PersonStatusHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exposes endpoints which can be used for managing the person status resources
 */
@Controller
@RequestMapping("/messages/person-statuses")
public class PersonStatusController extends BaseRestController {

    @Autowired
    @Qualifier("messages.personStatusHelper")
    private PersonStatusHelper personStatusHelper;

    @RequestMapping(value = "/{personId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PersonStatusDTO getPersonStatus(@PathVariable("personId") String personId) {
        PersonStatusDTO result = personStatusHelper.getStatus(personId);
        if (result == null) {
            throw new MessagesObjectNotFound(String.format("Could not fetch person status for personId: %s", personId));
        }
        return result;
    }

    @RequestMapping(value = "/{personId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updatePersonStatus(@PathVariable("personId") String personId, @RequestBody PersonStatusDTO personStatusDTO) {
        personStatusDTO.setPersonId(personId);
        personStatusHelper.saveStatus(personStatusDTO);
    }
}