/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

// Messages displayed on the frontend

export const MODULE_NAME = 'Messages';
export const FIELD_REQUIRED = 'Required';
export const SAVE_BUTTON_LABEL = 'Save';
export const NEXT_BUTTON_LABEL = 'Next';
export const CANCEL_BUTTON_LABEL = 'Cancel';

// Generic messages
export const GENERIC_FAILURE = 'An error occurred.';
export const GENERIC_PROCESSING = 'Processing...';
export const GENERIC_SUCCESS = 'Success.';
export const GENERIC_INVALID_FORM = 'Form is invalid. Check fields and send it again.';
export const GENERIC_LOADING = 'Loading...';

// Breadcrumb
export const GENERAL_MODULE_BREADCRUMB = 'Messages';
export const PATIENT_TEMPLATE_BREADCRUMB = 'Patient Template';
export const MANAGE_BREADCRUMB = 'Manage';
export const NEW_PATIENT_TEMPLATE_BREADCRUMB = 'New';
export const EDIT_PATIENT_TEMPLATE_BREADCRUMB = 'Edit';
export const SYSTEM_ADMINITRATION_BREADCRUMB = 'System Administration';

// Best contact time
export const NO_END_DATE_LABEL = 'No end date';
export const DATE_PICKER_END_DATE_LABEL = 'On';
export const AFTER_DAYS_BEFORE = 'After';
export const AFTER_DAYS_AFTER = 'times';
export const BEST_CONTACT_TIME_LABEL = 'Best Contact Time';
export const CALENDAR_OVERVIEW_LABEL = 'Calendar Overview';

// Admin settings 
export const DEFAULT_SETTINGS = 'Default settings';
export const DEFAULT_SETTINGS_POSTFIX = ' - default settings';
export const DEFAULT_SETTINGS_TABLE_TITLE = 'Default message details';
export const OTHER = 'Other';

export const PATIENT_TEMPLATE_START_DATE = 'Start of daily messages';
export const PATIENT_TEMPLATE_END_DATE = 'End of daily messages';
export const PATIENT_ROLE = 'Patient';
export const ISO_DATE_FORMAT = 'YYYY-MM-DD';
export const DATE_FORMAT = 'dd MMM yyyy';

// Patient template values
export const SERVICE_TYPE_VALUES = ['Call', 'SMS', 'Deactivate service'];
export const DAY_OF_WEEK_VALUES = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
export const MESSAGING_FREQUENCY_DAILY_OR_WEEKLY_OR_MONTHLY_VALUES = ['Daily', 'Weekly', 'Monthly'];
export const MESSAGING_FREQUENCY_WEEKLY_OR_MONTHLY_VALUES = ['Weekly', 'Monthly'];
export const CATEGORIES_MAP = {
  'HT_CONTACT_TRACING': 'Contact Tracing',
  'HT_CAREGIVER': 'Caregiver',
  'HT_SIDE_EFFECTS': 'Side Effects',
  'HT_TREATMENT': 'Treatment',
  'HT_PREVENTION': 'Prevention',
  'HT_SPREAD': 'Spread'
};

// Person status
export const PERSON_STATUS_NO_CONSENT = 'No consent';
export const PERSON_STATUS_ACTIVATED = 'Activated';
export const PERSON_STATUS_DEACTIVATED = 'Deactivated';
export const PERSON_STATUS_MISSING_VALUE = 'Missing value';
export const PERSON_STATUS_LABEL = 'Person status:';
export const PERSON_STATUS_MODAL_LABEL = 'Update the person status';
export const PERSON_STATUS_MODAL_INSTRUCTION = 'Change person status';
export const PERSON_STATUS_MODAL_FIELD_LABEL = 'Person status:';
export const PERSON_STATUS_MODAL_REASON_FIELD_LABEL = 'Reason:';
export const DEACTIVATED_KEY = 'DEACTIVATED';
export const ACTIVATED_KEY = 'ACTIVATED';
export const MISSING_VALUE_KEY = 'MISSING_VALUE';
export const NO_CONSENT_KEY = 'NO_CONSENT';
export const PERSON_STATUSES = {
  NO_CONSENT: {
    value: NO_CONSENT_KEY,
    label: PERSON_STATUS_NO_CONSENT
  },
  ACTIVATED: {
    value: ACTIVATED_KEY,
    label: PERSON_STATUS_ACTIVATED
  },
  DEACTIVATED: {
    value: DEACTIVATED_KEY,
    label: PERSON_STATUS_DEACTIVATED
  },
  MISSING_VALUE: {
    value: MISSING_VALUE_KEY,
    label: PERSON_STATUS_MISSING_VALUE
  }
};

export const CANCEL = 'Cancel';
export const CONFIRM = 'Confirm';

// Edit messages
export const EDIT_MESSAGES_TITLE = 'Create messages';
