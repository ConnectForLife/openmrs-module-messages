package org.openmrs.module.messages.api.service.impl;

import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.messages.api.model.ScheduledExecutionContext;
import org.openmrs.module.messages.api.model.ScheduledService;

/**
 * Implements methods related to the handling of WhatsApp service results
 */
public class WhatsAppServiceResultsHandlerServiceImpl extends AbstractTextMessageServiceResultsHandlerService {

  private static final String WHATSAPP_CHANNEL_TYPE = "WhatsApp";

  @Override
  public void handle(List<ScheduledService> whatsAppServices, ScheduledExecutionContext executionContext) {
    handleServices(whatsAppServices, executionContext);
    if (CollectionUtils.isNotEmpty(whatsAppServices)) {
      int groupId = executionContext.getGroupId();
      messagesExecutionService.executionCompleted(groupId, null, WHATSAPP_CHANNEL_TYPE);
    }
  }

  @Override
  protected String getConfigName(ScheduledExecutionContext executionContext) {
    return executionContext.getChannelConfiguration()
      .getOrDefault(CONFIG_KEY,
        Context.getAdministrationService()
          .getGlobalProperty(ConfigConstants.WHATSAPP_CONFIG_GP_KEY, ConfigConstants.WHATSAPP_CONFIG_GP_DEFAULT_VALUE));
  }
}
