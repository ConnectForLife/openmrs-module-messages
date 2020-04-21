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

/**
 * Controller for managing adherence graph
 */
public class ActorResponseGraphFragmentController {

    private static final String ACTOR_ID = "actorId";

    private static final String PATIENT_ID = "patientId";

    private static final String QUESTION_ID = "questionId";

    private static final String TEXT_QUESTION = "textQuestion";

    private static final String POSSIBLE_RESPONSES = "possibleResponses";

    private static final String POSSIBLE_TEXT_RESPONSES = "possibleTextResponses";

    private static final String RESPONSE_MODE = "responseMode";

    private static final String AGGREGATE_MODE = "aggregateMode";

    private static final String DATA_DATE_RANGE = "dataDateRange";

    private static final String REQUEST_CONFIGURATION = "requestConfiguration";

    public void controller(FragmentModel model, FragmentConfiguration configuration,
            @FragmentParam("patientId") Person person,
            @SpringBean("messages.actorResponseService") ActorResponseService actorResponseService) {
        buildRequestConfiguration(model, configuration, person);
    }

    /**
     * Fetches actor responses which are used to display as a content of adherence graph
     *
     * @param actorId actor id
     * @param patientId person id
     * @param questionId id of concept containing adherence question
     * @param textQuestion text adherence question
     * @param possibleResponses list of concept ids containing adherence responses
     * @param possibleTextResponses list of possible text responses
     * @param responseMode response mode e.g. concept or text
     * @param aggregateMode aggregate mode e.g. week or month
     * @param dataDateRange number of aggregate mode units, e.g. 4 (weeks/months)
     * @param actorResponseService bean for ActorResponseService
     * @return list of actor responses for particular person from given date range
     */
    @SuppressWarnings({"checkstyle:parameterNumber", "PMD.ExcessiveParameterList"})
    public List<ActorResponse> getData(
            @RequestParam(value = ACTOR_ID, required = false) Integer actorId,
            @RequestParam(value = PATIENT_ID, required = false) Integer patientId,
            @RequestParam(value = QUESTION_ID, required = false) Integer questionId,
            @RequestParam(value = TEXT_QUESTION, required = false) String textQuestion,
            @RequestParam(value = POSSIBLE_RESPONSES + "[]", required = false) List<Integer> possibleResponses,
            @RequestParam(value = POSSIBLE_TEXT_RESPONSES + "[]", required = false) List<String> possibleTextResponses,
            @RequestParam(value = RESPONSE_MODE, required = false) String responseMode,
            @RequestParam(value = AGGREGATE_MODE, required = false) String aggregateMode,
            @RequestParam(value = DATA_DATE_RANGE, required = false) Integer dataDateRange,
            @SpringBean("messages.actorResponseService") ActorResponseService actorResponseService) {
        return actorResponseService.findAllByCriteria(new ActorResponseCriteria()
            .setActorId(actorId)
            .setPatientId(patientId)
            .setQuestionId(questionId)
            .setTextQuestion(textQuestion)
            .setPossibleResponses(possibleResponses)
            .setPossibleTextResponses(possibleTextResponses)
            .setResponseMode(responseMode)
            .setAggregateMode(aggregateMode)
            .setDataDateRange(dataDateRange));
    }

    private void buildRequestConfiguration(FragmentModel model, FragmentConfiguration configuration, Person person) {
        configuration.require(RESPONSE_MODE);
        SimpleObject requestConfiguration = new SimpleObject();
        requestConfiguration.put(ACTOR_ID, (person == null) ? null : person.getPersonId());
        requestConfiguration.put(PATIENT_ID, person.isPatient() ? configuration.getAttribute(PATIENT_ID) : null);
        requestConfiguration.put(QUESTION_ID, configuration.getAttribute(QUESTION_ID));
        requestConfiguration.put(DATA_DATE_RANGE, configuration.getAttribute(DATA_DATE_RANGE));
        requestConfiguration.put(RESPONSE_MODE, configuration.getAttribute(RESPONSE_MODE));
        requestConfiguration.put(AGGREGATE_MODE, configuration.getAttribute(AGGREGATE_MODE));
        requestConfiguration.put(TEXT_QUESTION, configuration.getAttribute(TEXT_QUESTION));
        requestConfiguration.put(POSSIBLE_RESPONSES, configuration.getAttribute(POSSIBLE_RESPONSES));
        requestConfiguration.put(POSSIBLE_TEXT_RESPONSES, configuration.getAttribute(POSSIBLE_TEXT_RESPONSES));
        model.addAttribute(REQUEST_CONFIGURATION, requestConfiguration);
    }
}
