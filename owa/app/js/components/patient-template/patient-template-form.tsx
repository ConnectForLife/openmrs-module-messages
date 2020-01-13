/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import React from 'react';
import { connect } from 'react-redux';
import { Form, FormGroup, ControlLabel } from 'react-bootstrap';
import _ from 'lodash';
import OpenMrsDatePicker from '@bit/soldevelo-omrs.cfl-components.openmrs-date-picker';
import FormLabel from '@bit/soldevelo-omrs.cfl-components.form-label';
import ErrorDesc from '@bit/soldevelo-omrs.cfl-components.error-description';

import { updatePatientTemplate } from '../../reducers/patient-template.reducer';
import { PatientTemplateUI } from '../../shared/model/patient-template-ui';
import { TemplateUI } from '../../shared/model/template-ui';
import { TemplateFieldValueUI } from '../../shared/model/template-field-value-ui';
import { TemplateFieldType } from '../../shared/model/template-field-type';
import DynamicRadioButton from './form/dynamic-radio-button';
import DynamicMultiselect from './form/dynamic-multiselect';
import DynamicCheckboxButton from './form/dynamic-checbox-button';
import InputField from './form/input-field';
import { CATEGORIES_MAP } from './form/dynamic-multiselect.constants';
import RadioWrappedContainer, { InitInput } from './form/radio-wrapper';
import {
  PATIENT_TEMPLATE_START_DATE,
  PATIENT_TEMPLATE_END_DATE,
  PATIENT_ROLE
} from '../../shared/utils/messages';
import { factory } from './form/type-factory';
import { InputTypeEnum } from './form/radio-wrapper/parsable-input';


interface IReactProps {
  patientTemplate: PatientTemplateUI | undefined;
  template: TemplateUI;
  patientId: number;
  actorId: number;
  actorName?: string;
  actorTypeId?: number;
}

interface IProps extends IReactProps, DispatchProps {
}

interface IState {
  defaultPatientTemplate: PatientTemplateUI;
}

const elements: InitInput[] = [
  factory(InputTypeEnum.NO_DATE),
  factory(InputTypeEnum.DATE_PICKER),
  factory(InputTypeEnum.AFTER_TIMES)
];

class PatientTemplateForm extends React.Component<IProps, IState> {

  constructor(props: IProps) {
    super(props);
    this.state = {
      defaultPatientTemplate: PatientTemplateUI.getNew(props.patientId, props.actorId, props.template, props.actorTypeId)
    }
  }

  getPatientTemplate = () => this.props.patientTemplate || this.state.defaultPatientTemplate;

  onTemplateFieldValueChange = (templateFieldLocalId: string, value: string) => {
    const patientTemplate = this.getPatientTemplate();
    const tfvToUpdate = patientTemplate.templateFieldValues.find(tfv => tfv.localId === templateFieldLocalId)!;
    tfvToUpdate.value = value;
    this.props.updatePatientTemplate(patientTemplate, [this.props.template]);
  };

  renderField = (tfv: TemplateFieldValueUI) => {
    const fieldType: TemplateFieldType = tfv.getFieldType(this.props.template);
    const fieldName: string = tfv.getFieldName(this.props.template);
    const isMandatory: boolean = tfv.isMandatory(this.props.template);
    switch (fieldType) {
      case TemplateFieldType.SERVICE_TYPE:
        return this.renderDynamicRadioButton(tfv, ['Call', 'SMS', 'Deactivate service'],
          fieldName, isMandatory);
      case TemplateFieldType.DAY_OF_WEEK:
        return this.renderDynamicCheckboxButton(tfv,
          ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'],
          fieldName, isMandatory);
      case TemplateFieldType.MESSAGING_FREQUENCY:
        return this.renderDynamicRadioButton(tfv, ['Daily', 'Weekly', 'Monthly'], fieldName, isMandatory);
      case TemplateFieldType.CATEGORY_OF_MESSAGE:
        return this.renderDynamicMultiselect(tfv, Object.keys(CATEGORIES_MAP), fieldName, isMandatory);
      case TemplateFieldType.START_OF_MESSAGES:
        return this.renderDatePicker(tfv, PATIENT_TEMPLATE_START_DATE, isMandatory);
      case TemplateFieldType.END_OF_MESSAGES:
        return (
          <RadioWrappedContainer
            key={tfv.localId}
            id={tfv.localId}
            initValue={tfv.value}
            isMandatory={isMandatory}
            label={PATIENT_TEMPLATE_END_DATE}
            fieldName={fieldName}
            initElements={elements}
            fieldValueChange={this.onTemplateFieldValueChange} />
        );
      default:
        return this.renderInputField(tfv, fieldName, isMandatory);
    };
  };

