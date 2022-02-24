package org.openmrs.module.messages.api.util;

import com.google.gson.JsonSyntaxException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.RelationshipType;
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

  public static String getBestContactTime(Person person, RelationshipType relationshipType) {
    final PersonAttribute contactTimeAttribute =
        PersonAttributeUtil.getBestContactTimeAttribute(person);

    final String contactTime;

    if (contactTimeAttribute == null) {
      final Optional<Concept> personCountry = PersonAddressUtil.getPersonCountry(person);
      contactTime = getDefaultBestContactTime(personCountry.orElse(null), relationshipType);
    } else {
      contactTime = contactTimeAttribute.getValue();
    }

    return contactTime;
  }

  private static String getDefaultBestContactTime(
      Concept country, RelationshipType relationshipType) {
    Map<String, String> config = getBestContactTimeConfig(country);
    if (relationshipType != null && config.containsKey(relationshipType.getUuid())) {
      return config.get(relationshipType.getUuid());
    }
    return getGlobalDefaultBestContactTime(config);
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

  private static String getGlobalDefaultBestContactTime(Map<String, String> config) {
    return config.get(ConfigConstants.GLOBAL_BEST_CONTACT_TIME_KEY);
  }

  private static void setDefaultBestContactTimeProperty(String newValue) {
    Context.getService(CountryPropertyService.class)
        .setCountryPropertyValue((Concept) null, ConfigConstants.BEST_CONTACT_TIME_KEY, newValue);
  }

  private BestContactTimeHelper() {}
}
