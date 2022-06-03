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

import org.apache.commons.lang3.StringUtils;
import org.openmrs.module.messages.api.dto.TemplateFieldDTO;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.model.TemplateFieldDefaultValue;
import org.openmrs.module.messages.api.model.TemplateFieldType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Convert between {@link org.openmrs.module.messages.api.model.TemplateField}
 * and {@link org.openmrs.module.messages.api.dto.TemplateFieldDTO} resources in both ways.
 */
public class TemplateFieldMapper extends AbstractOpenMrsDataMapper<TemplateFieldDTO, TemplateField> {

    public static final String POSSIBLE_VALUES_SEPARATOR = "|";
    private TemplateFieldDefaultValueMapper templateFieldDefaultValueMapper;

    @Override
    public TemplateFieldDTO toDto(TemplateField dao) {
        String fieldType = dao.getTemplateFieldType() == null ? null :
                dao.getTemplateFieldType().name();
        return new TemplateFieldDTO()
                .setId(dao.getId())
                .setName(dao.getName())
                .setMandatory(dao.getMandatory())
                .setDefaultValue(dao.getDefaultValue())
                .setType(fieldType)
                .setUuid(dao.getUuid())
                .setDefaultValues(templateFieldDefaultValueMapper.toDtos(dao.getDefaultValues()))
                .setPossibleValues(extractPossibleValues(dao.getPossibleValues()));
    }

    @Override
    public TemplateField fromDto(TemplateFieldDTO dto) {
        TemplateField templateField = new TemplateField();
        templateField.setId(dto.getId());
        templateField.setName(dto.getName());
        templateField.setMandatory(dto.getMandatory());
        templateField.setDefaultValue(dto.getDefaultValue());
        TemplateFieldType type = TemplateFieldType.valueOf(dto.getType());
        templateField.setTemplateFieldType(type);
        if (dto.getUuid() != null) {
            templateField.setUuid(dto.getUuid());
        }
        templateField.setDefaultValues(new HashSet<>(templateFieldDefaultValueMapper.fromDtos(dto.getDefaultValues())));
        for (TemplateFieldDefaultValue defaultValue : templateField.getDefaultValues()) {
            defaultValue.setTemplateField(templateField);
        }
        templateField.setPossibleValues(mergePossibleValues(dto.getPossibleValues()));
        return templateField;
    }

    @Override
    public void updateFromDto(TemplateFieldDTO source, TemplateField target) {
        target.setName(source.getName());
        target.setMandatory(source.getMandatory());
        target.setDefaultValue(source.getDefaultValue());
        target.setTemplateFieldType(TemplateFieldType.valueOf(source.getType()));
        templateFieldDefaultValueMapper.updateFromDtos(source.getDefaultValues(), target.getDefaultValues());
        for (TemplateFieldDefaultValue defaultValue : target.getDefaultValues()) {
            defaultValue.setTemplateField(target);
        }
        target.setPossibleValues(mergePossibleValues(source.getPossibleValues()));
    }

    public TemplateFieldMapper setTemplateFieldDefaultValueMapper(
            TemplateFieldDefaultValueMapper templateFieldDefaultValueMapper) {
        this.templateFieldDefaultValueMapper = templateFieldDefaultValueMapper;
        return this;
    }

    private List<String> extractPossibleValues(String possibleValues) {
        return StringUtils.isBlank(possibleValues) ? new ArrayList<>()
                : Arrays.asList(StringUtils.split(possibleValues, POSSIBLE_VALUES_SEPARATOR));
    }

    private String mergePossibleValues(List<String> possibleValues) {
        return StringUtils.join(possibleValues, POSSIBLE_VALUES_SEPARATOR);
    }
}
