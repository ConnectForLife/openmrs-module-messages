package org.openmrs.module.messages.api.util;

public final class ConfigConstants {

    public static final String ACTOR_TYPES_KEY = "messages.actor.types";

    public static final String ACTOR_TYPES_DEFAULT_VALUE = "";

    public static final String ACTOR_TYPES_DESCRIPTION = "Coma separated list of relationship types used to control the "
            + "list of possible targets of message (not including the patient as a target). Example of usage: "
            + "'relationshipTypeUuid:directionOfRelationship,secondRelationshipTypeUuid:directionOfRelationship'";

    public static final String MODULE_ID = "messages";

    public static final String PATIENT_STATUS_ATTRIBUTE_TYPE_NAME = "Patient status";

    public static final String PATIENT_STATUS_ATTRIBUTE_TYPE_DESCRIPTION = "Patient status attribute";

    public static final String PATIENT_STATUS_ATTRIBUTE_TYPE_FORMAT = "java.lang.String";

    public static final String PATIENT_STATUS_ATTRIBUTE_TYPE_UUID = "dda246c6-c806-402a-9b7c-e2c1574a6441";

    public static final String CONSENT_CONTROL_KEY = "message.consent.validation";

    public static final String CONSENT_CONTROL_DEFAULT_VALUE = "false";

    public static final String CONSENT_CONTROL_DESCRIPTION = "Used to determine if Patient Consent (Patient Status) "
            + "should be used or not. Possible values: true / false.";

    private ConfigConstants() {
    }
}
