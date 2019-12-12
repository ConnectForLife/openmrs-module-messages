/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import React, { PureComponent } from 'react';
import { FormControl } from 'react-bootstrap';
import 'react-datepicker/dist/react-datepicker.css';

import './index.scss';
// TODO: CFLM-308 to styling; icon should be in datepicker box
// TODO: CFLM-308 consider moving shared components
// to component repository

interface IProps {
  label?: string;
  value?: string;
  onClick?: () => void;
}

export default class DateDisplay extends PureComponent<IProps> {
  render() {
    const { onClick, value, label } = this.props;
    const placeholder = (!!label) ? label : '';

    return (
      <span className="date-display">
        <FormControl
          onChange={() => { }}
          onClick={onClick}
          placeholder={placeholder}
          type="text"
          value={value} />
        <i onClick={onClick}
          className='icon-calendar small' />
      </span>
    );
  }
}