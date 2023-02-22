/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.messages;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.APIException;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.DaemonToken;
import org.openmrs.module.DaemonTokenAware;
import org.openmrs.module.Module;
import org.openmrs.module.ModuleFactory;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.event.AbstractMessagesEventListener;
import org.openmrs.module.messages.api.event.listener.MessagesEventListenerFactory;
import org.openmrs.module.messages.api.event.listener.subscribable.PeopleActionListener;
import org.openmrs.module.messages.api.event.listener.subscribable.RelationshipActionListener;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;
import org.openmrs.module.messages.api.scheduler.job.JobRepeatInterval;
import org.openmrs.module.messages.api.scheduler.job.MessageDeliveriesJobDefinition;
import org.openmrs.module.messages.api.service.MessagesSchedulerService;
import org.openmrs.module.messages.api.util.GlobalPropertyUtil;
import org.openmrs.module.metadatadeploy.api.MetadataDeployService;
import org.openmrs.module.metadatadeploy.bundle.MetadataBundle;

import java.util.List;
import java.util.stream.Collectors;

import static org.openmrs.api.context.Context.getRegisteredComponent;
import static org.openmrs.module.messages.api.util.CountryPropertyUtils.createDefaultCountrySettingIfNotExists;

/**
 * This class contains the logic that is run every time this module is either started or shutdown
 */
public class MessagesActivator extends BaseModuleActivator implements DaemonTokenAware {

  private static final Log LOGGER = LogFactory.getLog(MessagesActivator.class);
  private static final String MESSAGES_MODULE_ROOT_PACKAGE = "org.openmrs.module.messages";

  /** @see #started() */
  @Override
  public void started() {
    LOGGER.info("Started Messages");
    try {
      createConfig();
      installMetadataBundles();
      scheduleMessageDeliveries();
      MessagesEventListenerFactory.registerEventListeners();
    } catch (APIException e) {
      safeShutdownModule();
      LOGGER.error("Failed to setup the required modules");
      throw new MessagesRuntimeException("Failed to setup the required modules", e);
    }
  }

  /** @see #shutdown() */
  public void shutdown() {
    LOGGER.info("Shutdown Messages");
    MessagesEventListenerFactory.unRegisterEventListeners();
  }

  /** @see #stopped() */
  @Override
  public void stopped() {
    LOGGER.info("Stopped Messages");
    MessagesEventListenerFactory.unRegisterEventListeners();
  }

  @Override
  public void setDaemonToken(DaemonToken token) {
    LOGGER.info("Set daemon token to Messages Module event listeners");
    getSchedulerService().setDaemonToken(token);

    List<PeopleActionListener> listeners =
        Context.getRegisteredComponents(PeopleActionListener.class);
    for (PeopleActionListener peopleActionListener : listeners) {
      peopleActionListener.setDaemonToken(token);
    }

    List<RelationshipActionListener> relationshipListeners =
        Context.getRegisteredComponents(RelationshipActionListener.class);
    for (RelationshipActionListener relationshipActionListener : relationshipListeners) {
      relationshipActionListener.setDaemonToken(token);
    }

    List<AbstractMessagesEventListener> eventComponents =
        Context.getRegisteredComponents(AbstractMessagesEventListener.class);
    for (AbstractMessagesEventListener eventListener : eventComponents) {
      eventListener.setDaemonToken(token);
    }
  }

  private void scheduleMessageDeliveries() {
    Long interval = getJobInterval();
    getSchedulerService().rescheduleOrCreateNewTask(new MessageDeliveriesJobDefinition(), interval);
  }

  private MessagesSchedulerService getSchedulerService() {
    return Context.getRegisteredComponent(
        MessagesConstants.SCHEDULER_SERVICE, MessagesSchedulerService.class);
  }

  private void createConfig() {
    createActorTypeConfig();
    createReschedulingStrategyConfig();
    createServiceResultHandlersConfig();
    createConsentConfig();
    createPersonStatusConfig();
    createStatusesEndingCallflowConfig();
    createNotificationTemplateConfig();
    createDefaultUserTimezone();
    createCallConfiguration();
    createSMSConfiguration();
    createWhatsAppConfiguration();
    createSchedulerConfiguration();
    createITRConfiguration();
  }

