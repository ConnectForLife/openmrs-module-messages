package org.openmrs.module.messages.api.service.impl;

import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.model.ScheduledServiceParameter;
import org.openmrs.module.messages.api.service.MessagingGroupService;
import org.openmrs.module.messages.api.service.ScheduledServiceGroupService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;

/**
 * The default implementation of {@link ScheduledServiceGroupService}.
 * <p>
 * The bean is configured in resources/moduleApplicationContext.xml.
 * </p>
 */
public class ScheduledServiceGroupServiceImpl implements ScheduledServiceGroupService {

    private MessagingGroupService messagingGroupService;

    @Override
    @Transactional
    public ScheduledServiceGroup createSingletonGroup(final Date deliveryTime, final String channelType,
                                                      final PatientTemplate patientTemplate) {
        final ScheduledService scheduledService = new ScheduledService();
        scheduledService.setStatus(INITIAL_SCHEDULED_SERVICE_STATUS);
        scheduledService.setPatientTemplate(patientTemplate);
        scheduledService.setScheduledServiceParameters(Collections.<ScheduledServiceParameter>emptyList());
        scheduledService.setService(patientTemplate.getTemplate().getName());

        final ScheduledServiceGroup scheduledServiceGroup = new ScheduledServiceGroup();
        scheduledServiceGroup.setPatient(patientTemplate.getPatient());
        scheduledServiceGroup.setActor(patientTemplate.getPatient());
        scheduledServiceGroup.setStatus(INITIAL_SCHEDULED_SERVICE_STATUS);

        scheduledServiceGroup.setChannelType(channelType);
        scheduledServiceGroup.setMsgSendTime(deliveryTime);

        scheduledServiceGroup.getScheduledServices().add(scheduledService);
        scheduledService.setGroup(scheduledServiceGroup);

        return messagingGroupService.saveGroup(scheduledServiceGroup);
    }

    public void setMessagingGroupService(MessagingGroupService messagingGroupService) {
        this.messagingGroupService = messagingGroupService;
    }
}
