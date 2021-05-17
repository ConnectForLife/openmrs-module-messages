/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.ServiceContext;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.messages.api.dao.NotificationTemplateDao;
import org.openmrs.module.messages.api.model.NotificationTemplate;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.service.NotificationTemplateService;
import org.openmrs.module.messages.api.util.DateUtil;
import org.openmrs.module.messages.api.util.MessagesUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Service used to build the notifications based on the {@link org.openmrs.module.messages.api.model.NotificationTemplate}.
 * Abstraction extended by specific template engine implementation.
 */
public abstract class NotificationTemplateServiceImpl extends BaseOpenmrsService implements NotificationTemplateService {

    private static final Log LOGGER = LogFactory.getLog(NotificationTemplateServiceImpl.class);

    private static final String PATIENT_PROP = "patient";

    private static final String PATIENT_TEMPLATE_PROP = "patientTemplate";

    private static final String ACTOR_PROP = "actor";

    private NotificationTemplateDao notificationTemplateDao;

    /**
     * Loads and eval the template and returns the outcome message as String.
     *
     * @param template     - the notification template
     * @param templateData - input template's data
     * @return - the outcome message
     */
    protected abstract String loadTemplate(NotificationTemplate template, Map<String, Object> templateData);

    @Override
    public String parseTemplate(PatientTemplate patientTemplate, Map<String, String> serviceParam) {
        LOGGER.trace("handling the buildMessageForService(...)");
        final String serviceTemplateName = getServiceTemplateName(patientTemplate);
        final NotificationTemplate template = notificationTemplateDao.getTemplate(serviceTemplateName);

        return internalParseTemplate(patientTemplate, template, serviceParam);
    }

    @Override
    public String buildMessageByGlobalProperty(Map<String, Object> param, String globalPropertyName) {
        final GlobalProperty globalProperty = Context.getAdministrationService().getGlobalPropertyObject(globalPropertyName);
        final NotificationTemplate template = notificationTemplateDao.convertToNotificationTemplate(globalProperty);

        final Map<String, String> safeServiceParameters = new HashMap<>();
        for (Map.Entry<String, Object> paramEntry : param.entrySet()) {
            safeServiceParameters.put(paramEntry.getKey(), Objects.toString(paramEntry.getValue()));
        }

        return internalParseTemplate(null, template, safeServiceParameters);
    }

    @Override
    public String parseTemplate(PatientTemplate patientTemplate, NotificationTemplate notificationTemplate,
                                Map<String, String> serviceParam) {
        return internalParseTemplate(patientTemplate, notificationTemplate, serviceParam);
    }

    private String internalParseTemplate(PatientTemplate patientTemplate, NotificationTemplate notificationTemplate,
                                         Map<String, String> serviceParameters) {
        final String injectedServices = notificationTemplateDao.getInjectedServicesMap();
        final Map<String, Object> templateData = buildTemplateInputData(patientTemplate, serviceParameters,
                injectedServices);
        return loadTemplate(notificationTemplate, templateData);
    }

    /**
     * Sets the notification template dao bean value
     *
     * @param notificationTemplateDao - notification Template Dao impl
     */
    public void setNotificationTemplateDao(NotificationTemplateDao notificationTemplateDao) {
        this.notificationTemplateDao = notificationTemplateDao;
    }

    /**
     * Creates service template name for given {@link PatientTemplate}
     *
     * @param patientTemplate - given patient template
     * @return - service template name if exists
     * @should return null when given null patient template
     * @should return null when missing {@link org.openmrs.module.messages.api.model.Template} for given patient template
     */
    private String getServiceTemplateName(PatientTemplate patientTemplate) {
        String serviceTemplateName = null;
        if (null != patientTemplate && null != patientTemplate.getTemplate()) {
            serviceTemplateName = patientTemplate.getTemplate().getName();
        }
        return serviceTemplateName;
    }

