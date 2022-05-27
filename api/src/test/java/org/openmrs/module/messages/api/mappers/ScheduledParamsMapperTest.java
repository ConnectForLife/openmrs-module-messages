package org.openmrs.module.messages.api.mappers;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;
import org.openmrs.module.messages.api.execution.ServiceResult;
import org.openmrs.module.messages.api.model.ScheduledServiceParameter;
import org.openmrs.module.messages.api.model.types.ServiceStatus;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ScheduledParamsMapperTest {

  @Test
  public void shouldExtractScheduledServiceParameters() {
    ScheduledParamsMapper mapper = new ScheduledParamsMapper();

    List<ScheduledServiceParameter> actual =
        mapper.fromDto(createTestServiceResult(createValidAdditionalParams()));

    assertNotNull(actual);
    assertEquals(2, actual.size());
    assertTrue(
        actual.stream()
            .anyMatch(o -> StringUtils.equalsIgnoreCase("visitType", o.getParameterType())));
    assertTrue(
        actual.stream()
            .anyMatch(o -> StringUtils.equalsIgnoreCase("Clinical visit", o.getParameterValue())));
    assertTrue(
        actual.stream()
            .anyMatch(o -> StringUtils.equalsIgnoreCase("visitTime", o.getParameterType())));
    assertTrue(
        actual.stream()
            .anyMatch(o -> StringUtils.equalsIgnoreCase("Morning", o.getParameterValue())));
  }

  @Test
  public void shouldExtractScheduledServiceParametersAndSkipNullValues() {
    ScheduledParamsMapper mapper = new ScheduledParamsMapper();

    List<ScheduledServiceParameter> actual =
        mapper.fromDto(createTestServiceResult(createAdditionalParamsWithNullValues()));

    assertNotNull(actual);
    assertEquals(1, actual.size());
  }

  @Test(expected = MessagesRuntimeException.class)
  public void shouldThrowExceptionWhenComplexObjectIsPassedAsValue() {
    ScheduledParamsMapper mapper = new ScheduledParamsMapper();

    mapper.fromDto(createTestServiceResult(createAdditionalParamsWithComplexValues()));
  }

  private ServiceResult createTestServiceResult(Map<String, Object> additionalParams) {
    ServiceResult serviceResult = new ServiceResult();
    serviceResult.setExecutionDate(ZonedDateTime.now());
    serviceResult.setMessageId("TestId");
    serviceResult.setChannelType("SMS");
    serviceResult.setPatientId(100);
    serviceResult.setActorId(100);
    serviceResult.setServiceStatus(ServiceStatus.DELIVERED);
    serviceResult.setBestContactTime("10:30");
    serviceResult.setPatientTemplateId(5);
    serviceResult.setAdditionalParams(additionalParams);
    return serviceResult;
  }

  private HashMap<String, Object> createValidAdditionalParams() {
    HashMap<String, Object> params = new HashMap<>();
    params.put("visitType", "Clinical visit");
    params.put("visitTime", "Morning");
    return params;
  }

  private HashMap<String, Object> createAdditionalParamsWithNullValues() {
    HashMap<String, Object> params = new HashMap<>();
    params.put("visitType", null);
    params.put("visitTime", "Morning");
    return params;
  }

  private HashMap<String, Object> createAdditionalParamsWithComplexValues() {
    HashMap<String, Object> params = new HashMap<>();
    params.put("visitType", new ArrayList<>());
    params.put("visitTime", "Morning");
    return params;
  }
}
