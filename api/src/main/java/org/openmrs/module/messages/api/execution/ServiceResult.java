package org.openmrs.module.messages.api.execution;

import org.openmrs.module.messages.api.model.types.ServiceStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a single execution for a service/message.
 */
public class ServiceResult implements Serializable {

    private static final long serialVersionUID = 2598236499107927781L;

    public static final int EXEC_DATE_INDEX = 0;
    public static final int MSG_ID_INDEX = 1;
    public static final int CHANNEL_ID_INDEX = 2;
    public static final int STATUS_COL_INDEX = 3;
    public static final int MIN_COL_NUM = 3;
    public static final int MAX_COL_NUM = 4;

    private Date executionDate;
    private Object messageId;
    private Integer channelId;
    private ServiceStatus serviceStatus = ServiceStatus.FUTURE;

    public static ServiceResult parse(Object[] row) {
        if (row.length < MIN_COL_NUM || row.length > MAX_COL_NUM) {
            throw new IllegalStateException("Invalid number of columns in result row: " + row.length);
        }

        Date date = (Date) row[EXEC_DATE_INDEX];
        Object msgId = row[MSG_ID_INDEX];
        Integer channel = (Integer) row[CHANNEL_ID_INDEX];

        ServiceStatus status = ServiceStatus.FUTURE;
        if (row.length >= STATUS_COL_INDEX + 1 && row[STATUS_COL_INDEX] instanceof String) {
            status = ServiceStatus.valueOf((String) row[STATUS_COL_INDEX]);
        }

        return new ServiceResult(date, msgId, channel, status);
    }

    public static List<ServiceResult> parseList(List<Object[]> list) {
        List<ServiceResult> resultList = new ArrayList<>();
        for (Object[] row : list) {
            ServiceResult result = ServiceResult.parse(row);
            resultList.add(result);
        }
        return resultList;
    }

    public ServiceResult() {
    }

    public ServiceResult(Date executionDate, Object messageId, Integer channelId, ServiceStatus serviceStatus) {
        this.executionDate = executionDate;
        this.messageId = messageId;
        this.channelId = channelId;
        this.serviceStatus = serviceStatus;
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
}
