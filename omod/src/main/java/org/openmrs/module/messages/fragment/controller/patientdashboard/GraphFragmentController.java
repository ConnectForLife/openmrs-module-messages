package org.openmrs.module.messages.fragment.controller.patientdashboard;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Person;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.dto.GraphResultDTO;
import org.openmrs.module.messages.api.execution.ExecutionException;
import org.openmrs.module.messages.api.service.GraphService;
import org.openmrs.module.messages.model.GraphConfigurationDTO;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.service.DataSetDefinitionService;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentConfiguration;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for managing graph
 */
public class GraphFragmentController {

    private static final String REQUEST_CONFIGURATION = "requestConfiguration";

    private static final Log LOGGER = LogFactory.getLog(GraphFragmentController.class);

    public void controller(FragmentModel model, FragmentConfiguration configuration,
            @FragmentParam("patientId") Person person) {
        buildRequestConfiguration(model, configuration, person);
    }

    /**
     * Fetches data used to display as a content of graph
     *
     * @param graphConfig representation of {@link GraphConfigurationDTO}
     * @return list of {@link GraphResultDTO} objects
     */
    public List<GraphResultDTO> getData(
            @RequestParam(value = "graphConfig", required = false) GraphConfigurationDTO graphConfig,
            @SpringBean("messages.graphService") GraphService graphService,
            @SpringBean("reportingDataSetDefinitionService") DataSetDefinitionService dataSetDefinitionService)
            throws ExecutionException {
        if (graphConfig == null) {
            return new ArrayList<>();
        }

        List<Object> resultList = new ArrayList<>();
        DataSetDefinition dataSet = getDataSetByName(graphConfig.getGraphDataSetName(), dataSetDefinitionService);
        if (dataSet != null) {
            SqlDataSetDefinition sqlDataSetDefinition = (SqlDataSetDefinition) dataSet;
            resultList = graphService.processQuery(sqlDataSetDefinition.getSqlQuery(),
                    getMandatoryQueryParams(graphConfig));
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Definition data set with name %s not found", graphConfig.getGraphDataSetName()));
        }

        return buildResultsDTOList(resultList, graphConfig);
    }

    private void buildRequestConfiguration(FragmentModel model, FragmentConfiguration configuration, Person person) {
        model.addAttribute(REQUEST_CONFIGURATION, new GraphConfigurationDTO()
                .withFragmentConfiguration(configuration, person));
    }

    private List<GraphResultDTO> buildResultsDTOList(List<Object> resultList, GraphConfigurationDTO config) {
        List<GraphResultDTO> graphResultDTOList = new ArrayList<>();
        for (Object object : resultList) {
            Map<String, Object> map = (Map<String, Object>) object;
            if (StringUtils.isNotBlank(config.getGroupByAlias())) {
                graphResultDTOList.add(new GraphResultDTO()
                    .setConfigMap(map));
            } else {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    graphResultDTOList.add(new GraphResultDTO()
                        .setAlias(entry.getKey())
                        .setResult(((Number) entry.getValue()).intValue()));
                }
            }
        }
        return graphResultDTOList;
    }

    private DataSetDefinition getDataSetByName(String dataSetName, DataSetDefinitionService dataSetService) {
        List<DataSetDefinition> allDataSets = dataSetService.getAllDefinitions(false);
        DataSetDefinition dataSetDefinition = null;
        for (DataSetDefinition dataSet : allDataSets) {
            if (StringUtils.equalsIgnoreCase(dataSet.getName(), dataSetName)) {
                dataSetDefinition = dataSet;
                break;
            }
        }
        return dataSetDefinition;
    }

    private Map<String, Object> getMandatoryQueryParams(GraphConfigurationDTO config) {
        Map<String, Object> queryParams = new HashMap<>();
        Integer patientId = config.getPatientId();
        Integer actorId = config.getActorId();
        queryParams.put(MessagesConstants.PATIENT_ID_PARAM, patientId != null ? patientId : actorId);
        queryParams.put(MessagesConstants.ACTOR_ID_PARAM, actorId != null ? actorId : patientId);
        queryParams.put(MessagesConstants.PERSON_ID_PARAM, actorId);

        return queryParams;
    }
}
