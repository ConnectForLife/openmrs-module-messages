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
