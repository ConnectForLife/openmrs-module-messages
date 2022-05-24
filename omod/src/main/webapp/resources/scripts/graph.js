var responseGraph = window.responseGraph || {};
var groupBarChart = window.groupBarChart;

responseGraph.load = function(mainDiv, config) {

  jq(mainDiv).empty();
  jq(config.loadingMessage).show();
  const url = '/' + OPENMRS_CONTEXT_PATH + '/messages/patientdashboard/graph/getData.action';
  jq.ajax({
    url: url,
    type: 'POST',
    dataType: "json",
    data: {graphConfig: JSON.stringify(config.requestConfig)},
    success: function(data) {
        if (responseGraph.checkIfNotEmpty(data, config)) {
          const barChartConfig = {
            fragmentId: config.fragmentId,
            mainDiv: mainDiv,
            colorRange: config.colorRange,
            data: responseGraph.extractChartData(data, config),
            columnsInfo: config.columnsInfo,
            xAxis: config.groupByAlias,
            label: responseGraph.prepareLabels(config),
            requireLegend: true
          };
          new groupBarChart(barChartConfig);
        } else {
          jq(config.noContentMessage).show();
        }
    },
    error: function() {
      jq(config.noContentMessage).show();
    },
    complete: function() {
      jq(config.loadingMessage).hide();
    }
  });
};

responseGraph.prepareLabels = function(config) {
  let labels = {};
  if (config.xAxisLabel) {
    labels.xAxis = config.xAxisLabel;
  }
  if (config.yAxisLabel) {
    labels.yAxis = config.yAxisLabel;
  }
  return labels;
};

responseGraph.extractChartData = function(resultGraphDTOList, config) {
      let output = [];
       if (config.groupByAlias == "null") {
            resultGraphDTOList.forEach((item) => {
                let chartItem = {};
                chartItem[item.alias] = item.result;
                output.push(chartItem);
            });
       } else {
          resultGraphDTOList.forEach((item) => {
                let configMap = item.configMap;
                let numberOfResponses = configMap[config.countResultAlias];
                let responseAlias = configMap[config.responseAlias];
                let groupByAlias = configMap[config.groupByAlias];

                let chartItem = {};
                chartItem[config.groupByAlias] = groupByAlias;
                chartItem[responseAlias] = numberOfResponses;
                output.push(chartItem);
              });
       }

      return output;
};

responseGraph.checkIfNotEmpty = function(results, config) {
    let counter = 0;
    if (config.groupByAlias == "null") {
        results.forEach((item) => {
            counter = counter + item.result;
       });
    } else {
        results.forEach((item) => {
            let configMap = item.configMap;
            counter = counter + configMap[config.countResultAlias];
       });
    }

    return counter > 0 ? true : false;
};

