package org.openmrs.module.messages.api.util;

import com.google.gson.JsonSyntaxException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.Relationship;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.messages.api.dto.DefaultContactTimeDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class BestContactTimeHelper {

    private static final Log LOG = LogFactory.getLog(BestContactTimeHelper.class);

    public static List<DefaultContactTimeDTO> getDefaultContactTimes() {
        List<DefaultContactTimeDTO> result = new ArrayList<>();
        Map<String, String> config = getBestContactTimeConfig();

        for (String actor : config.keySet()) {
            result.add(new DefaultContactTimeDTO(actor, config.get(actor)));
        }

        return result;
    }

    public static String getBestContactTime(Person person, Relationship relationship) {
        PersonAttribute contactTime = PersonAttributeUtil.getBestContactTimeAttribute(person);
        return contactTime != null ? contactTime.getValue() : getDefaultBestContactTime(relationship);
    }

    private static String getDefaultBestContactTime(Relationship relationship) {
        Map<String, String> config = getBestContactTimeConfig();
        if (relationship != null && config.containsKey(relationship.getRelationshipType().getUuid())) {
            return config.get(relationship.getRelationshipType().getUuid());
        }
        return getGlobalDefaultBestContactTime(config);
    }

    private static String getGlobalDefaultBestContactTime(Map<String, String> config) {
        return config.get(ConfigConstants.GLOBAL_BEST_CONTACT_TIME_KEY);
    }

    private static Map<String, String> getBestContactTimeConfig() {
        try {
            return MapperUtil.getAsStringToStringMap(getBestContactTimeProperty());
        } catch (JsonSyntaxException exception) {
            LOG.warn("Configuration of " + ConfigConstants.BEST_CONTACT_TIME_KEY +
                    " is incorrect. Using default value: " + ConfigConstants.BEST_CONTACT_TIME_DEFAULT_VALUE);
            return MapperUtil.getAsStringToStringMap(ConfigConstants.BEST_CONTACT_TIME_DEFAULT_VALUE);
        }
    }

    private static String getBestContactTimeProperty() {
        return Context.getAdministrationService().getGlobalProperty(ConfigConstants.BEST_CONTACT_TIME_KEY);
    }

    private BestContactTimeHelper() {
    }
}
