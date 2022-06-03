/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.util.validate;

import org.openmrs.module.messages.api.exception.ValidationException;
import org.openmrs.module.messages.api.model.ErrorMessage;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

public class ValidationComponent {

    private LocalValidatorFactoryBean factory;

    /**
     * Generic method which validates objects list according its annotations.
     *
     * @param objectsToValidate objects list to validate
     * @param clazz            class of the object. If not passed automatically inferred from objectToValidate.

     * @throws ValidationException if validation error found
     */
    public <T> void validateList(List<T> objectsToValidate, Class<?>... clazz) {
        for (T objectToValidate : objectsToValidate) {
            this.validate(objectToValidate, clazz);
        }
    }

    /**
     * Generic method which validates objects according its annotations.
     *
     * @param objectToValidate object to validate
     * @param clazz            class of the object. If not passed automatically inferred from objectToValidate.

     * @throws ValidationException if validation error found
     */
    public <T> void validate(T objectToValidate, Class<?>... clazz) {
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(objectToValidate, clazz);
        if (!violations.isEmpty()) {
            throw new ValidationException(buildValidationErrorCause(violations));
        }
    }

    public ValidationComponent setFactory(LocalValidatorFactoryBean factory) {
        this.factory = factory;
        return this;
    }

    private <T> List<ErrorMessage> buildValidationErrorCause(Set<ConstraintViolation<T>> violations) {
        List<ErrorMessage> errorMessages = new LinkedList<>();
        for (ConstraintViolation<T> violation : violations) {
            errorMessages.add(new ErrorMessage(violation.getPropertyPath().toString(), violation.getMessage()));
        }
        return errorMessages;
    }
}