  private void createNotificationTemplateConfig() {
    createGlobalSettingIfNotExists(
        ConfigConstants.NOTIFICATION_TEMPLATE_INJECTED_SERVICES,
        ConfigConstants.NOTIFICATION_TEMPLATE_INJECTED_SERVICES_DEFAULT_VALUE,
        ConfigConstants.NOTIFICATION_TEMPLATE_INJECTED_SERVICES_DESCRIPTION);
  }

  private void createActorTypeConfig() {
    createGlobalSettingIfNotExists(
        ConfigConstants.ACTOR_TYPES_KEY,
        ConfigConstants.ACTOR_TYPES_DEFAULT_VALUE,
        ConfigConstants.ACTOR_TYPES_DESCRIPTION);
  }

  private void createConsentConfig() {
    createBestContactTimeAttributeType();

    createDefaultCountrySettingIfNotExists(
        ConfigConstants.BEST_CONTACT_TIME_KEY,
        ConfigConstants.BEST_CONTACT_TIME_DEFAULT_VALUE,
        ConfigConstants.BEST_CONTACT_TIME_DESCRIPTION);

    createGlobalSettingIfNotExists(
        ConfigConstants.CONSENT_CONTROL_KEY,
        ConfigConstants.CONSENT_CONTROL_DEFAULT_VALUE,
        ConfigConstants.CONSENT_CONTROL_DESCRIPTION);
  }

  private void createReschedulingStrategyConfig() {
    createGlobalSettingIfNotExists(
        ConfigConstants.RESCHEDULING_STRATEGY_KEY,
        ConfigConstants.RESCHEDULING_STRATEGY_DEFAULT_VALUE,
        ConfigConstants.RESCHEDULING_STRATEGY_DESCRIPTION);
    createGlobalSettingIfNotExists(
        ConfigConstants.MAX_NUMBER_OF_ATTEMPTS_KEY,
        ConfigConstants.MAX_NUMBER_OF_ATTEMPTS_DEFAULT_VALUE,
        ConfigConstants.MAX_NUMBER_OF_ATTEMPTS_DESCRIPTION);
    createGlobalSettingIfNotExists(
        ConfigConstants.TIME_INTERVAL_TO_NEXT_RESCHEDULE_KEY,
        ConfigConstants.TIME_INTERVAL_TO_NEXT_RESCHEDULE_DEFAULT_VALUE,
        ConfigConstants.TIME_INTERVAL_TO_NEXT_RESCHEDULE_DESCRIPTION);
  }

  private void createServiceResultHandlersConfig() {
    createGlobalSettingIfNotExists(
        ConfigConstants.SERVICE_RESULT_HANDLERS,
        ConfigConstants.SERVICE_RESULT_HANDLERS_DEFAULT_VALUE,
        ConfigConstants.SERVICE_RESULT_HANDLERS_DESCRIPTION);
  }

  private void createStatusesEndingCallflowConfig() {
    createGlobalSettingIfNotExists(
        ConfigConstants.STATUSES_ENDING_CALLFLOW,
        ConfigConstants.STATUSES_ENDING_CALLFLOW_DEFAULT_VALUE,
        ConfigConstants.STATUSES_ENDING_CALLFLOW_DESCRIPTION);
  }

  private void createBestContactTimeAttributeType() {
    PersonAttributeType attributeType = new PersonAttributeType();
    attributeType.setName(ConfigConstants.PERSON_CONTACT_TIME_ATTRIBUTE_TYPE_NAME);
    attributeType.setFormat(ConfigConstants.PERSON_CONTACT_TIME_TYPE_FORMAT);
    attributeType.setDescription(ConfigConstants.PERSON_CONTACT_TIME_TYPE_DESCRIPTION);
    attributeType.setUuid(ConfigConstants.PERSON_CONTACT_TIME_TYPE_UUID);
    createPersonAttributeTypeIfNotExists(attributeType);
  }

