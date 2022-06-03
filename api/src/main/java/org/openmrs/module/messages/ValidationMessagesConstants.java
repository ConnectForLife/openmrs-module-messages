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

public final class ValidationMessagesConstants {
  public static final String PATIENT_TEMPLATE_INVALID = "The patient template is invalid";
  public static final String PATIENT_TEMPLATE_REQUIRED_FIELD_IS_EMPTY =
      "Patient template field with id: %sand name: %s is empty";

  public static final String TEMPLATE_WITH_ID_NOT_FOUND = "Template with id %d not found";

  public static final String COUNTRY_PROPERTY_INVALID = "The Country Property is invalid.";
  public static final String COUNTRY_PROPERTY_NAME_MUST_NOT_BE_EMPTY_MSG =
      "The Country Property must not be empty!";
  public static final String COUNTRY_PROPERTY_VALUE_MUST_NOT_BE_EMPTY_MSG =
      "The Country Property must not be empty!";
  public static final String COUNTRY_PROPERTY_MUST_BE_UNIQUE_MSG =
      "The Country Property name and country must be unique together!";

  private ValidationMessagesConstants() {}
}
