package org.openmrs.module.messages.api.dto;

import java.util.Map;

public class GraphResultDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    private Integer result;

    private String alias;

    private transient Map<String, Object> configMap;

    public Integer getResult() {
        return result;
    }

    public GraphResultDTO setResult(Integer result) {
        this.result = result;
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public GraphResultDTO setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public Map<String, Object> getConfigMap() {
        return configMap;
    }

    public GraphResultDTO setConfigMap(Map<String, Object> configMap) {
        this.configMap = configMap;
        return this;
    }
}
