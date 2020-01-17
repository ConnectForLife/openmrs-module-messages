import React from 'react';
import { FormGroup, FormControl } from 'react-bootstrap';
import { WrappedInputProps } from '../radio-wrapper';

enum TimeType {
  DAY = 'DAY',
  MONTH = 'MONTH',
  YEAR = 'YEAR'
}

interface IOption {
  type: TimeType;
  value: number;
  label: string;
}

interface IProps extends WrappedInputProps { }

export default class OtherTimesDropdown extends React.Component<IProps> {

  getOptions = (): ReadonlyArray<IOption> => [
    {
      type: TimeType.MONTH,
      value: 6,
      label: "After 6 months"
    },
    {
      type: TimeType.DAY,
      value: 14,
      label: "After 14 days"
    },
    {
      type: TimeType.YEAR,
      value: 1,
      label: "After 1 year"
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