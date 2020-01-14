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
import { parseType } from '../type-factory';
import { ValueType, InputParser, InputTypeEnum, EMPTY } from './parsable-input';
import './index.scss';

export interface WrappedInputProps {
  value?: string,
  onChange?: (value: any) => void;
}

export type RadioValue = {
  id: string;
  checked: boolean;
  value: string;
}

export type InitInput = {
  type: InputTypeEnum,
  defaultValue?: string,
  parser: InputParser,
  labelBefore?: string;
  element?: JSX.Element;
  labelAfter?: string;
}

type RadioValueContainer = {
  input: InitInput;
  radioValue: RadioValue;
}

interface IProps {
  id: string,
  fieldName: string,
  isMandatory: boolean;
  label: string;
  initValue: string;
  initElements: InitInput[];
  fieldValueChange: (fieldId: string, value: string) => void;
}

interface IState {
  values: RadioValueContainer[];
}

export default class RadioWrappedContainer extends React.Component<IProps, IState> {

  public static defaultProps = {
    isMandatory: false
  };

  constructor(props) {
    super(props);
    const withType: ValueType = parseType(props.initValue);
    const values: RadioValueContainer[] = this.props.initElements.map(
      init => this.mapInits(init, withType));

    this.state = {
      values
    };
  }

  mapInits = (init: InitInput, fetched: ValueType) => {
    if (init.type == fetched.name) {
      return {
        input: init,
        radioValue: {
          id: init.type,
          value: fetched.value,
          checked: true
        }
      } as RadioValueContainer;
    } else {
      return {
        input: init,
        radioValue: {
          id: init.type,
          value: init.defaultValue,
          checked: false
        }
      } as RadioValueContainer;
    }
  }

  mapUpdates = (el: RadioValueContainer, radio: RadioValue) => {
    if (el.radioValue.id === radio.id) {
      const updated = _.cloneDeep(el);
      updated.radioValue.value = radio.value;
      updated.radioValue.checked = true;
      return updated;
    } else {
      const updated = _.cloneDeep(el);
      updated.radioValue.checked = false;
      return updated;
    }
  }

  serializeValue = (values: RadioValueContainer[]) => {
    let value: string = EMPTY;
    values.forEach(({ input, radioValue }) => {
      if (radioValue.checked) {
        value = input.parser.serialize(radioValue.value);
      }
    });
    return value;
  }

  update = (radio: RadioValue) => {
    const values = _.map(this.state.values, el => this.mapUpdates(el, radio));
    this.setState({ values });
    if (radio.checked) {
      this.props.fieldValueChange(this.props.id, this.serializeValue(values));
    }
  }

  onChange = (id: string, checked: boolean, value: string) => {
    this.update({ value, id, checked });
  }

  onClick = (id: string, target: HTMLInputElement) => {
    this.update({
      id,
      value: target.value,
      checked: target.checked
    });
  }

  prepareElement = ({ input, radioValue }: RadioValueContainer): JSX.Element | undefined => {
    if (!!input.element) {
      return React.cloneElement(input.element, {
        onChange: (value: string) => { this.onChange(radioValue.id, radioValue.checked, value); },
        value: radioValue.value
      });
    } else {
      return input.element;
    }
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
          {this.state.values.map((container: RadioValueContainer) => {
            const { radioValue, input } = container;
            const subId = `${id}-${input.type}`;
            return (
              <RadioWrapper
                key={subId}
                id={subId}
                onClick={(target: HTMLInputElement) => this.onClick(input.type, target)}
                radio={radioValue}
                labelBefore={input.labelBefore}
                labelAfter={input.labelAfter}
                inputJsx={this.prepareElement(container)}
              />
            );
          })}
        </div>
      </FormGroup>
    );
  }
}
