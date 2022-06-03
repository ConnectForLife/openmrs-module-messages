/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.util;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.ServiceContext;
import org.openmrs.module.messages.api.constants.ConfigConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The VelocityContextUtil Class.
 *
 * <p>The VelocityContextUtil provides utilities to create a VelocityContext for Messages module.
 */
public class VelocityContextFactory {
  private static String SERVICE_MAP_ENTRY_DELIMITER = ",";
  private static String SERVICE_MAP_KEY_VALUE_DELIMITER = ":";
  /** List of classes to add an alias to VelocityContext. */
  private static final List<Class<?>> DEFAULT_CLASSES =
      Arrays.asList(
          String.class,
          Integer.class,
          Long.class,
          Float.class,
          Double.class,
          Date.class,
          SimpleDateFormat.class,
          Calendar.class,
          DateUtil.class,
          Math.class);

  private VelocityContextFactory() {}

  public static VelocityContext createDefaultContext() {
    final VelocityContext velocityContext = new VelocityContext();
    addDefaultClasses(velocityContext);
    addConfiguredBeans(velocityContext);
    return velocityContext;
  }

  private static void addDefaultClasses(VelocityContext velocityContext) {
    DEFAULT_CLASSES.forEach(
        defaultClass -> velocityContext.put(defaultClass.getSimpleName(), defaultClass));
  }

  private static void addConfiguredBeans(VelocityContext velocityContext) {
    final Map<String, String> serviceMap = getServiceMap();
    final List<String> notFoundBeans = new ArrayList<>();

    for (Map.Entry<String, String> serviceMapEntry : serviceMap.entrySet()) {
      final String contextKey = serviceMapEntry.getKey();
      final String serviceName = serviceMapEntry.getValue();

      final Object bean = getBean(serviceName);

      if (bean == null) {
        notFoundBeans.add(contextKey + SERVICE_MAP_ENTRY_DELIMITER + serviceName);
      } else {
        velocityContext.put(contextKey, bean);
      }
    }

    if (!notFoundBeans.isEmpty()) {
      throw new IllegalStateException(
          "Failed to load beans for Velocity Context: " + notFoundBeans);
    }
  }

  private static Map<String, String> getServiceMap() {
    final String parameterValue =
        Context.getAdministrationService()
            .getGlobalProperty(ConfigConstants.NOTIFICATION_TEMPLATE_INJECTED_SERVICES);

    if (StringUtils.isBlank(parameterValue)) {
      return Collections.emptyMap();
    }

    return Stream.of(parameterValue.split(SERVICE_MAP_ENTRY_DELIMITER))
        .map(String::trim)
        .collect(
            Collectors.toMap(
                VelocityContextFactory::getServiceMapEntryKey,
                VelocityContextFactory::getServiceMapEntryValue));
  }

  private static String getServiceMapEntryKey(String entry) {
    if (entry.contains(SERVICE_MAP_KEY_VALUE_DELIMITER)) {
      return entry.split(SERVICE_MAP_KEY_VALUE_DELIMITER)[0].trim();
    } else {
      return entry.trim();
    }
  }

  private static String getServiceMapEntryValue(String entry) {
    if (entry.contains(SERVICE_MAP_KEY_VALUE_DELIMITER)) {
      return entry.split(SERVICE_MAP_KEY_VALUE_DELIMITER)[1].trim();
    } else {
      return entry.trim();
    }
  }

  private static Object getBean(String beanName) {
    return ServiceContext.getInstance().getApplicationContext().getBean(beanName);
  }
}
