/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package validate.validator;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.exception.ValidationException;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.api.service.TemplateFieldService;
import org.openmrs.module.messages.api.service.TemplateService;
import validate.annotation.ValidPatientTemplate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.openmrs.module.messages.ValidationMessagesConstants.PATIENT_TEMPLATE_REQUIRED_FIELD_IS_EMPTY;
import static org.openmrs.module.messages.ValidationMessagesConstants.TEMPLATE_WITH_ID_NOT_FOUND;
import static org.openmrs.module.messages.api.model.TemplateFieldType.SERVICE_TYPE;

public class PatientTemplateValidator implements ConstraintValidator<ValidPatientTemplate, PatientTemplate> {

    private static final String TEMPLATE_FIELD_VALUES_PATH = "patientTemplate.templateFieldValues";
    private static final String TEMPLATE_ID_PATH = "patientTemplate.templateId";

    @Override
    public boolean isValid(PatientTemplate patientTemplate, ConstraintValidatorContext ctx) {
        ctx.disableDefaultConstraintViolation();

        return validateTemplateFields(patientTemplate, ctx);
    }

    private boolean validateTemplateFields(PatientTemplate patientTemplate, ConstraintValidatorContext ctx) {
        List<TemplateFieldValue> emptyButRequired = new ArrayList<>();
        for (TemplateFieldValue templateFieldValue : patientTemplate.getTemplateFieldValues()) {
            validateFieldNotEmpty(emptyButRequired, templateFieldValue);
            Template template = getTemplateService().getById(patientTemplate.getTemplate().getId());
            if (template == null) {
                addErrorToContext(ctx, TEMPLATE_ID_PATH,
                        String.format(TEMPLATE_WITH_ID_NOT_FOUND, patientTemplate.getTemplate().getId()));
                return false;
            }
            validateTemplateFieldValue(templateFieldValue, template.getTemplateFields());
        }

        for (TemplateFieldValue violation : emptyButRequired) {
            addErrorToContext(ctx, TEMPLATE_FIELD_VALUES_PATH,
                    String.format(PATIENT_TEMPLATE_REQUIRED_FIELD_IS_EMPTY, violation.getTemplateField().getId(),
                            getTemplateFieldService().getById(violation.getTemplateField().getId()).getName()));
        }

        return emptyButRequired.size() == 0;
    }

    private void validateFieldNotEmpty(List<TemplateFieldValue> emptyButRequired, TemplateFieldValue templateFieldValue) {
        if (getTemplateFieldService().getById(templateFieldValue.getTemplateField().getId()).getMandatory()
                && StringUtils.isBlank(templateFieldValue.getValue())) {
            emptyButRequired.add(templateFieldValue);
        }
    }

    private void validateTemplateFieldValue(TemplateFieldValue tfv,
                                            Collection<TemplateField> templateFields) {
        for (TemplateField tf : templateFields) {
            if (tf.getId().equals(tfv.getTemplateField().getId()) && SERVICE_TYPE.equals(tf.getTemplateFieldType())) {
                validateServiceType(tfv);
            }
        }
    }

    private void validateServiceType(TemplateFieldValue tfv) {
        if (StringUtils.isBlank(tfv.getValue())) {
            throw new ValidationException(String.format("Invalid blank service type"));
        }
    }

    @Override
    public void initialize(ValidPatientTemplate validPatientTemplate) {
        // any specific action isn't required
    }

    private void addErrorToContext(ConstraintValidatorContext context, String path,
                                   String message) {
        context.buildConstraintViolationWithTemplate(message)
                .addNode(path)
                .addConstraintViolation();
    }

    private TemplateFieldService getTemplateFieldService() {
        return Context.getRegisteredComponent("messages.templateFieldService", TemplateFieldService.class);
    }

    private TemplateService getTemplateService() {
        return Context.getRegisteredComponent("messages.templateService", TemplateService.class);
    }
}
