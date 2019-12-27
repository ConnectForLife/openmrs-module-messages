package org.openmrs.module.messages.api.constants;

import static org.openmrs.module.messages.api.util.ConfigConstants.MODULE_ID;

public final class MessagesConstants {

    public static final String SCHEDULER_SERVICE = "messages.schedulerService";

    public static final String DELIVERY_SERVICE = "messages.deliveryService";

    public static final String MESSAGING_GROUP_SERVICE = "messages.messagingGroupService";

    public static final String MESSAGING_SERVICE = "messages.messagingService";

    public static final String CONFIG_SERVICE = "messages.configService";

    public static final String PERSON_SERVICE = "personService";

    public static final String EVENT_SERVICE = "messages.messagesEventService";

    public static final String CALLFLOWS_DEFAULT_CONFIG = "voxeo"; //TODO: CFLM-473: Use global properties

    public static final String CALLFLOWS_DEFAULT_FLOW = "MainFlow"; //TODO: CFLM-473: Use global properties

    public static final String CALL_FLOW_SERVICE_RESULT_HANDLER_SERVICE = MODULE_ID +
        ".callFlowServiceResultHandlerService";
    public static final String SMS_SERVICE_RESULT_HANDLER_SERVICE = MODULE_ID +
        ".smsServiceResultHandlerService";

    public static final String DEFAULT_SERVER_SIDE_DATE_FORMAT = "yyyy-MM-dd";

    public static final String DEFAULT_FRONT_END_DATE_FORMAT = "dd MMM yyyy";

    public static final String SCHEDULED_GROUP_MAPPER = "messages.scheduledGroupMapper";

    private MessagesConstants() {
        // private. So can't be initialized
    }
}
