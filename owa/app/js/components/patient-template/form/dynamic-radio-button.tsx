/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import React from 'react';
import { FormGroup, Radio } from 'react-bootstrap';

import FormLabel from '@bit/soldevelo-omrs.cfl-components.form-label';

interface IProps {
  options: ReadonlyArray<string>
  selectedOption: string;
  label: string;
  id: string;
  mandatory?: boolean;
  onSelectChange: (valueSelected: string) => void;
}

export default class DynamicRadioButton extends React.Component<IProps> {
  render = () => {
    return (
      <FormGroup controlId={this.props.id}>
        <FormLabel label={this.props.label} mandatory={this.props.mandatory} />
        {this.props.options.map(option => (
          <Radio
            key={`${this.props.id}-${option}`}
            name={option}
            inline
            checked={option === this.props.selectedOption}
            className="u-mt-8"
            onChange={() => this.props.onSelectChange(option)}>
            {option}
          </Radio>))}
      </FormGroup>
    )
  }
}
