package org.openmrs.module.messages.api.service.impl;

import org.codehaus.jackson.map.ObjectMapper;
import org.openmrs.api.APIException;
import org.openmrs.module.messages.api.event.SmsEventParamConstants;
import org.openmrs.module.messages.api.model.itr.ITRMessage;
import org.openmrs.module.messages.api.model.itr.ITRResponseContext;
import org.openmrs.module.messages.api.model.itr.impl.ITRResponseContextBuilder;
import org.openmrs.module.messages.api.service.ITRConverterService;
import org.openmrs.module.messages.api.service.ITRScriptUtilsService;
import org.openmrs.module.messages.api.service.ITRService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.text.MessageFormat.format;
import static java.util.Objects.requireNonNull;

/**
 * The default implementation of ITRScriptUtilsService.
 */
public class ITRScriptUtilsServiceImpl implements ITRScriptUtilsService {
  private final static String MESSAGE_PROP = "message";
  private final static String CUSTOM_PARAMETERS_PROP = "customParameters";

  private ITRService itrService;
  private ITRConverterService itrMessageConverterService;

  @Override
  public ITRResponseContextBuilder getContextBuilder() {
    return new ITRResponseContextBuilder();
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  @Override
  public String getITRResponseMessageJson(ITRResponseContext responseContext) {
    requireNonNull(responseContext);

    final ITRMessage responseMessage =
        itrService.findResponse(responseContext.getReceivedText()).orElseGet(() -> itrService.getDefaultResponse());

    final Map<String, Object> smsEventMessageData =
        itrMessageConverterService.convertToSmsEvent(responseMessage, responseContext);

    final Map<String, Object> responseMessageJson = new HashMap<>();
    responseMessageJson.put(MESSAGE_PROP, smsEventMessageData.get(SmsEventParamConstants.MESSAGE));
    responseMessageJson.put(CUSTOM_PARAMETERS_PROP, smsEventMessageData.get(SmsEventParamConstants.CUSTOM_PARAMS));

    try {
      final ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.writeValueAsString(responseMessageJson);
    } catch (IOException ioe) {
      throw new APIException(
          format("Failed to serialize ITR response for message: {0} and response Map: " + "{1}", responseMessage.getName(),
              Objects.toString(responseMessageJson)), ioe);
    }
  }

  public void setItrService(ITRService itrService) {
    this.itrService = itrService;
  }

  public void setItrMessageConverterService(ITRConverterService itrMessageConverterService) {
    this.itrMessageConverterService = itrMessageConverterService;
  }
}
