package org.openmrs.module.messages.fragment.controller.patientdashboard;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openmrs.Person;
import org.openmrs.module.messages.api.model.ActorResponse;
import org.openmrs.module.messages.api.service.ActorResponseService;
import org.openmrs.module.messages.model.GraphConfigurationDTO;
import org.openmrs.module.messages.util.WebConstants;
import org.openmrs.ui.framework.AttributeExpressionException;
import org.openmrs.ui.framework.fragment.FragmentConfiguration;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class ActorResponseGraphFragmentControllerTest {

    private static final String RESPONSE_MODE = "CONCEPT";

    private static final String REQUEST_CONFIGURATION_PROP = "requestConfiguration";

    private static final int PERSON_ID = 124;

    private static final String AGGREGATE_MODE = "WEEK";

    private static final int DATA_DATE_RANGE = 4;

    private static final List<Integer> POSSIBLE_RESPONSES = Arrays.asList(1, 3, 5);

    private static final List<String> POSSIBLE_TEXT_RESPONSES = Arrays.asList("Response 1", "Response 2");

    private static final int QUESTION_ID = 24;

    private static final String TEXT_QUESTION = "Question";

    private ActorResponseGraphFragmentController controller;

    private List<ActorResponse> expectedResponses;

    @Mock
    private ActorResponseService actorResponseService;

    @Before
    public void setUp() {
        controller = new ActorResponseGraphFragmentController();
        expectedResponses = Arrays.asList(new ActorResponse(), new ActorResponse());
        doReturn(expectedResponses).when(actorResponseService).findAllByCriteria(any());
    }

    @Test(expected = AttributeExpressionException.class)
    public void controllerRequireResponseMode() {
        controller.controller(new FragmentModel(), new FragmentConfiguration(), new Person());
    }

    @Test
    public void controllerSuccessfullyBuildRequestConfigurationForPatient() {
        FragmentModel expectedModel = buildFragmentModel();
        FragmentModel actual = new FragmentModel();
        controller.controller(actual, buildFragmentConfiguration(), buildPerson());
        assertThat(actual, is(expectedModel));
    }

    @Test
    public void getDataReturnExpectedResult() {
        List<ActorResponse> actual = controller.getData(buildGraphConfig(), actorResponseService);
        assertThat(actual, is(expectedResponses));
    }

    @Test
    public void getDataReturnEmptyListWhenGraphConfigIsNull() {
        List<ActorResponse> actual = controller.getData(null, actorResponseService);
        assertThat(actual, empty());
    }

    private Person buildPerson() {
        Person person = new Person();
        person.setId(PERSON_ID);
        return person;
    }

    private FragmentModel buildFragmentModel() {
        FragmentModel model = new FragmentModel();
        GraphConfigurationDTO configurationDTO = buildGraphConfig();
        model.addAttribute(REQUEST_CONFIGURATION_PROP, configurationDTO);
        return model;
    }

    private FragmentConfiguration buildFragmentConfiguration() {
        FragmentConfiguration configuration = new FragmentConfiguration();
        configuration.addAttribute(WebConstants.AGGREGATE_MODE, AGGREGATE_MODE);
        configuration.addAttribute(WebConstants.RESPONSE_MODE, RESPONSE_MODE);
        configuration.addAttribute(WebConstants.DATA_DATE_RANGE, DATA_DATE_RANGE);
        configuration.addAttribute(WebConstants.POSSIBLE_RESPONSES, POSSIBLE_RESPONSES);
        configuration.addAttribute(WebConstants.POSSIBLE_TEXT_RESPONSES, POSSIBLE_TEXT_RESPONSES);
        configuration.addAttribute(WebConstants.QUESTION_ID, QUESTION_ID);
        configuration.addAttribute(WebConstants.TEXT_QUESTION, TEXT_QUESTION);
        return configuration;
    }

    private GraphConfigurationDTO buildGraphConfig() {
        return new GraphConfigurationDTO()
                .setActorId(PERSON_ID)
                .setAggregateMode(AGGREGATE_MODE)
                .setDataDateRange(DATA_DATE_RANGE)
                .setPossibleResponses(POSSIBLE_RESPONSES)
                .setPossibleTextResponses(POSSIBLE_TEXT_RESPONSES)
                .setResponseMode(RESPONSE_MODE)
                .setQuestionId(QUESTION_ID)
                .setTextQuestion(TEXT_QUESTION);
    }
}