    /**
     * Builds the notification template's input properties which can be use inside the template.
     *
     * @param patientTemplate  - the message service definition
     * @param serviceParam     - additional service params
     * @param injectedServices - services which can be used inside the template
     * @return - input notification template's properties map
     */
    private Map<String, Object> buildTemplateInputData(PatientTemplate patientTemplate, Map<String, String> serviceParam,
                                                       String injectedServices) {
        if (LOGGER.isDebugEnabled()) {
            final String serviceParamAsString = Objects.toString(serviceParam);
            LOGGER.debug(String.format("Building notification template's input data with injected services = %s" +
                            "\n\t service parameters = %s \n\t patientTemplate = %s ", injectedServices,
                    serviceParamAsString,
                    patientTemplate));
        }

        final Map<String, Object> templateInputProperties =
                serviceParam == null ? new HashMap<String, Object>() : new HashMap<String, Object>(serviceParam);

        this.loadBaseClasses(templateInputProperties);

        if (patientTemplate != null) {
            this.addDataBasedOnPatientTemplate(patientTemplate, templateInputProperties);
        }

        final Map<String, String> servicesMap = MessagesUtils.parseStringToMap(injectedServices);
        this.loadServices(templateInputProperties, servicesMap);

        return templateInputProperties;
    }

    /**
     * Creates useful set of template's data based on {@link PatientTemplate} resource.
     *
     * @param patientTemplate         - related patient template
     * @param templateInputProperties - map of template properties
     */
    private void addDataBasedOnPatientTemplate(PatientTemplate patientTemplate,
                                               Map<String, Object> templateInputProperties) {
        if (null == patientTemplate) {
            return;
        }
        this.addToMapIfNotNull(templateInputProperties, PATIENT_PROP, patientTemplate.getPatient());
        this.addToMapIfNotNull(templateInputProperties, ACTOR_PROP, patientTemplate.getActor());
        templateInputProperties.put(PATIENT_TEMPLATE_PROP, patientTemplate);
    }

    /**
     * Adds adds value into map if value is not null
     *
     * @param map   - target map
     * @param key   - provided key
     * @param value - provided value
     */
    private void addToMapIfNotNull(Map<String, Object> map, String key, Object value) {
        if (null != value) {
            map.put(key, value);
        }
    }

    /**
     * Loads some classes have very useful static methods that are useful for templates
     *
     * @param templateInputProperties - the map of template properties
     */
    private void loadBaseClasses(Map<String, Object> templateInputProperties) {
        templateInputProperties.put("String", String.class);
        templateInputProperties.put("Integer", Integer.class);
        templateInputProperties.put("Long", Long.class);
        templateInputProperties.put("Float", Float.class);
        templateInputProperties.put("Double", Double.class);
        templateInputProperties.put("Date", Date.class);
        templateInputProperties.put("SimpleDateFormat", SimpleDateFormat.class);
        templateInputProperties.put("Calendar", Calendar.class);
        templateInputProperties.put("DateUtil", DateUtil.class);
        templateInputProperties.put("Math", Math.class);
    }

    /**
     * Loads the injected service into the notification template properties.
     *
     * @param templateInputProperties - the map of template properties
     * @param servicesMap             - map of services which should be loaded (serviceProp:beanId)
     */
    private void loadServices(Map<String, Object> templateInputProperties, Map<String, String> servicesMap) {
        StringBuilder notFoundServices = new StringBuilder();

        for (Map.Entry<String, String> entry : servicesMap.entrySet()) {
            String serviceProp = entry.getKey();
            String serviceKey = entry.getValue();

            Object service = this.tryLoadingService(serviceKey);
            if (null == service) {
                service = this.tryLoadingBean(serviceKey);
            }
            if (null == service) {
                notFoundServices.append(entry.getValue());
                notFoundServices.append("\n");
            } else {
                templateInputProperties.put(serviceProp, service);
            }
        }

        if (!notFoundServices.toString().isEmpty()) {
            LOGGER.warn(String.format("Didn't load some services %s", notFoundServices));
        }
    }

    /**
     * Try to load the service from service context.
     *
     * @param serviceKey - service key
     * @return - service if exists
     */
    private Object tryLoadingService(String serviceKey) {
        Object service = null;
        try {
            Class<?> serviceClass = Thread.currentThread().getContextClassLoader().loadClass(serviceKey);
            service = ServiceContext.getInstance().getService(serviceClass);
        } catch (Exception ex) {
            LOGGER.trace("Service not found", ex);
        }

        return service;
    }

    /**
     * Try to load the bean from the aplication context.
     *
     * @param beanName - bean name
     * @return - bean if exists
     */
    private Object tryLoadingBean(String beanName) {
        Object bean = null;
        try {
            bean = ServiceContext.getInstance().getApplicationContext().getBean(beanName);
        } catch (Exception ex) {
            LOGGER.trace("Bean not found", ex);
        }
        return bean;
    }
}