  private void createPersonStatusConfig() {
    createPersonStatusAttributeType();
    createPersonStatusReasonAttributeType();
    createGlobalSettingIfNotExists(
        ConfigConstants.PERSON_STATUS_POSSIBLE_REASONS_KEY,
        ConfigConstants.PERSON_STATUS_POSSIBLE_REASONS_DEFAULT_VALUE,
        ConfigConstants.PERSON_STATUS_POSSIBLE_REASONS_DESCRIPTION);
    createGlobalSettingIfNotExists(
        ConfigConstants.PERSON_STATUS_CONFIGURATION_KEY,
        ConfigConstants.PERSON_STATUS_CONFIGURATION_DEFAULT_VALUE,
        ConfigConstants.PERSON_STATUS_CONFIGURATION_DESCRIPTION);
  }

  private void createPersonStatusAttributeType() {
    PersonAttributeType attributeType = new PersonAttributeType();
    attributeType.setName(ConfigConstants.PERSON_STATUS_ATTRIBUTE_TYPE_NAME);
    attributeType.setFormat(ConfigConstants.PERSON_STATUS_ATTRIBUTE_TYPE_FORMAT);
    attributeType.setDescription(ConfigConstants.PERSON_STATUS_ATTRIBUTE_TYPE_DESCRIPTION);
    attributeType.setUuid(ConfigConstants.PERSON_STATUS_ATTRIBUTE_TYPE_UUID);
    createPersonAttributeTypeIfNotExists(attributeType);
  }

  private void createPersonStatusReasonAttributeType() {
    PersonAttributeType attributeType = new PersonAttributeType();
    attributeType.setName(ConfigConstants.PERSON_STATUS_REASON_ATTRIBUTE_TYPE_NAME);
    attributeType.setFormat(ConfigConstants.PERSON_STATUS_REASON_ATTRIBUTE_TYPE_FORMAT);
    attributeType.setDescription(ConfigConstants.PERSON_STATUS_REASON_ATTRIBUTE_TYPE_DESCRIPTION);
    attributeType.setUuid(ConfigConstants.PERSON_STATUS_REASON_ATTRIBUTE_TYPE_UUID);
    createPersonAttributeTypeIfNotExists(attributeType);
  }

  private void createDefaultUserTimezone() {
    createGlobalSettingIfNotExists(
        ConfigConstants.DEFAULT_USER_TIMEZONE,
        ConfigConstants.DEFAULT_USER_TIMEZONE_DEFAULT_VALUE,
        ConfigConstants.DEFAULT_USER_TIMEZONE_DESCRIPTION);
  }

  private void createCallConfiguration() {
    createGlobalSettingIfNotExists(
        ConfigConstants.CALL_CONFIG,
        ConfigConstants.CALL_CONFIG_DEFAULT_VALUE,
        ConfigConstants.CALL_CONFIG_DESCRIPTION);
    createGlobalSettingIfNotExists(
        ConfigConstants.CALL_DEFAULT_FLOW,
        ConfigConstants.CALL_DEFAULT_FLOW_DEFAULT_VALUE,
        ConfigConstants.CALL_DEFAULT_FLOW_DESCRIPTION);
  }

  private void createSMSConfiguration() {
    createGlobalSettingIfNotExists(
        ConfigConstants.SMS_CONFIG,
        ConfigConstants.SMS_CONFIG_DEFAULT_VALUE,
        ConfigConstants.SMS_CONFIG_DESCRIPTION);
  }

  private void createWhatsAppConfiguration() {
    createGlobalSettingIfNotExists(
        ConfigConstants.WHATSAPP_CONFIG_GP_KEY,
        ConfigConstants.WHATSAPP_CONFIG_GP_DEFAULT_VALUE,
        ConfigConstants.WHATSAPP_CONFIG_GP_DESCRIPTION);
  }

