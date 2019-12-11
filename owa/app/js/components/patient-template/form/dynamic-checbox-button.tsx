import React from 'react';
import { FormGroup, Checkbox, ControlLabel } from 'react-bootstrap';
import _ from 'lodash';

import { TemplateFieldValueUI } from '../../../shared/model/template-field-value-ui';

interface IProps {
  options: ReadonlyArray<string>
  selectedOptions: string[];
  label: string;
  key: string;
  onSelectChange: (valueSelected: string) => void;
}

export default class DynamicCheckboxButton extends React.Component<IProps> {

  isChecked = (option: string) => {
    return _.includes(this.props.selectedOptions, option);
  };

  onChange = (option: string) => {
    let options: string[] = _.cloneDeep(this.props.selectedOptions);
    if (this.isChecked(option)) {
      options = _.pull(options, option);
    } else {
      options.push(option);
    }
    this.props.onSelectChange(_.join(options, ','));
  };

  render = () => {
    let { label, key, options } = this.props;

    return (
      <FormGroup controlId={key} key={key}>
        <ControlLabel>{`${label} ${key}`}</ControlLabel>
        {options.map(option => (
          <Checkbox
            name={`${option}`}
            inline
            checked={this.isChecked(option)}
            className="u-mt-8"
            onChange={() => this.onChange(option)}>
            {option}
          </Checkbox>
        ))}
      </FormGroup>
    )
  };
}
