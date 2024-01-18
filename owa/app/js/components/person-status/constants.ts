/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
 
export const CONFIRM = 'Confirm';
export const CANCEL = 'Cancel';
export const FIELD_REQUIRED = 'Required';
export const PERSON_STATUS_NO_CONSENT = 'No consent';
export const PERSON_STATUS_ACTIVATED = 'Activated';
export const ACTIVATED_TRANSLATION_KEY = 'person.status.activated.title';
export const PERSON_STATUS_DEACTIVATED = 'Deactivated';
export const DEACTIVATED_TRANSLATION_KEY = 'person.status.deactivated.title';
export const PERSON_STATUS_MISSING_VALUE = 'Missing value';
export const PERSON_STATUS_LABEL = 'Status:';
export const PERSON_STATUS_MODAL_LABEL = 'Update the person status';
export const PERSON_STATUS_MODAL_INSTRUCTION = 'Change person status';
export const PERSON_STATUS_MODAL_FIELD_LABEL = 'Person status:';
export const PERSON_STATUS_MODAL_REASON_FIELD_LABEL = 'Reason:';
export const DEACTIVATED_KEY = 'DEACTIVATED';
export const ACTIVATED_KEY = 'ACTIVATED';
export const MISSING_VALUE_KEY = 'MISSING_VALUE';
export const MISSING_VALUE_TRANSLATION_KEY = 'person.status.missing.title';
export const NO_CONSENT_KEY = 'NO_CONSENT';
export const NO_CONSENT_TRANSLATION_KEY = 'person.status.no_consent.title';
export const PERSON_STATUS_CHANGE_SUCCESS = 'The person status has been successfully changed.';
export const PERSON_STATUS_CHANGE_FAILURE = 'Unable to change the person status.';
export const PERSON_STATUSES = {
  NO_CONSENT: {
    key: NO_CONSENT_TRANSLATION_KEY,
    value: NO_CONSENT_KEY,
    label: PERSON_STATUS_NO_CONSENT
  },
  ACTIVATED: {
    key: ACTIVATED_TRANSLATION_KEY,
    value: ACTIVATED_KEY,
    label: PERSON_STATUS_ACTIVATED
  },
  DEACTIVATED: {
    key: DEACTIVATED_TRANSLATION_KEY,
    value: DEACTIVATED_KEY,
    label: PERSON_STATUS_DEACTIVATED
  },
  MISSING_VALUE: {
    key: MISSING_VALUE_TRANSLATION_KEY,
    value: MISSING_VALUE_KEY,
    label: PERSON_STATUS_MISSING_VALUE
  }
};