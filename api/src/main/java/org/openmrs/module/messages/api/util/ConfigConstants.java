package org.openmrs.module.messages.api.util;

public final class ConfigConstants {

    public static final String ACTOR_TYPES_KEY = "messages.actor.types";

    public static final String ACTOR_TYPES_DEFAULT_VALUE = "";

    public static final String ACTOR_TYPES_DESCRIPTION = "Coma separated list of relationship types used to control the "
            + "list of possible targets of message (not including the patient as a target). Example of usage: "
            + "'relationshipTypeUuid:directionOfRelationship,secondRelationshipTypeUuid:directionOfRelationship'";

    public static final String MODULE_ID = "messages";

    public static final String PERSON_CONTACT_TIME_ATTRIBUTE_TYPE_NAME = "Best contact time";

    public static final String PERSON_CONTACT_TIME_TYPE_DESCRIPTION = "Person Best contact time";

    public static final String PERSON_CONTACT_TIME_TYPE_FORMAT = "java.util.Date";

    public static final String PERSON_CONTACT_TIME_TYPE_UUID = "7a8ad9aa-bd0e-48c0-826d-fa628f1db644";

    public static final String PATIENT_STATUS_ATTRIBUTE_TYPE_NAME = "Patient status";

    public static final String PATIENT_STATUS_ATTRIBUTE_TYPE_DESCRIPTION = "Patient status attribute";

    public static final String PATIENT_STATUS_ATTRIBUTE_TYPE_FORMAT = "java.lang.String";

    public static final String PATIENT_STATUS_ATTRIBUTE_TYPE_UUID = "dda246c6-c806-402a-9b7c-e2c1574a6441";

    public static final String CONSENT_CONTROL_KEY = "message.consent.validation";

    public static final String CONSENT_CONTROL_DEFAULT_VALUE = "false";

    public static final String CONSENT_CONTROL_DESCRIPTION = "Used to determine if Patient Consent (Patient Status) "
            + "should be used or not. Possible values: true / false.";

    public static final String BEST_CONTACT_TIME_KEY = "message.bestContactTime.default";

    public static final String BEST_CONTACT_TIME_DEFAULT_VALUE = "10:00";

    public static final String BEST_CONTACT_TIME_DESCRIPTION = "Used to determine the default contact time for services.";

    private ConfigConstants() {
    }
}
