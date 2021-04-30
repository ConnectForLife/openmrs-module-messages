package org.openmrs.module.messages.api.util;

import com.google.gson.JsonSyntaxException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.RelationshipType;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.messages.api.dto.DefaultContactTimeDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BestContactTimeHelper {

    private static final Log LOG = LogFactory.getLog(BestContactTimeHelper.class);

    public static void setDefaultContactTimes(List<DefaultContactTimeDTO> contactTimes) {
        Map<String, String> newValue = new HashMap<String, String>();

        for (DefaultContactTimeDTO contactTime : contactTimes) {
            newValue.put(contactTime.getActor(), contactTime.getTime());
        }

        setBestContactTimeProperty(JsonUtil.fromMap(newValue));
    }

    public static List<DefaultContactTimeDTO> getDefaultContactTimes() {
        List<DefaultContactTimeDTO> result = new ArrayList<>();
        Map<String, String> config = getBestContactTimeConfig();

        for (String actor : config.keySet()) {
            result.add(new DefaultContactTimeDTO(actor, config.get(actor)));
        }

        return result;
    }

    public static String getBestContactTime(Person person, RelationshipType relationshipType) {
        PersonAttribute contactTime = PersonAttributeUtil.getBestContactTimeAttribute(person);
        return contactTime != null ? contactTime.getValue() : getDefaultBestContactTime(relationshipType);
    }

    private static String getDefaultBestContactTime(RelationshipType relationshipType) {
        Map<String, String> config = getBestContactTimeConfig();
        if (relationshipType != null && config.containsKey(relationshipType.getUuid())) {
            return config.get(relationshipType.getUuid());
        }
        return getGlobalDefaultBestContactTime(config);
    }

    private static String getGlobalDefaultBestContactTime(Map<String, String> config) {
        return config.get(ConfigConstants.GLOBAL_BEST_CONTACT_TIME_KEY);
    }

    private static Map<String, String> getBestContactTimeConfig() {
        try {
            return JsonUtil.toMap(getBestContactTimeProperty(), JsonUtil.STRING_TO_STRING_MAP);
        } catch (JsonSyntaxException exception) {
            LOG.warn("Configuration of " + ConfigConstants.BEST_CONTACT_TIME_KEY +
                    " is incorrect. Using default value: " + ConfigConstants.BEST_CONTACT_TIME_DEFAULT_VALUE);
            return JsonUtil.toMap(ConfigConstants.BEST_CONTACT_TIME_DEFAULT_VALUE, JsonUtil.STRING_TO_STRING_MAP);
        }
    }

    private static String getBestContactTimeProperty() {
        return Context.getAdministrationService().getGlobalProperty(ConfigConstants.BEST_CONTACT_TIME_KEY);
    }

    private static void setBestContactTimeProperty(String newValue) {
        Context.getAdministrationService().setGlobalProperty(ConfigConstants.BEST_CONTACT_TIME_KEY, newValue);
    }

    private BestContactTimeHelper() {
    }
}
