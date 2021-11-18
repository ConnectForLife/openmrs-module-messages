package org.openmrs.module.messages.api.service;

import org.openmrs.module.messages.api.model.itr.ITRContext;
import org.openmrs.module.messages.api.model.itr.ITRMessage;

import java.util.Map;

/**
 * The ITRConverterService Class.
 * <p>The ITRConverter service allows to convert the ITRMessage to different representations fit for API of other
 * modules.
 */
public interface ITRConverterService {
  Map<String, Object> convertToSmsEvent(ITRMessage message, ITRContext context);
}
