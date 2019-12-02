package org.openmrs.module.messages.api.execution;

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
    public static final int EXPECTED_COL_NUM = 3;

    private Date executionDate;
    private Object messageId;
    private Integer channelId;

    public static ServiceResult parse(Object[] row) {
        if (row.length != EXPECTED_COL_NUM) {
            throw new IllegalStateException("Invalid number of columns in result row: " + row.length);
        }

        Date date = (Date) row[EXEC_DATE_INDEX];
        Object msgId = row[MSG_ID_INDEX];
        Integer channel = (Integer) row[CHANNEL_ID_INDEX];

        return new ServiceResult(date, msgId, channel);
    }

    public static List<ServiceResult> parseList(List<Object[]> list) {
        List<ServiceResult> resultList = new ArrayList<>();
        for (Object[] row : list) {
            ServiceResult result = ServiceResult.parse(row);
            resultList.add(result);
        }
        return resultList;
    }

    public ServiceResult(Date executionDate, Object messageId, Integer channelId) {
        this.executionDate = executionDate;
        this.messageId = messageId;
        this.channelId = channelId;
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
}
