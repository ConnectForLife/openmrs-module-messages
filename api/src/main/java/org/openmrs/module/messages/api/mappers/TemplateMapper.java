/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.mappers;

import org.openmrs.module.messages.api.dto.TemplateDTO;
import org.openmrs.module.messages.api.dto.TemplateFieldDTO;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateField;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Convert between {@link org.openmrs.module.messages.api.model.Template}
 * and {@link org.openmrs.module.messages.api.dto.TemplateDTO} resources in both ways.
 */
public class TemplateMapper extends AbstractMapper<TemplateDTO, Template> implements UpdateMapper<TemplateDTO, Template> {

    private TemplateFieldMapper templateFieldMapper;

    @Override
    public TemplateDTO toDto(Template dao) {
        List<TemplateFieldDTO> templateFields = templateFieldMapper.toDtos(dao.getTemplateFields());
        return new TemplateDTO()
                .setId(dao.getId())
                .setServiceQuery(dao.getServiceQuery())
                .setCalendarServiceQuery(dao.getCalendarServiceQuery())
                .setServiceQueryType(dao.getServiceQueryType())
                .setTemplateFields(templateFields)
                .setName(dao.getName())
                .setCreatedAt(dao.getDateCreated())
                .setUuid(dao.getUuid());
    }

    @Override
    public Template fromDto(TemplateDTO dto) {
        Template template = new Template();
        template.setName(dto.getName());
        template.setId(dto.getId());
        template.setServiceQuery(dto.getServiceQuery());
        template.setCalendarServiceQuery(dto.getCalendarServiceQuery());
        template.setServiceQueryType(dto.getServiceQueryType());
        mapTemplateFieldsFromDto(dto, template);
        if (dto.getUuid() != null) {
            template.setUuid(dto.getUuid());
        }
        return template;
    }

    @Override
    public void updateFromDto(TemplateDTO newTemplate, Template existingTemplate) {
        existingTemplate.setName(newTemplate.getName());
        templateFieldMapper.updateFromDtos(newTemplate.getTemplateFields(), existingTemplate.getTemplateFields());
    }

    public TemplateMapper setTemplateFieldMapper(TemplateFieldMapper templateFieldMapper) {
        this.templateFieldMapper = templateFieldMapper;
        return this;
    }

    @Override
    protected void doSafeDelete(Template target) {
        target.setRetired(Boolean.TRUE);
    }

    private void mapTemplateFieldsFromDto(TemplateDTO dto, Template template) {
        final Set<TemplateField> templateFields = new HashSet<>(templateFieldMapper.fromDtos(dto.getTemplateFields()));
        for (TemplateField templateField : templateFields) {
            templateField.setTemplate(template);
        }
        template.setTemplateFields(templateFields);
    }
}
