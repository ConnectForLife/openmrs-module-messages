import React from 'react';
import { FormGroup, Radio, ControlLabel } from 'react-bootstrap';

interface IProps {
  options: ReadonlyArray<string>
  selectedOption: string;
  label: string;
  key: string;
  onSelectChange: (valueSelected: string) => void;
}

export default class DynamicRadioButton extends React.Component<IProps> {

  render = () => {
    return (
      <FormGroup controlId={this.props.key} key={this.props.key}>
        <ControlLabel>{this.props.label + ' ' + this.props.key}</ControlLabel>
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