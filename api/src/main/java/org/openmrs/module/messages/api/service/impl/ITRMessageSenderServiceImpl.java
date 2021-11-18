package org.openmrs.module.messages.api.service.impl;

import org.openmrs.module.messages.api.event.MessagesEvent;
import org.openmrs.module.messages.api.event.SmsEventParamConstants;
import org.openmrs.module.messages.api.model.itr.ITRMessage;
import org.openmrs.module.messages.api.model.itr.ITRSendContext;
import org.openmrs.module.messages.api.service.ITRConverterService;
import org.openmrs.module.messages.api.service.ITRMessageSenderService;
import org.openmrs.module.messages.api.service.ITRService;
import org.openmrs.module.messages.api.service.MessagesEventService;

import java.util.Map;

import static org.openmrs.module.messages.api.constants.MessagesConstants.SMS_INITIATE_EVENT;

/**
 * The default implementation of {@link ITRMessageSenderService}.
 */
public class ITRMessageSenderServiceImpl implements ITRMessageSenderService {
  private ITRService itrService;
  private ITRConverterService itrConverterService;
  private MessagesEventService messagesEventService;

  @Override
  public void sendMessage(String uuid, ITRSendContext sendContext) {
    final ITRMessage messageConcept = itrService.getMessageByUuid(uuid);
    final MessagesEvent messagesEvent = buildMessage(messageConcept, sendContext);
    messagesEventService.sendEventMessage(messagesEvent);
  }

  public void setItrService(ITRService itrService) {
    this.itrService = itrService;
  }

  public void setItrConverterService(ITRConverterService itrConverterService) {
    this.itrConverterService = itrConverterService;
  }

  public void setMessagesEventService(MessagesEventService messagesEventService) {
    this.messagesEventService = messagesEventService;
  }

  private MessagesEvent buildMessage(ITRMessage messageConcept, ITRSendContext sendContext) {
    final Map<String, Object> smsEventParams = itrConverterService.convertToSmsEvent(messageConcept, sendContext);
    smsEventParams.put(SmsEventParamConstants.CONFIG, sendContext.getProviderConfigName());
    smsEventParams.put(SmsEventParamConstants.RECIPIENTS, sendContext.getRecipientsPhoneNumbers());
    return new MessagesEvent(SMS_INITIATE_EVENT, smsEventParams);
  }
}
