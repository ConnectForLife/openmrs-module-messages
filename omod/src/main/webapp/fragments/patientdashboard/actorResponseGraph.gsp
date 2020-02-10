<%
    config.require("label")
    config.require("colorRange")
    config.require("columnsInfo")
    ui.includeJavascript("messages", "barChart.js")
    ui.includeJavascript("messages", "d3.v4.min.js")
    ui.includeJavascript("messages", "adherenceResponseGraph.js")
    ui.includeCss("messages", "actorResponseGraph.css")
%>
<div class="info-section">
    <div class="info-header">
        <h3>${ ui.message(config.label) }</h3>
    </div>

    <div class="info-body">
        <% if (config.additionalTitle) { %>
            <span class="actor-response-graph-title"> ${ ui.message(config.additionalTitle) }</span>
            <br />
        <% } %>
        <span id="adherence-chart-loading-${config.id}" style="display: none;">
            ${ ui.message("messages.loading") }
            <i class="icon-spinner icon-spin icon-2x" style="margin-left: 10px;"></i>
        </span>
        <span id="adherence-chart-no-content-${config.id}" style="display: none;">
            ${ ui.message("messages.noContent") }
        </span>
        <div id="chart-${config.id}">
        </div>
        <script>
            
            var chartConfig = {
                columnsInfo: <%= groovy.json.JsonOutput.toJson(config.columnsInfo) %>,
                colorRange: <%= groovy.json.JsonOutput.toJson(config.colorRange) %>,
                 <% if (config.xAxisLabel) { %>
                    xAxisLabel: "${ ui.message(config.xAxisLabel) }",
                <% } %>
                <% if (config.yAxisLabel) { %>
                    yAxisLabel: "${ ui.message(config.yAxisLabel) }",
                <% } %>
                loadingMessage: "#adherence-chart-loading-${config.id}",
                noContentMessage: "#adherence-chart-no-content-${config.id}",
                requestConfig: <%= groovy.json.JsonOutput.toJson(requestConfiguration) %>
            };

            responseGraph.load("#chart-${config.id}", chartConfig);
        </script>
    </div>
</div>
