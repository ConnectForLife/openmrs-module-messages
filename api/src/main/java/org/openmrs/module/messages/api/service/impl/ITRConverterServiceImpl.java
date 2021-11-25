package org.openmrs.module.messages.api.service.impl;

import org.openmrs.module.messages.api.event.SmsEventParamConstants;
import org.openmrs.module.messages.api.model.itr.ITRContext;
import org.openmrs.module.messages.api.model.itr.ITRMessage;
import org.openmrs.module.messages.api.service.ITRConverterService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static java.text.MessageFormat.format;

/**
 * The default implementation of ITRConverterService.
 */
public class ITRConverterServiceImpl implements ITRConverterService {
  private final Map<String, BiFunction<ITRMessage, ITRContext, Map<String, Object>>> convertersByType = new HashMap<>();

  public ITRConverterServiceImpl() {
    convertersByType.put(ITRMessage.TEXT_TYPE, this::createForMessageText);
    convertersByType.put(ITRMessage.TEMPLATE_TYPE, this::createForTemplate);
    convertersByType.put(ITRMessage.IMAGE_TYPE, this::createForImage);
  }

  @Override
  public Map<String, Object> convertToSmsEvent(ITRMessage message, ITRContext context) {
    return convertersByType.get(message.getType()).apply(message, context);
  }

  private Map<String, Object> getDefaultSmsEventData(ITRMessage message) {
    final Map<String, Object> customParams = new HashMap<>();
    customParams.put("type", message.getType());

    final Map<String, Object> result = new HashMap<>();
    result.put(SmsEventParamConstants.CUSTOM_PARAMS, customParams);
    return result;
  }

  private Map<String, Object> createForMessageText(ITRMessage message, ITRContext context) {
    final List<String> parameterValues = message.resolveParameterValues(context.toContextParameters());

    final Map<String, Object> result = getDefaultSmsEventData(message);
    result.put(SmsEventParamConstants.MESSAGE,
        format(message.getMessageText(context.getLocale()), parameterValues.toArray()));
    return result;
  }

  private Map<String, Object> createForTemplate(ITRMessage message, ITRContext context) {
    final List<String> parameterValues = message.resolveParameterValues(context.toContextParameters());

    final Map<String, Object> result = getDefaultSmsEventData(message);
    result.put(SmsEventParamConstants.MESSAGE, message.getProviderTemplateName().orElse(""));
    ((Map<String, Object>) result.get(SmsEventParamConstants.CUSTOM_PARAMS)).put("parameterValues", parameterValues);
    return result;
  }

  private Map<String, Object> createForImage(ITRMessage message, ITRContext context) {
    final List<String> parameterValues = message.resolveParameterValues(context.toContextParameters());

    final Map<String, Object> result = getDefaultSmsEventData(message);
    result.put(SmsEventParamConstants.MESSAGE,
        format(message.getMessageText(context.getLocale()), parameterValues.toArray()));
    ((Map<String, Object>) result.get(SmsEventParamConstants.CUSTOM_PARAMS)).put("imageURL",
        message.getImageURL().orElse(""));
    return result;
  }
}
