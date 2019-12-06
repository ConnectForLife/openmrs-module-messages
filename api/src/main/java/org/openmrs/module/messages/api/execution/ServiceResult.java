package org.openmrs.module.messages.api.execution;

import org.openmrs.module.messages.api.model.types.ServiceStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a single execution for a service/message.
 */
public class ServiceResult implements Serializable {

    private static final long serialVersionUID = 2598236499107927781L;

    public static final String EXEC_DATE_ALIAS = "EXECUTION_DATE";
    public static final String MSG_ID_ALIAS = "MESSAGE_ID";
    public static final String CHANNEL_ID_ALIAS = "CHANNEL_ID";
    public static final String STATUS_COL_ALIAS = "STATUS_ID";
    public static final int MIN_COL_NUM = 3;
    public static final int MAX_COL_NUM = 4;

    private Date executionDate;
    private Object messageId;
    private Integer channelId;
    private ServiceStatus serviceStatus = ServiceStatus.FUTURE;
    private Map<String, Object> additionalParams = new HashMap<>();

    public static ServiceResult parse(Map<String, Object> row) {
        if (row.size() < MIN_COL_NUM || row.size() > MAX_COL_NUM) {
            throw new IllegalStateException("Invalid number of columns in result row: " + row.size());
        }

        Date date = null;
        Object msgId = null;
        Integer channel = null;
        ServiceStatus status = ServiceStatus.FUTURE;
        Map<String, Object> params = new HashMap<>();

        for (Map.Entry<String, Object> entry : row.entrySet()) {
            switch (entry.getKey()) {
                case EXEC_DATE_ALIAS:
                    date = (Date) entry.getValue();
                    break;
                case MSG_ID_ALIAS:
                    msgId = entry.getValue();
                    break;
                case CHANNEL_ID_ALIAS:
                    channel = (Integer) entry.getValue();
                    break;
                case STATUS_COL_ALIAS:
                    status = ServiceStatus.valueOf((String) entry.getValue());
                    break;
                default:
                     params.put(entry.getKey(), entry.getValue());
                     break;
            }
        }

        return new ServiceResult(date, msgId, channel, status, params);
    }

    public static List<ServiceResult> parseList(List<Map<String, Object>> list) {
        List<ServiceResult> resultList = new ArrayList<>();
        for (Map<String, Object> row : list) {
            ServiceResult result = ServiceResult.parse(row);
            resultList.add(result);
        }
        return resultList;
    }

    public ServiceResult() {
    }

    public ServiceResult(
            Date executionDate,
            Object messageId,
            Integer channelId,
            ServiceStatus serviceStatus,
            Map<String, Object> additionalParams
    ) {
        if (executionDate == null) {
            throw new IllegalArgumentException("Execution date is mandatory");
        }
        if (messageId == null) {
            throw new IllegalArgumentException("Message ID (external execution id) is required");
        }
        if (channelId == null) {
            throw new IllegalArgumentException("Channel ID is required");
        }

        this.executionDate = executionDate;
        this.messageId = messageId;
        this.channelId = channelId;
        this.serviceStatus = serviceStatus;
        this.additionalParams = additionalParams == null ? new HashMap<>() : additionalParams;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

    public Object getMessageId() {
        return messageId;
    }

    public void setMessageId(Object messageId) {
        this.messageId = messageId;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public ServiceStatus getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(ServiceStatus serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public Map<String, Object> getAdditionalParams() {
        return additionalParams;
    }

    public void setAdditionalParams(Map<String, Object> additionalParams) {
        this.additionalParams = additionalParams;
    }
}
