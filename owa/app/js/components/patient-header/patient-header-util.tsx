/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import { formatPhoneNumberIntl } from 'react-phone-number-input';
import moment from 'moment';

export const TELEPHONE_NUMBER_ATTRIBUTE_TYPE = 'Telephone Number';
export const LOCATION_ATTRIBUTE_TYPE = 'LocationAttribute';
export const PLUS_SIGN = '+';
export const DEFAULT_COLUMN_VALUE = '';
export const DISPLAY = 'display';

export const getPhoneNumberWithPlusSign = phoneNumber => phoneNumber && PLUS_SIGN + phoneNumber;

export const extractAttribute = (entity, type) => {
  if (!entity) {
    return DEFAULT_COLUMN_VALUE;
  }
  const attr = entity.attributes && entity.attributes.find(a => a.attributeType[DISPLAY].toLowerCase() === type.toLowerCase());
  return (attr && attr.value) || DEFAULT_COLUMN_VALUE;
};

export const extractIdentifier = (entity, type) => {
  if (!entity) {
    return DEFAULT_COLUMN_VALUE;
  }
  const identifier = entity.identifiers && entity.identifiers.find(id => id.identifierType[DISPLAY].toLowerCase() === type.toLowerCase());
  return (identifier && identifier.identifier) || DEFAULT_COLUMN_VALUE;
};

export const getPatientHeaderFieldValue = (patient, field, locations) => {
  const fieldName = field.name;

  const { person } = patient;
  const { preferredAddress, preferredName } = person;
  const attributesValue = extractAttribute(person, fieldName);
  const personValue = person[fieldName];
  let value = '';

  if (attributesValue) {
    if (fieldName === TELEPHONE_NUMBER_ATTRIBUTE_TYPE) {
      value = formatPhoneNumberIntl(attributesValue) ? getPhoneNumberWithPlusSign(attributesValue) : attributesValue;
    } else if (fieldName === LOCATION_ATTRIBUTE_TYPE) {
      value = locations.find(location => location.uuid === attributesValue)?.[DISPLAY];
    } else {
      value = attributesValue;
    }
  } else if (personValue != null) {
    if (field.type === 'date') {
      value = moment.utc(personValue).local().format(field.format);
    } else {
      value = personValue;
    }
  } else if (field.type === 'static') {
    value = field.value;
  } else {
    value = preferredName?.[fieldName] || preferredAddress?.[fieldName] || extractIdentifier(patient, fieldName);
  }

  return value;
};