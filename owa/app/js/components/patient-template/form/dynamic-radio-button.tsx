import React from 'react';
import { FormGroup, Radio } from 'react-bootstrap';

import FormLabel from './form-label';

interface IProps {
  options: ReadonlyArray<string>
  selectedOption: string;
  label: string;
  key: string;
  mandatory: boolean;
  onSelectChange: (valueSelected: string) => void;
}

export default class DynamicRadioButton extends React.Component<IProps> {

  render = () => {
    return (
      <FormGroup controlId={this.props.key} key={this.props.key}>
        <FormLabel label={this.props.label} mandatory={this.props.mandatory} />
        {this.props.options.map(option => (
          <Radio
            name={`${option}`}
            inline
            checked={option === this.props.selectedOption}
            className="u-mt-8"
            onChange={() => this.props.onSelectChange(option)}>
            {option}
          </Radio>
        ))}
      </FormGroup>
    )
  }
}
