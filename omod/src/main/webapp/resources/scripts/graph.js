responseGraph = responseGraph || {};
jq = jq || {};
OPENMRS_CONTEXT_PATH = OPENMRS_CONTEXT_PATH || '/openmrs';
groupBarChart = groupBarChart || function(c) {};

responseGraph.load = function(mainDiv, config) {

  jq(mainDiv).empty();
  jq(config.loadingMessage).show();
  var url = '/' + OPENMRS_CONTEXT_PATH + '/messages/patientdashboard/graph/getData.action';
  jq.ajax({
    url: url,
    type: 'POST',
    dataType: "json",
    data: {graphConfig: JSON.stringify(config.requestConfig)},
    success: function(data) {
        if (responseGraph.checkIfNotEmpty(data, config)) {
          var barChartConfig = {
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
  var labels = {};
  if (config.xAxisLabel) {
    labels.xAxis = config.xAxisLabel;
  }
  if (config.yAxisLabel) {
    labels.yAxis = config.yAxisLabel;
  }
  return labels;
};

responseGraph.extractChartData = function(resultGraphDTOList, config) {
      var output = [];
       if (config.groupByAlias == "null") {
            resultGraphDTOList.forEach((item) => {
                var chartItem = {};
                chartItem[item.alias] = item.result;
                output.push(chartItem);
            });
       } else {
          resultGraphDTOList.forEach((item) => {
                var configMap = item.configMap;
                var numberOfResponses = configMap[config.countResultAlias];
                var responseAlias = configMap[config.responseAlias];
                var groupByAlias = configMap[config.groupByAlias];

                var chartItem = {};
                chartItem[config.groupByAlias] = groupByAlias;
                chartItem[responseAlias] = numberOfResponses;
                output.push(chartItem);
              });
       }

      return output;
};

responseGraph.checkIfNotEmpty = function(results, config) {
    var counter = 0;
    if (config.groupByAlias == "null") {
        results.forEach((item) => {
            counter = counter + item.result;
       });
    } else {
        results.forEach((item) => {
            var configMap = item.configMap;
            counter = counter + configMap[config.countResultAlias];
       });
    }

    return counter > 0 ? true : false;
};

