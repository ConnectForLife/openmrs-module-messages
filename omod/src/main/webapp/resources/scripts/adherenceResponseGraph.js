var responseGraph = responseGraph || {};

responseGraph.load = function(mainDiv, config) {
  jq(mainDiv).empty();
  jq(config.loadingMessage).show();
  var url = '/' + OPENMRS_CONTEXT_PATH + '/messages/patientdashboard/actorResponseGraph/getData.action';
  jq.ajax({
    url: url,
    type: 'POST',
    dataType: "json",
    data: {graphConfig: JSON.stringify(config.requestConfig)},
    success: function(data) {
        if(data && data.length) {
          var barChartConfig = {
            fragmentId: config.fragmentId,
            mainDiv: mainDiv,
            colorRange: config.colorRange,
            data: responseGraph.extractChartData(data),
            columnsInfo: config.columnsInfo,
            xAxis: "over",
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

responseGraph.extractChartData = function(actorResponses) {
  var output = [];
  actorResponses.forEach((item) => {
      var existing = output.filter((value) => {
          return value.over === item.over;
      });
      if (existing.length) {
          var existingIndex = output.indexOf(existing[0]);
          output[existingIndex][item.response] = item.responseCount;
      } else {
          var chartItem = {};
          chartItem.over = item.over;
          chartItem[item.response] = item.responseCount;
          output.push(chartItem); 
      }
  });
  return output;
};
