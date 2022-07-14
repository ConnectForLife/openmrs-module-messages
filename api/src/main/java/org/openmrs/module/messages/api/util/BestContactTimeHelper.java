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

import com.google.gson.JsonSyntaxException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.messages.api.dto.DefaultContactTimeDTO;
import org.openmrs.module.messages.api.service.CountryPropertyService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public final class BestContactTimeHelper {

  private static final Log LOG = LogFactory.getLog(BestContactTimeHelper.class);

  public static void setDefaultContactTimes(List<DefaultContactTimeDTO> contactTimes) {
    final Map<String, String> newValue =
        contactTimes.stream()
            .collect(toMap(DefaultContactTimeDTO::getActor, DefaultContactTimeDTO::getTime));

    setDefaultBestContactTimeProperty(JsonUtil.fromMap(newValue));
  }

  public static List<DefaultContactTimeDTO> getDefaultContactTimes() {
    final Map<String, String> config = getBestContactTimeConfig(null);

    return config.entrySet().stream()
        .map(entry -> new DefaultContactTimeDTO(entry.getKey(), entry.getValue()))
        .collect(toList());
  }

  public static String getBestContactTime(Person person) {
    final PersonAttribute contactTimeAttribute = PersonAttributeUtil.getBestContactTimeAttribute(person);

    return contactTimeAttribute != null ? contactTimeAttribute.getValue() : null;
  }

  private static Map<String, String> getBestContactTimeConfig(Concept country) {
    try {
      return JsonUtil.toMap(
          getBestContactTimeProperty(country)
              .orElse(ConfigConstants.BEST_CONTACT_TIME_DEFAULT_VALUE),
          JsonUtil.STRING_TO_STRING_MAP);
    } catch (JsonSyntaxException exception) {
      LOG.warn(
          "Configuration of "
              + ConfigConstants.BEST_CONTACT_TIME_KEY
              + " is incorrect. Using default value: "
              + ConfigConstants.BEST_CONTACT_TIME_DEFAULT_VALUE);

      return JsonUtil.toMap(
          ConfigConstants.BEST_CONTACT_TIME_DEFAULT_VALUE, JsonUtil.STRING_TO_STRING_MAP);
    }
  }

  private static Optional<String> getBestContactTimeProperty(Concept country) {
    return Context.getService(CountryPropertyService.class)
        .getCountryPropertyValue(country, ConfigConstants.BEST_CONTACT_TIME_KEY);
  }

  private static void setDefaultBestContactTimeProperty(String newValue) {
    Context.getService(CountryPropertyService.class)
        .setCountryPropertyValue((Concept) null, ConfigConstants.BEST_CONTACT_TIME_KEY, newValue);
  }

  private BestContactTimeHelper() {}
}
