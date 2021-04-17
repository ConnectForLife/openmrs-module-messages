package org.openmrs.module.messages.api.model;

import java.util.Map;

public class GraphConfig {

    private String graphConfigName;

    private String query;

    private Map<String, Object> queryParams;

    public String getGraphConfigName() {
        return graphConfigName;
    }

    public void setGraphConfigName(String graphConfigName) {
        this.graphConfigName = graphConfigName;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Map<String, Object> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(Map<String, Object> queryParams) {
        this.queryParams = queryParams;
    }
}
