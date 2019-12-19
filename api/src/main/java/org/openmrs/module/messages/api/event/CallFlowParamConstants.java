package org.openmrs.module.messages.api.event;

public final class CallFlowParamConstants {
    
    /**
     * Recipient's phone number
     */
    public static final String PHONE = "phone";
    
    /**
     * Config that was used for this call
     */
    public static final String CONFIG = "config";

    /**
     * Flow that was used for this call
     */
    public static final String FLOW_NAME = "flowName";

    /**
     * Map of additional parameters
     */
    public static final String ADDITIONAL_PARAMS = "params";

    /**
     * Message id of the call
     */
    public static final String MESSAGE_ID = "message_id";

    /**
     * Used service name
     */
    public static final String SERVICE_NAME = "service";

    /**
     * Services used in multiple call flow
     */
    public static final String SERVICES = "services";

    /**
     * Service objects used in multiple call flow. Alternative for SERVICES.
     * Might be required, for now we will send both
     */
    public static final String SERVICE_OBJECTS = "services_objects";

    private CallFlowParamConstants() {

    }
}
