/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import React, { PureComponent } from 'react';
import DatePicker from 'react-datepicker';
import { startOfDay, format, parse } from 'date-fns';
import 'react-datepicker/dist/react-datepicker.css';

import { DATE_FORMAT, ISO_DATE_FORMAT } from '../../utils/messages';
import { parseDateOrDefault, ParsableToDate } from '../../utils/date-util';
import DateDisplay from './date-display';
import './index.scss';

interface IProps {
  minDate?: ParsableToDate;
  maxDate?: ParsableToDate;
  value: string;
  onChange?: (isoDate: string) => void;
}

interface IState {
}

class CustomDatePicker extends PureComponent<IProps, IState> {

  getDate = (val: ParsableToDate): Date => {
    const defalutDate = !!this.props.minDate
          ? parse(this.props.minDate)
          : parse(Date.now());
    return parseDateOrDefault(val, defalutDate);
  }; 

  handleChange = (newValue: string) => {
    const date: Date = this.getDate(newValue);
    if (!!this.props.onChange) {
      let parsed: string = format(
        startOfDay(date), ISO_DATE_FORMAT);
      this.props.onChange(parsed);
    }
  };

  render() {
    const { value, minDate, maxDate } = this.props;
    const parsed = this.getDate(value);

    return (
      <DatePicker
        customInput={<DateDisplay />}
        dateFormat={DATE_FORMAT}
        onChange={this.handleChange}
        // TODO: CFLM-308 decide if minDate
        // should be always minDate or maybe
        // min(savedOnBackend, minDate)
        minDate={minDate}
        maxDate={maxDate}
        selected={parsed}
      />
    );
  };
}

export default CustomDatePicker;
