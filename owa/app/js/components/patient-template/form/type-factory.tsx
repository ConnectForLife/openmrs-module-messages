/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import React from 'react';
import { startOfDay, format } from 'date-fns';
import { ISO_DATE_FORMAT } from '../../../shared/utils/date-util';
import OpenMrsDatePicker from '../../openmrs-date-picker/openmrs-date-picker';

import { ValueType, InputTypeEnum, SEPARATOR, EMPTY } from './radio-wrapper/parsable-input';
import _ from 'lodash';
import { InitInput } from './radio-wrapper';
import {
  NO_END_DATE_LABEL,
  DATE_PICKER_END_DATE_LABEL,
  AFTER_DAYS_BEFORE,
  AFTER_DAYS_AFTER,
  OTHER
} from '../../../shared/utils/messages';
import AfterTimesInput from './after-times-input';
import OtherTimesDropdown from './other-times-dropdown';

export const parseType = (inputValue: string): ValueType => {
  const result: ValueType = {
    name: InputTypeEnum.NO_DATE,
    value: EMPTY
  };
  const chain = _.split(inputValue, SEPARATOR);
  if (chain.length >= 2) {
    const first = _.first(chain);
    result.value = _.join(_.tail(chain), SEPARATOR);
    if (!!first) {
      result.name = !!InputTypeEnum[first] ? InputTypeEnum[first] : result.name;
    }
  }

  return result;
}

export const factory = (type: InputTypeEnum): InitInput => {
  switch (type) {
    case InputTypeEnum.NO_DATE: {
      return noDate;
    }
    case InputTypeEnum.DATE_PICKER: {
      return datePicker;
    }
    case InputTypeEnum.AFTER_TIMES: {
      return afterDays;
    }
    case InputTypeEnum.OTHER: {
      return other;
    }
    default: {
      return noDate;
    }
  }
}

const noDateParse = (value: string): string => {
  return value;
}

const noDateSerialize = (value: string): string => {
  return InputTypeEnum.NO_DATE + SEPARATOR + value;
}

const datePickerParse = (value: string): string => {
  return value;
}

const datePickerSerialize = (value: string): string => {
  return InputTypeEnum.DATE_PICKER + SEPARATOR + value;
}

const afterDaysParse = (value: string): string => {
  return value;
}

const afterDaysSerialize = (value: string): string => {
  return InputTypeEnum.AFTER_TIMES + SEPARATOR + value;
}

const othersParse = (value: string): string => {
  return value;
}

const othersSerialize = (value: string): string => {
  return InputTypeEnum.OTHER + SEPARATOR + value;
}

const noDate: InitInput = {
  type: InputTypeEnum.NO_DATE,
  defaultValue: EMPTY,
  parser: {
    parse: noDateParse,
    serialize: noDateSerialize,
  },
  labelBefore: NO_END_DATE_LABEL
};

const datePicker: InitInput = {
  type: InputTypeEnum.DATE_PICKER,
  defaultValue: format(startOfDay(Date.now()), ISO_DATE_FORMAT),
  parser: {
    parse: datePickerParse,
    serialize: datePickerSerialize
  },
  labelBefore: DATE_PICKER_END_DATE_LABEL,
  element: (<OpenMrsDatePicker
    value=''
    minDate={new Date()}/>)
};

const afterDays: InitInput = {
  type: InputTypeEnum.AFTER_TIMES,
  defaultValue: '7',
  parser: {
    parse: afterDaysParse,
    serialize: afterDaysSerialize
  },
  labelBefore: AFTER_DAYS_BEFORE,
  element: <AfterTimesInput />,
  labelAfter: AFTER_DAYS_AFTER
};

const other: InitInput = {
  type: InputTypeEnum.OTHER,
  defaultValue: '',
  parser: {
    parse: othersParse,
    serialize: othersSerialize
  },
  labelBefore: OTHER,
  element: <OtherTimesDropdown />
}
