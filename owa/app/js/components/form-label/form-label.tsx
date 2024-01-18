/*
 *  This Source Code Form is subject to the terms of the Mozilla Public License,
 *  v. 2.0. If a copy of the MPL was not distributed with this file, You can
 *  obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 *  the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *  <p>
 *  Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 *  graphic logo is a trademark of OpenMRS Inc.
 */

import React from 'react';
import { ControlLabel } from 'react-bootstrap';
import { injectIntl } from 'react-intl';
import './form-label.scss';

interface FormLabel {
  label: string,
  mandatory?: boolean,
  intl: any
}

const FormLabel: React.SFC<FormLabel> = (props: FormLabel) => {
  return (
    <span className='form-label'>
      <ControlLabel>
        {props.label}
        {!!props.mandatory
          ? <p className="label-required"> {`(${props.intl.formatMessage({ id: "common.required" })})`}</p>
          : null}
      </ControlLabel>
    </span>
  );
}

FormLabel.defaultProps = {
  label: '',
  mandatory: false
}

export default injectIntl(FormLabel);