  addErrorDesc = (el: JSX.Element, error: string | null) => {
    return <>
      {el}
      {error && <ErrorDesc field={error} />}
    </>
  }

  renderDynamicMultiselect = (tfv: TemplateFieldValueUI,
    options: ReadonlyArray<string>,
    fieldName: string,
    isMandatory: boolean) => (
      <DynamicMultiselect
        options={options}
        selectedOptions={tfv.value}
        label={fieldName}
        key={tfv.localId}
        mandatory={isMandatory}
        onSelectChange={(value: string) => this.onTemplateFieldValueChange(tfv.localId, value)}
      />
    )

  renderDynamicRadioButton = (tfv: TemplateFieldValueUI,
    options: ReadonlyArray<string>,
    fieldName: string,
    isMandatory: boolean) => {
    return (
      <DynamicRadioButton
        options={options}
        selectedOption={tfv.value}
        label={fieldName}
        key={tfv.localId}
        id={tfv.localId}
        mandatory={isMandatory}
        onSelectChange={(value: string) => this.onTemplateFieldValueChange(tfv.localId, value)}
      />
    );
  }

  renderDynamicCheckboxButton = (tfv: TemplateFieldValueUI,
    options: ReadonlyArray<string>,
    fieldName: string,
    isMandatory: boolean) => {
    const value = _.split(tfv.value, ',');
    return (
      <DynamicCheckboxButton
        options={options}
        selectedOptions={value}
        label={fieldName}
        fieldName={fieldName}
        key={tfv.localId}
        id={tfv.localId}
        mandatory={isMandatory}
        onSelectChange={(value: string) => this.onTemplateFieldValueChange(tfv.localId, value)} />
    );
  };

  renderInputField = (tfv: TemplateFieldValueUI,
    fieldName: string,
    isMandatory: boolean) => {
    return (
      <InputField
        fieldName={fieldName}
        value={tfv.value}
        label={fieldName}
        key={tfv.localId}
        mandatory={isMandatory}
        handleChange={(value: string) => {
          this.onTemplateFieldValueChange(tfv.localId, value)
        }} />
    );
  };

  renderDatePicker = (tfv: TemplateFieldValueUI, fieldName: string, isMandatory: boolean) => (
    <FormGroup controlId={tfv.localId} key={tfv.localId}>
      <FormLabel
        label={fieldName}
        mandatory={isMandatory} />
      <OpenMrsDatePicker
        value={tfv.value}
        onChange={isoDate => this.onTemplateFieldValueChange(tfv.localId, isoDate)}
      />
    </FormGroup>
  );

  render = () => {
    const patientTemplate = this.getPatientTemplate();
    const templateName = this.props.template ? this.props.template.name : '';
    const { actorName } = this.props;

    return (
      <div>
        <Form className="fields-form">
          <ControlLabel className="fields-form-title">
            <h4>{`${templateName} - ${actorName ? actorName : PATIENT_ROLE}`}</h4>
          </ControlLabel>
          {patientTemplate.templateFieldValues.map(tfv => this.addErrorDesc(
            this.renderField(tfv), tfv.templateFieldId ? this.getPatientTemplate().errors[tfv.templateFieldId] : null))}
        </Form>
      </div>
    );
  };
}

const mapDispatchToProps = ({
  updatePatientTemplate
});

type DispatchProps = typeof mapDispatchToProps;

export default connect(
  undefined,
  mapDispatchToProps
)(PatientTemplateForm);
