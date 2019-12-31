/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import React from 'react';
import { FormGroup } from 'react-bootstrap';
import _ from 'lodash';
import FormLabel from '@bit/soldevelo-omrs.cfl-components.form-label';

import RadioWrapper from './radio-wrapper';
import './radio-wrapper.scss';

export interface WrappedInputProps {
  value?: string,
  onChange?: (value: any) => void;
}

export type RadioDate = {
  id: string;
  checked: boolean;
  value: string;
}

export type InputType = {
  id: string;
  initValue: any;
  initChecked?: boolean;
  labelBefore?: string;
  labelAfter?: string;
  element?: JSX.Element;
}

type RadioDateContainer = {
  input: InputType;
  radioDate: RadioDate;
}

interface IProps {
  id: string,
  fieldName: string,
  isMandatory: boolean;
  label: string;
  initElements: InputType[];
  fieldValueChange: (fieldId: string, value: string) => void;
}

interface IState {
  values: RadioDateContainer[];
  value: string;
}

export default class RadioWrappedContainer extends React.Component<IProps, IState> {

  constructor(props) {
    super(props);
    const values: RadioDateContainer[] = this.props.initElements.map(el => {
      const value: string = el.initValue;
      const checked: boolean = !!el.initChecked;
      const radioDate: RadioDate = { id: el.id, value, checked };
      return { input: el, radioDate } as RadioDateContainer;
    });
    const value = this.getValue(values);
    this.state = {
      values,
      value
    };
  }

  getValue = (values: RadioDateContainer[]) => {
    let value: string = '';
    values.map(val => {
      if (val.radioDate.checked) {
        value = val.radioDate.value;
      }
    });
    return value;
  }

  update = (radio: RadioDate) => {
    let changed = _.map(this.state.values, (el: RadioDateContainer) => {
      if (el.radioDate.id === radio.id) {
        let updated = _.cloneDeep(el);
        updated.radioDate.value = radio.value;
        updated.radioDate.checked = true;
        return updated;
      } else {
        let updated = _.cloneDeep(el);
        updated.radioDate.checked = false;
        return updated;
      }
    });

    this.setState({
      values: changed,
      value: this.getValue(changed)
    });
    if (radio.checked) {
      this.props.fieldValueChange(this.props.id, radio.value);
    }
  }

  onChange = (id: string, checked: boolean, value: string) => {
    const radio: RadioDate = { value, id, checked };
    this.update(radio);
  }

  onClick = (id: string, target: HTMLInputElement) => {
    const radio: RadioDate = {
      id,
      value: target.value,
      checked: target.checked
    };
    this.update(radio);
  }

  prepareElement = (containter: RadioDateContainer): JSX.Element | undefined => {
    const inputJsx = containter.input.element;
    return (!!inputJsx)
      ? React.cloneElement(inputJsx, {
        onChange: (value: string) => {
          this.onChange(containter.radioDate.id,
            containter.radioDate.checked,
            value);
        },
        value: containter.radioDate.value
      })
      : inputJsx;
  }

  render = () => {
    const { isMandatory, fieldName } = this.props;
    const id = `${this.props.id}-${fieldName}`;
    return (
      <FormGroup controlId={this.props.id}>
        <FormLabel
          label={this.props.label}
          mandatory={isMandatory} />
        <div className="radio-wrapper-container">
          {this.state.values.map((val: RadioDateContainer) => {
            const subId =`${id}-${val.input.id}`;
            return (
              <RadioWrapper
                key={subId}
                id={subId}
                onClick={ (target: HTMLInputElement) => this.onClick(val.input.id, target)}
                radio={val.radioDate}
                labelBefore={val.input.labelBefore}
                labelAfter={val.input.labelAfter}
                inputJsx={this.prepareElement(val)}
              />
            );
          })}
        </div>
      </FormGroup>
    );
  }
}
