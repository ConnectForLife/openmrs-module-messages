package org.openmrs.module.messages.fragment.controller.patientdashboard;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.Person;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.messages.api.dto.GraphResultDTO;
import org.openmrs.module.messages.api.execution.ExecutionException;
import org.openmrs.module.messages.api.service.GraphService;
import org.openmrs.module.messages.model.GraphConfigurationDTO;
import org.openmrs.module.messages.util.WebConstants;
import org.openmrs.module.reporting.dataset.definition.service.DataSetDefinitionService;
import org.openmrs.ui.framework.fragment.FragmentConfiguration;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class GraphFragmentControllerTest {

    private static final String REQUEST_CONFIGURATION_PROP = "requestConfiguration";

    private static final int PERSON_ID = 124;

    private static final String GRAPH_DATA_SET_TEST_NAME = "graphDataSetTestName";

    private GraphFragmentController controller;

    private List<Object> resultList;

    @Mock
    private GraphService graphService;

    @Mock
    private AdministrationService administrationService;

    @Before
    public void setUp() throws ExecutionException {
        mockStatic(Context.class);
        when(Context.getAdministrationService()).thenReturn(administrationService);
        controller = new GraphFragmentController();
        resultList = Arrays.asList(buildResultMap());
        doReturn(resultList).when(graphService).processQuery(any(), any());
    }

    @Test
    public void controllerSuccessfullyBuildRequestConfigurationForPatient() {
        FragmentModel expectedModel = buildFragmentModel();
        FragmentModel actual = new FragmentModel();
        controller.controller(actual, buildFragmentConfiguration(), buildPerson());
        assertThat(actual, is(expectedModel));
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
        configuration.addAttribute(WebConstants.GRAPH_DATA_SET_NAME, GRAPH_DATA_SET_TEST_NAME);
        return configuration;
    }

    private GraphConfigurationDTO buildGraphConfig() {
        return new GraphConfigurationDTO()
                .setActorId(PERSON_ID)
                .setGraphDataSetName(GRAPH_DATA_SET_TEST_NAME);
    }

    private Object buildResultMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("alias1", new BigInteger("1"));
        map.put("alias2", new BigInteger("2"));

        return map;
    }
}
