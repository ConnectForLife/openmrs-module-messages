package org.openmrs.module.messages.api.scheduler.job;

import org.openmrs.module.messages.api.util.DateUtil;

public enum JobRepeatInterval {
    DAILY(DateUtil.DAY_IN_SECONDS);

    JobRepeatInterval(long seconds) {
        this.seconds = seconds;
    }

    private long seconds;

    public long getSeconds() {
        return seconds;
    }
}
