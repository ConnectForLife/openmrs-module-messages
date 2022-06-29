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

import { RadioValue } from './index';
import './index.scss';
import { EMPTY } from './parsable-input';

interface IProps {
  id: string;
  labelBefore?: string;
  labelAfter?: string;
  inputJsx?: JSX.Element;
  radio: RadioValue;
  onChange?: (target: HTMLInputElement) => void;
  onClick: (target: HTMLInputElement) => void;
}

const RadioWrapper = (props: IProps) => {

  const handleChange = (e: any) => {
    if (!!props.onChange) {
      props.onChange(e.target as HTMLInputElement);
    }
  }

  const handleClick = (e: any) => {
    props.onClick(e.target as HTMLInputElement); 
  }

  const renderInlineText = (before: boolean, label?: string): JSX.Element | null => {
    const clazzName: string = (before) ? 'left-text' : 'right-text';
    return !!label ? <p className={clazzName}>{label}</p> : null;
  }

  const renderElement = (): JSX.Element => {
    const inputClassName = (props.radio.checked)
      ? 'wrapped-aligned'
      : 'wrapped-aligned disabled';
    return (
      <div className={inputClassName}>
        {!!props.inputJsx ? props.inputJsx : null}
      </div>);
  }

  const setOrEmpty = (): string => {
    return !!props.inputJsx? props.radio.value : EMPTY;
  }

  return (
    <label className="radio-inline">
      <div className="wrapped-aligned">
        <input
          className="radio-button"
          type="radio"
          checked={props.radio.checked}
          value={setOrEmpty()}
          id={`${props.id}-radio-button`}
          onClick={handleClick}
          onChange={handleChange} />
        {renderInlineText(true, props.labelBefore)}
        {renderElement()}
        {renderInlineText(false, props.labelAfter)}
      </div>
    </label>
  );
};

export default RadioWrapper;
