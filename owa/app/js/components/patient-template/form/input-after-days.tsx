import React from 'react';

import { WrappedInputProps } from './radio-wrapper-container';
import './input-after-days.scss';

interface IProps extends WrappedInputProps {
  min?: number;
  max?: number;
  disabled?: boolean;
  step?: number;
}

const InputAfterDays = (props: IProps) => {

  const handleChange = (target: HTMLInputElement) => {
    if (!!props.onChange) {
      props.onChange(target.value);
    }
  }

  return (
    <input
      className="input-after-days"
      type="number"
      min={props.min}
      max={props.max}
      disabled={props.disabled}
      step={props.step}
      value={props.value}
      onChange={(e: any) => { handleChange(e.target as HTMLInputElement) }} />
  );
}

InputAfterDays.defaultProps = {
  min: 0,
  max: 31,
  disabled: false,
  step: 1,
  value: 0
}

export default InputAfterDays;