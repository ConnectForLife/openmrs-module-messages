/*
 *  This Source Code Form is subject to the terms of the Mozilla Public License,
 *  v. 2.0. If a copy of the MPL was not distributed with this file, You can
 *  obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 *  the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *  <p>
 *  Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 *  graphic logo is a trademark of OpenMRS Inc.
 */

import React, { PureComponent } from 'react';
import DatePicker from 'react-datepicker';
import { startOfDay, format, parse } from 'date-fns';
import 'react-datepicker/dist/react-datepicker.css';

import { DATE_FORMAT, ISO_DATE_FORMAT } from '../date-util/constants';
import { parseDateOrDefault, ParsableToDate } from '../date-util/date-util';
import DateDisplay from './date-display';
import './openmrs-date-picker.scss';

interface IProps {
  value: ParsableToDate;
  minDate?: Date;
  onChange?: (isoDate: string) => void;
}

interface IState {
}

class OpenMrsDatePicker extends PureComponent<IProps, IState> {

  getDate = (val: ParsableToDate | null): Date => {
    const defaultDate = parse(Date.now());
    return parseDateOrDefault(!!val ? val : defaultDate, defaultDate);
  };

  handleChange = (newValue: Date | null) => {
    const date: Date = this.getDate(newValue);
    if (!!this.props.onChange) {
      const isoDate: string = format(startOfDay(date), ISO_DATE_FORMAT);
      this.props.onChange(isoDate);
    }
  };

  render() {
    const { value } = this.props;
    const parsed = this.getDate(value);
    const minDate = this.props.minDate ? { minDate: this.props.minDate } : {};

    return (
      <span className="openmrs-date-picker">
        <DatePicker
          customInput={<DateDisplay />}
          dateFormat={DATE_FORMAT}
          onChange={this.handleChange}
          selected={parsed}
          {...minDate}
        />
      </span>
    );
  };
}

export default OpenMrsDatePicker;