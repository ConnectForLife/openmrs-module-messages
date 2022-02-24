package org.openmrs.module.messages.api.util;

import org.openmrs.api.context.Context;

public class ValidationUtil {
  private ValidationUtil() {}

  public static String resolveErrorCode(String errorCode, Object... args) {
    return Context.getMessageSourceService().getMessage(errorCode, args, Context.getLocale());
  }
}
