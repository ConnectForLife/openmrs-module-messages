package org.openmrs.module.messages.fragment.controller.patientdashboard;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.Person;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.execution.ExecutionException;
import org.openmrs.module.messages.api.service.GraphService;
import org.openmrs.module.messages.model.GraphConfigurationDTO;
import org.openmrs.module.messages.util.WebConstants;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.service.DataSetDefinitionService;
import org.openmrs.ui.framework.fragment.FragmentConfiguration;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class GraphFragmentControllerTest {

  private static final String GRAPH_DATA_SET_TEST_NAME = "graphDataSetTestName";

  private final GraphFragmentController controller = new GraphFragmentController();

  @Mock private GraphService graphService;

  @Mock private AdministrationService administrationService;

  @Mock private FragmentModel fragmentModel;

  @Mock private DataSetDefinitionService dataSetDefinitionService;

  @Before
  public void setUp() throws ExecutionException {
    mockStatic(Context.class);
    when(Context.getAdministrationService()).thenReturn(administrationService);
  }

  @Test
  public void controllerSuccessfullyBuildRequestConfigurationForPatient() {
    controller.controller(fragmentModel, buildFragmentConfiguration(), new Person(100));

    verify(fragmentModel)
        .addAttribute(eq("requestConfiguration"), any(GraphConfigurationDTO.class));
  }

  @Test
  public void shouldGetControllerData() throws ExecutionException {
    when(graphService.processQuery(anyString(), anyMapOf(String.class, Object.class)))
        .thenReturn(Arrays.asList(buildResultMap()));
    when(dataSetDefinitionService.getAllDefinitions(false))
        .thenReturn(Arrays.asList(new SqlDataSetDefinition()));

    controller.getData(new GraphConfigurationDTO(), graphService, dataSetDefinitionService);

    verify(dataSetDefinitionService).getAllDefinitions(false);
    verify(graphService).processQuery(anyString(), anyMapOf(String.class, Object.class));
  }

  private FragmentConfiguration buildFragmentConfiguration() {
    FragmentConfiguration configuration = new FragmentConfiguration();
    configuration.addAttribute(WebConstants.GRAPH_DATA_SET_NAME, GRAPH_DATA_SET_TEST_NAME);
    return configuration;
  }

  private Object buildResultMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("alias1", new BigInteger("1"));
    map.put("alias2", new BigInteger("2"));

    return map;
  }
}
