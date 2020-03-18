package org.openmrs.module.messages.fragment.controller.patientdashboard;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Person;
import org.openmrs.module.messages.api.dto.ContactTimeDTO;
import org.openmrs.module.messages.api.model.Actor;
import org.openmrs.module.messages.api.service.ActorService;
import org.openmrs.module.messages.api.util.ActorUtil;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentConfiguration;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BestContactTimeFragmentController {

    private static final String LABEL_KEY = "label";

    private static final String TIME_KEY = "time";

    private static final String BEST_CONTACT_TIMES = "bestContactTimes";

    private static final String PATIENT_ID = "patientId";

    private static final String PERSON_ID = "personId";

    private static final String PERSON_UUID = "personUuid";

    private static final String MESSAGES_ACTOR_SERVICE = "messages.actorService";

    private static final String PATIENT = "Patient";

    private static final String PERSON_ACTOR_TYPE_LABEL = "personActorTypeLabel";

    public void controller(FragmentModel model, FragmentConfiguration configuration, UiUtils uiUtils,
            @RequestParam(PATIENT_ID) Person person, @SpringBean(MESSAGES_ACTOR_SERVICE) ActorService actorService) {
        if (person != null) {
            String personActorTypeLabel = getCurrentPersonActorTypeLabel(person,
                    configuration.getAttribute(PERSON_ACTOR_TYPE_LABEL), uiUtils);
            Map<Integer, String> actors = this.getAllActors(person, personActorTypeLabel, actorService);
            List<SimpleObject> bestContactTimes = this.createContactTimes(person, actors, actorService);
            model.addAttribute(BEST_CONTACT_TIMES, bestContactTimes);
        }
        model.addAttribute(PERSON_ID, (null == person) ? null : person.getPersonId());
        model.addAttribute(PERSON_UUID, (null == person) ? null : person.getUuid());
    }

    private List<SimpleObject> createContactTimes(Person person, Map<Integer, String> actors, ActorService actorService) {
        List<ContactTimeDTO> result = actorService.getContactTimes(new LinkedList<>(actors.keySet()));

        List<SimpleObject> bestContactTimes = new ArrayList<>();
        for (ContactTimeDTO contactTime : result) {
            SimpleObject entry = createContactTimeEntry(person, actors, contactTime);
            bestContactTimes.add(entry);
        }
        return bestContactTimes;
    }

    private SimpleObject createContactTimeEntry(Person person, Map<Integer, String> actors, ContactTimeDTO contactTime) {
        String actorType = actors.get(contactTime.getPersonId());
        String label = (person.getId().equals(contactTime.getPersonId())) ? actorType :
                String.format("%s %d", actorType, contactTime.getPersonId());
        SimpleObject entry = new SimpleObject();
        entry.put(LABEL_KEY, label);
        entry.put(TIME_KEY, contactTime.getTime());
        return entry;
    }

    private Map<Integer, String> getAllActors(Person person, String personActorTypeLabel,
            ActorService actorService) {
        Map<Integer, String> result = new LinkedHashMap<>();
        result.put(person.getPersonId(), personActorTypeLabel);
        List<Actor> actors = actorService.getAllActorsForPerson(person, person.isPatient());
        for (Actor actor : actors) {
            result.put(actor.getTarget().getPersonId(), ActorUtil.getActorTypeName(actor));
        }
        return result;
    }

    private String getCurrentPersonActorTypeLabel(Person person, Object attribute, UiUtils uiUtils) {
        String result = StringUtils.EMPTY;
        if (person.isPatient()) {
            result = PATIENT;
        } else if (null != attribute) {
            result = uiUtils.message((String) attribute);
        }
        return result;
    }
}
