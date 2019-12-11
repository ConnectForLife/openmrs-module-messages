import React from 'react';
import { FormGroup, FormControl } from 'react-bootstrap';

import FormLabel from './form-label';

interface IProps {
  label: string;
  key: string;
  value: string;
  fieldName: string
  mandatory: boolean;
  handleChange: (value: string) => void;
}

export default class InputField extends React.Component<IProps> {
  render = () => {
    let { label, key, value } = this.props;
    return (
      <FormGroup controlId={key} key={key}>
        <FormLabel label={this.props.label} mandatory={this.props.mandatory} />
        <FormControl
          type="text"
          name={label}
          value={value}
          onChange={e => { this.props.handleChange(e.target.value) }}
          className="form-control" />
      </FormGroup>
    );
  };
}
