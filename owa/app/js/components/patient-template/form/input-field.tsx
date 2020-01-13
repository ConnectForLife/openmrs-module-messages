/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import React from 'react';
import { FormGroup, FormControl } from 'react-bootstrap';

import FormLabel from '@bit/soldevelo-omrs.cfl-components.form-label';

interface IProps {
  label: string;
  key: string;
  value: string;
  fieldName: string
  mandatory?: boolean;
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
          onChange={e => this.props.handleChange((e.target as HTMLInputElement).value)} />
      </FormGroup>
    );
  };
}
