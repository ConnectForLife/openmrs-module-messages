<%
    config.require("label")
    config.require("colorRange")
    config.require("columnsInfo")
    ui.includeJavascript("messages", "barChart.js")
    ui.includeJavascript("messages", "d3.v4.min.js")
    ui.includeJavascript("messages", "graph.js")
    ui.includeCss("messages", "graph.css")
%>
<div class="info-section">
    <div class="info-header">
        <i class="${config.icon}"></i>
        <h3>${ ui.message(config.label) }</h3>
    </div>

    <div class="info-body">
        <% if (config.additionalTitle) { %>
            <span class="graph-additional-title"> ${ ui.message(config.additionalTitle) }</span>
            <br />
        <% } %>
        <span id="chart-loading-${config.id}" style="display: none;">
            ${ ui.message("messages.loading") }
            <i class="icon-spinner icon-spin icon-2x" style="margin-left: 10px;"></i>
        </span>
        <span id="chart-no-content-${config.id}" style="display: none;">
            ${ ui.message("messages.noContent") }
        </span>
        <div id="chart-${config.id}">
        </div>
        <script>
            var chartConfig = {
                fragmentId: "${config.id}",
                columnsInfo: <%= groovy.json.JsonOutput.toJson(config.columnsInfo) %>,
                colorRange: <%= groovy.json.JsonOutput.toJson(config.colorRange) %>,
                 <% if (config.xAxisLabel) { %>
                    xAxisLabel: "${ ui.message(config.xAxisLabel) }",
                <% } %>
                <% if (config.yAxisLabel) { %>
                    yAxisLabel: "${ ui.message(config.yAxisLabel) }",
                <% } %>
                loadingMessage: "#chart-loading-${config.id}",
                noContentMessage: "#chart-no-content-${config.id}",
                responseAlias: "${config.responseAlias}",
                countResultAlias: "${config.countResultAlias}",
                groupByAlias: "${config.groupByAlias}",
                requestConfig: <%= groovy.json.JsonOutput.toJson(requestConfiguration) %>
            };

            responseGraph.load("#chart-${config.id}", chartConfig);
        </script>
    </div>
</div>
