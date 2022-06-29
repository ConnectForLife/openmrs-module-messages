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

import { WrappedInputProps } from './radio-wrapper';
import './after-times-input.scss';

interface IProps extends WrappedInputProps {
  min?: number;
  max?: number;
  disabled?: boolean;
  step?: number;
}

export default class AfterTimesInput extends React.Component<IProps> {

  static defaultProps = {
    min: 1,
    disabled: false,
    step: 1,
    value: '0'
  }

  handleChange = (target: HTMLInputElement) => {
    if (!!this.props.onChange) {
      this.props.onChange(target.value);
    }
  }

  render() {
    let { min, max, disabled, step, value } = this.props;
    return (
      <input
        className="input-after-days"
        type="number"
        min={min}
        max={max}
        disabled={disabled}
        step={step}
        value={value}
        onChange={(e: any) => { this.handleChange(e.target as HTMLInputElement) }} />
    );
  }
}
