package org.openmrs.module.messages.fragment.controller.patientdashboard;

import org.openmrs.Person;
import org.openmrs.module.messages.api.model.ActorResponse;
import org.openmrs.module.messages.api.service.ActorResponseService;
import org.openmrs.module.messages.domain.criteria.ActorResponseCriteria;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentConfiguration;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public class ActorResponseGraphFragmentController {

    private static final String PATIENT_ID = "patientId";

    private static final String QUESTION_ID = "questionId";

    private static final String TEXT_QUESTION = "textQuestion";

    private static final String POSSIBLE_RESPONSES = "possibleResponses";

    private static final String POSSIBLE_TEST_RESPONSES = "possibleTestResponses";

    private static final String RESPONSE_MODE = "responseMode";

    private static final String DATA_DATE_RANGE = "dataDateRange";

    public void controller(FragmentModel model, FragmentConfiguration configuration,
            @FragmentParam("patientId") Person person,
            @SpringBean("messages.actorResponseService") ActorResponseService actorResponseService) {
        buildRequestConfiguration(model, configuration, person);
    }

    @SuppressWarnings({"checkstyle:parameterNumber", "PMD.ExcessiveParameterList"})
    public List<ActorResponse> getData(
            @RequestParam(value = "actorId", required = false) Integer actorId,
            @RequestParam(value = "patientId", required = false) Integer patientId,
            @RequestParam(value = "questionId", required = false) Integer questionId,
            @RequestParam(value = "textQuestion", required = false) String textQuestion,
            @RequestParam(value = "possibleResponses", required = false) List<Integer> possibleResponses,
            @RequestParam(value = "possibleTestResponses", required = false) List<String> possibleTestResponses,
            @RequestParam(value = "responseMode", required = false) String responseMode,
            @RequestParam(value = "dataDateRange", required = false) Integer dataDateRange,
            @SpringBean("messages.actorResponseService") ActorResponseService actorResponseService) {
        return actorResponseService.findAllByCriteria(new ActorResponseCriteria()
            .setActorId(actorId)
            .setPatientId(patientId)
            .setQuestionId(questionId)
            .setTextQuestion(textQuestion)
            .setPossibleResponses(possibleResponses)
            .setPossibleTestResponses(possibleTestResponses)
            .setResponseMode(responseMode)
            .setDataDateRange(dataDateRange));
    }

    private void buildRequestConfiguration(FragmentModel model, FragmentConfiguration configuration, Person person) {
        configuration.require(RESPONSE_MODE);
        SimpleObject requestConfiguration = new SimpleObject();
        requestConfiguration.put("actorId", (person == null) ? null : person.getPersonId());
        requestConfiguration.put("patientId", configuration.getAttribute(PATIENT_ID));
        requestConfiguration.put("questionId", configuration.getAttribute(QUESTION_ID));
        requestConfiguration.put("dataDateRange", configuration.getAttribute(DATA_DATE_RANGE));
        requestConfiguration.put("responseMode", configuration.getAttribute(RESPONSE_MODE));
        requestConfiguration.put("textQuestion", configuration.getAttribute(TEXT_QUESTION));
        requestConfiguration.put("possibleResponses", configuration.getAttribute(POSSIBLE_RESPONSES));
        requestConfiguration.put("possibleTestResponses", configuration.getAttribute(POSSIBLE_TEST_RESPONSES));
        model.addAttribute("requestConfiguration", requestConfiguration);
    }
}