  private void createSchedulerConfiguration() {
    createGlobalSettingIfNotExists(
        ConfigConstants.MESSAGE_DELIVERY_JOB_INTERVAL,
        ConfigConstants.MESSAGE_DELIVERY_JOB_INTERVAL_DEFAULT_VALUE,
        ConfigConstants.MESSAGE_DELIVERY_JOB_INTERVAL_DESCRIPTION);

    createGlobalSettingIfNotExists(
        ConfigConstants.MESSAGE_DELIVERY_JOB_START_TIME_GP_KEY,
        ConfigConstants.MESSAGE_DELIVERY_JOB_START_TIME_DEFAULT_VALUE,
        ConfigConstants.MESSAGE_DELIVERY_JOB_START_TIME_DESCRIPTION);
  }

  private void createITRConfiguration() {
    createGlobalSettingIfNotExists(
        ConfigConstants.ITR_ANSWER_REGEX_CONCEPT_ATTR_TYPE_UUID,
        null,
        ConfigConstants.ITR_ANSWER_REGEX_CONCEPT_ATTR_TYPE_UUID_DESC);
    createGlobalSettingIfNotExists(
        ConfigConstants.ITR_PROVIDER_TEMPLATE_NAME_CONCEPT_ATTR_TYPE_UUID,
        null,
        ConfigConstants.ITR_PROVIDER_TEMPLATE_NAME_CONCEPT_ATTR_TYPE_UUID_DESC);
    createGlobalSettingIfNotExists(
        ConfigConstants.ITR_IMAGE_URL_CONCEPT_ATTR_TYPE_UUID,
        null,
        ConfigConstants.ITR_IMAGE_URL_CONCEPT_ATTR_TYPE_UUID_DESC);
    createGlobalSettingIfNotExists(
        ConfigConstants.ITR_MESSAGE_TEXT_ATTR_TYPE_UUID,
        null,
        ConfigConstants.ITR_MESSAGE_TEXT_ATTR_TYPE_UUID_DESC);
    createGlobalSettingIfNotExists(
        ConfigConstants.ITR_DEFAULT_ITR_MESSAGE_CONCEPT_UUID,
        null,
        ConfigConstants.ITR_DEFAULT_ITR_MESSAGE_CONCEPT_UUID_DESC);
  }

  private void installMetadataBundles() {
    final MetadataDeployService metadataDeployService =
        getRegisteredComponent("metadataDeployService", MetadataDeployService.class);
    final List<MetadataBundle> bundles =
        Context.getRegisteredComponents(MetadataBundle.class).stream()
            .filter(
                component ->
                    component.getClass().getName().startsWith(MESSAGES_MODULE_ROOT_PACKAGE))
            .collect(Collectors.toList());

    metadataDeployService.installBundles(bundles);
  }

  private Long getJobInterval() {
    String gpName = ConfigConstants.MESSAGE_DELIVERY_JOB_INTERVAL;
    String stringValue = Context.getAdministrationService().getGlobalProperty(gpName);
    Long interval;
    try {
      interval = (long) GlobalPropertyUtil.parseInt(gpName, stringValue);
    } catch (MessagesRuntimeException e) {
      interval = JobRepeatInterval.DAILY.getSeconds();
      LOGGER.warn(
          String.format(
              "Error occurred during getting job interval: %s. " + "Default value will be used: %d",
              e.getMessage(), interval));
    }
    return interval;
  }

  private void createPersonAttributeTypeIfNotExists(PersonAttributeType attributeType) {
    PersonService personService = Context.getPersonService();
    PersonAttributeType actual =
        personService.getPersonAttributeTypeByUuid(attributeType.getUuid());
    if (actual == null) {
      personService.savePersonAttributeType(attributeType);
    }
  }

  private void createGlobalSettingIfNotExists(String key, String value, String description) {
    String existSetting = Context.getAdministrationService().getGlobalProperty(key);
    if (StringUtils.isBlank(existSetting)) {
      GlobalProperty gp = new GlobalProperty(key, value, description);
      Context.getAdministrationService().saveGlobalProperty(gp);
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug(
            String.format(
                "Message Module created '%s' global property with value - %s", key, value));
      }
    }
  }

  private void safeShutdownModule() {
    Module mod = ModuleFactory.getModuleById(ConfigConstants.MODULE_ID);
    ModuleFactory.stopModule(mod);
  }
}
