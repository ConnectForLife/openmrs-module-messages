/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import React from 'react';

import { RadioDate } from './radio-wrapper-container';
import './radio-wrapper.scss';

interface IProps {
  id: string;
  labelBefore?: string;
  labelAfter?: string;
  inputJsx?: JSX.Element;
  radio: RadioDate;
  onChange?: (value: any) => void;
  onClick: (target: HTMLInputElement) => void;
}

const RadioWrapper = (props: IProps) => {

  const handleChange = (target: HTMLInputElement) => {
    if (!!props.onChange && props.radio.checked) {
      props.onChange(target.value);
    }
  }

  const handleClick = (target: HTMLInputElement) => {
    if (!!props.onClick) {
      props.onClick(target);
    }
  }

  const wrapInParagraph = (label: string, left: boolean): JSX.Element => {
    const clazzName: string = (left) ? 'left-text' : 'right-text';
    return <p className={clazzName}>{label}</p>
  }

  const inputClassName = (props.radio.checked) ? 'wrapped-aligned' : 'wrapped-aligned disabled';
  return (
    <label className="radio-inline">
      <div className="wrapped-aligned">
        <input
          className="radio-button"
          type="radio"
          checked={props.radio.checked}
          value={props.radio.value}
          id={`${props.id}-radio-button`}
          onChange={(e: any) => { handleChange(e.target as HTMLInputElement) }}
          onClick={(e: any) => { handleClick(e.target as HTMLInputElement) }} />
        {!!props.labelBefore ? wrapInParagraph(props.labelBefore, true) : null}
        <div className={inputClassName}>
          {!!props.inputJsx ? props.inputJsx : null}
        </div>
        {!!props.labelAfter ? wrapInParagraph(props.labelAfter, false) : null}
      </div>

    </label>
  );
};

export default RadioWrapper;
