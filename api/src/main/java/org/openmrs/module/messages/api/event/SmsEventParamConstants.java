package org.openmrs.module.messages.api.event;

public final class SmsEventParamConstants {

    /**
     * List of recipients (phone numbers)
     */
    public static final String RECIPIENTS = "recipients";

    /**
     * the text content of the SMS message
     */
    public static final String MESSAGE = "message";

    /**
     * map of custom parameters
     */
    public static final String CUSTOM_PARAMS = "custom_params";

    /**
     * Message id of the scheduled service
     */
    public static final String MESSAGE_ID = "message_id";

    /**
     * Message group id of the scheduled service
     */
    public static final String MESSAGE_GROUP_ID = "message_group_id";

    /**
     * Used service name
     */
    public static final String SERVICE_NAME = "service";

    private SmsEventParamConstants() { }
}
