/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.dto;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represent the DTO for the {@link org.openmrs.module.messages.api.model.Template}
 */
public class TemplateDTO extends BaseDTO implements DTO {

    private static final long serialVersionUID = 7144042249345408845L;

    private Integer id;

    @NotBlank
    private String name;

    @NotBlank
    private String serviceQuery;

    @NotBlank
    private String serviceQueryType;

    private String calendarServiceQuery;

    private boolean shouldUseOptimizedQuery;

    @Valid
    private List<TemplateFieldDTO> templateFields = new ArrayList<>();

    private Date createdAt;

    private String uuid;

    public Integer getId() {
        return id;
    }

    public TemplateDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getServiceQuery() {
        return serviceQuery;
    }

    public TemplateDTO setServiceQuery(String serviceQuery) {
        this.serviceQuery = serviceQuery;
        return this;
    }

    public String getServiceQueryType() {
        return serviceQueryType;
    }

    public TemplateDTO setServiceQueryType(String serviceQueryType) {
        this.serviceQueryType = serviceQueryType;
        return this;
    }

    public List<TemplateFieldDTO> getTemplateFields() {
        return templateFields;
    }

    public TemplateDTO setTemplateFields(List<TemplateFieldDTO> templateFields) {
        this.templateFields = templateFields;
        return this;
    }

    public String getName() {
        return name;
    }

    public TemplateDTO setName(String name) {
        this.name = name;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public TemplateDTO setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public TemplateDTO setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getCalendarServiceQuery() {
        return calendarServiceQuery;
    }

    public TemplateDTO setCalendarServiceQuery(String calendarServiceQuery) {
        this.calendarServiceQuery = calendarServiceQuery;
        return this;
    }

    public boolean isShouldUseOptimizedQuery() {
        return shouldUseOptimizedQuery;
    }

    public TemplateDTO setShouldUseOptimizedQuery(boolean shouldUseOptimizedQuery) {
        this.shouldUseOptimizedQuery = shouldUseOptimizedQuery;
        return this;
    }
}
