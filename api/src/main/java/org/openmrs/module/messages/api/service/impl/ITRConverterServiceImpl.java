package org.openmrs.module.messages.api.service.impl;

import org.openmrs.module.messages.api.event.SmsEventParamConstants;
import org.openmrs.module.messages.api.model.itr.ITRContext;
import org.openmrs.module.messages.api.model.itr.ITRMessage;
import org.openmrs.module.messages.api.service.ITRConverterService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.text.MessageFormat.format;
import static java.util.Collections.singletonMap;

/**
 * The default implementation of ITRConverterService.
 */
public class ITRConverterServiceImpl implements ITRConverterService {
  @Override
  public Map<String, Object> convertToSmsEvent(ITRMessage message, ITRContext context) {
    final List<String> parameterValues = message.resolveParameterValues(context.toContextParameters());

    return message
        .getProviderTemplateName()
        .map(templateName -> createForTemplate(templateName, parameterValues))
        .orElse(createForMessageText(message.getMessageText(context.getLocale()), parameterValues));
  }

  private Map<String, Object> createForMessageText(String messageText, List<String> parameterValues) {
    final Map<String, Object> result = new HashMap<>();
    result.put(SmsEventParamConstants.MESSAGE, format(messageText, parameterValues.toArray()));
    return result;
  }

  private Map<String, Object> createForTemplate(String providerTemplateName, List<String> parameterValues) {
    final Map<String, Object> result = new HashMap<>();
    result.put(SmsEventParamConstants.MESSAGE, providerTemplateName);
    result.put(SmsEventParamConstants.CUSTOM_PARAMS, singletonMap("parameterValues", parameterValues));
    return result;
  }
}
