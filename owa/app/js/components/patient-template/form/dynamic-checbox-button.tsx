/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import React from 'react';
import { FormGroup, Checkbox } from 'react-bootstrap';
import _ from 'lodash';

import FormLabel from '@bit/soldevelo-omrs.cfl-components.form-label';

interface IProps {
  options: ReadonlyArray<string>
  selectedOptions: string[];
  label: string;
  fieldName: string;
  id: string;
  mandatory?: boolean;
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
    let { label, fieldName, options } = this.props;
    const id = `${this.props.id}-${fieldName}`;
    return (
      <FormGroup controlId={this.props.id}>
        <FormLabel label={label} mandatory={this.props.mandatory} />
        {options.map(option => (
          <Checkbox
            key={`${id}-${option}`}
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
