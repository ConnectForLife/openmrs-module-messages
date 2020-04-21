package org.openmrs.module.messages.fragment.controller.patientdashboard;

import org.openmrs.Person;
import org.openmrs.module.messages.api.model.ActorResponse;
import org.openmrs.module.messages.api.service.ActorResponseService;
import org.openmrs.module.messages.domain.criteria.ActorResponseCriteria;
import org.openmrs.module.messages.model.GraphConfigurationDTO;
import org.openmrs.module.messages.util.WebConstants;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentConfiguration;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for managing adherence graph
 */
public class ActorResponseGraphFragmentController {

    private static final String REQUEST_CONFIGURATION = "requestConfiguration";

    public void controller(FragmentModel model, FragmentConfiguration configuration,
            @FragmentParam("patientId") Person person) {
        buildRequestConfiguration(model, configuration, person);
    }

    /**
     * Fetches actor responses which are used to display as a content of adherence graph
     *
     * @param graphConfig representation of {@link GraphConfigurationDTO}
     * @return list of actor responses for particular person from given date range
     */
    public List<ActorResponse> getData(
            @RequestParam(value = "graphConfig", required = false) GraphConfigurationDTO graphConfig,
            @SpringBean("messages.actorResponseService") ActorResponseService actorResponseService) {
        if (graphConfig == null) {
            return new ArrayList<>();
        }
        return actorResponseService.findAllByCriteria(new ActorResponseCriteria()
            .setActorId(graphConfig.getActorId())
            .setPatientId(graphConfig.getPatientId())
            .setQuestionId(graphConfig.getQuestionId())
            .setTextQuestion(graphConfig.getTextQuestion())
            .setPossibleResponses(graphConfig.getPossibleResponses())
            .setPossibleTextResponses(graphConfig.getPossibleTextResponses())
            .setResponseMode(graphConfig.getResponseMode())
            .setAggregateMode(graphConfig.getAggregateMode())
            .setDataDateRange(graphConfig.getDataDateRange()));
    }

    private void buildRequestConfiguration(FragmentModel model, FragmentConfiguration configuration, Person person) {
        configuration.require(WebConstants.RESPONSE_MODE);
        model.addAttribute(REQUEST_CONFIGURATION, new GraphConfigurationDTO()
                .withFragmentConfiguration(configuration, person));
    }
}
