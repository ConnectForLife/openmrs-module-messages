import React from 'react';
import { ControlLabel } from 'react-bootstrap';

import * as Msg from '../../../shared/utils/messages';
import './form-component.scss';

interface FormLabel {
  label: string,
  mandatory?: boolean
}

const FormLabel: React.SFC<FormLabel> = (props: FormLabel) => {
  return (
    <ControlLabel>
      {props.label}
      {!!props.mandatory
        ? <p className="label-required">{` (${Msg.REQUIERD_FORM_FIELD})`}</p>
        : null}
    </ControlLabel>
  );
}

FormLabel.defaultProps = {
  label: 'elo',
  mandatory: false
}

export default FormLabel;
