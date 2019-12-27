package org.openmrs.module.messages.api.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.openmrs.Person;
import org.openmrs.module.messages.api.util.OpenmrsObjectUtil;

public class ScheduledServicesExecutionContext implements Serializable {

    private static final long serialVersionUID = 7043667008864304408L;

    private static final int INITIAL_NON_ZERO_ODD_NUMBER = 17;
    private static final int MULTIPLIER_NON_ZERO_ODD_NUMBER = 37;

    private List<Integer> serviceIdsToExecute;

    private Date executionDate;

    private int actorId;

    public ScheduledServicesExecutionContext(List<ScheduledService> scheduledServices,
                                             Date executionDate, Person actor) {
        this.serviceIdsToExecute = OpenmrsObjectUtil.getIds(scheduledServices);
        this.executionDate = executionDate;
        this.actorId = actor.getId();
    }

    public List<Integer> getServiceIdsToExecute() {
        return serviceIdsToExecute;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public int getActorId() {
        return actorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ScheduledServicesExecutionContext that = (ScheduledServicesExecutionContext) o;

        return new EqualsBuilder()
                .append(actorId, that.actorId)
                .append(serviceIdsToExecute, that.serviceIdsToExecute)
                .append(executionDate, that.executionDate)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(INITIAL_NON_ZERO_ODD_NUMBER, MULTIPLIER_NON_ZERO_ODD_NUMBER)
                .append(serviceIdsToExecute)
                .append(executionDate)
                .append(actorId)
                .toHashCode();
    }
}
