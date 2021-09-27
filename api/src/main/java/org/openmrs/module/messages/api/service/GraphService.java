package org.openmrs.module.messages.api.service;

import org.openmrs.module.messages.api.execution.ExecutionException;

import java.util.List;
import java.util.Map;

public interface GraphService {

    List<Object> processQuery(String sqlQuery, Map<String, Object> queryParams) throws ExecutionException;
}
