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
import { FormGroup, FormControl } from 'react-bootstrap';

import { WrappedInputProps } from './radio-wrapper';
import { TimeType } from '../../../shared/enums/time-type';
import { IOption, getEmptyOption } from '../../../shared/model/dropdown-opton';

interface IProps extends WrappedInputProps { }

export default class OtherTimesDropdown extends React.Component<IProps> {

  getOptions = (): ReadonlyArray<IOption> => [
    getEmptyOption(),
    {
      type: TimeType.MONTH,
      value: 1,
      label: "After 1 month"
    },
    {
      type: TimeType.MONTH,
      value: 3,
      label: "After 3 months"
    },
    {
      type: TimeType.MONTH,
      value: 6,
      label: "After 6 months"
    }
  ];

  parseOptionValue = (option: IOption) => `${option.type}|${option.value}`;

  handleChange = (target: HTMLInputElement) => {
    if (!!this.props.onChange) {
      this.props.onChange(target.value);
    }
  }

  render = () => {
    return (
      <FormGroup controlId="formControlsSelect">
        <FormControl componentClass="select" value={this.props.value} onChange={(e: any) => { this.handleChange(e.target as HTMLInputElement) }}>
          {this.getOptions().map((option: IOption, idx: number) => (
            <option key={idx} value={this.parseOptionValue(option)}>{option.label}</option>
          ))}
        </FormControl>
      </FormGroup>
    )
  }
}