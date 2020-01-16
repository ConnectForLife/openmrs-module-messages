/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.web.controller;

import static org.openmrs.module.messages.api.model.ErrorMessageEnum.ERR_BAD_PARAM;
import static org.openmrs.module.messages.api.model.ErrorMessageEnum.ERR_ENTITY_NOT_FOUND;
import static org.openmrs.module.messages.api.model.ErrorMessageEnum.ERR_SYSTEM;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.messages.api.dto.ErrorResponseDTO;
import org.openmrs.module.messages.api.exception.EntityNotFoundException;
import org.openmrs.module.messages.api.exception.ValidationException;
import org.openmrs.module.messages.api.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Base Rest Controller
 * All controllers in this module extend this for easy error handling
 */
public abstract class BaseRestController {

    private final Log logger = LogFactory.getLog(getClass());

    /**
     * Exception handler for bad request - Http status code of 400
     *
     * @param e the exception throw
     * @return a error response
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponseDTO handleIllegalArgumentException(IllegalArgumentException e) {
        logger.error(e.getMessage(), e);
        return new ErrorResponseDTO(new ErrorMessage(ERR_BAD_PARAM.getCode(), e.getMessage()));
    }

    /**
     * Exception handler for anything not covered above - Http status code of 500
     *
     * @param e the exception throw
     * @return a error response
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponseDTO handleException(Exception e) {
        logger.error(e.getMessage(), e);
        // TODO: 500 error we should return only single error (without wrapping) - UI and backend refactor required
        return new ErrorResponseDTO(new ErrorMessage(ERR_SYSTEM.getCode(), e.getMessage()));
    }

    /**
     * Exception handler for bad request - Http status code of 400
     *
     * @param ex the exception throw
     * @return a error response
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponseDTO handleValidationException(ValidationException ex) {
        logger.error(ex.getMessage(), ex);
        return ex.getErrorResponse();
    }

    /**
     * Exception handler for not found - Http status code of 404
     *
     * @param e the exception throw
     * @return a error response
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponseDTO handleEntityNotFoundException(EntityNotFoundException e) {
        logger.error(e.getMessage(), e);
        // TODO: 404 error we should return only single error (without wrapping) - UI and backend refactor required
        return new ErrorResponseDTO(new ErrorMessage(ERR_ENTITY_NOT_FOUND.getCode(), e.getMessage()));
    }

    protected Log getLogger() {
        return logger;
    }
}
