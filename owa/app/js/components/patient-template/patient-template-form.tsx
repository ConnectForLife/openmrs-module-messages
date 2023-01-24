/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import React from 'react';
import {connect} from 'react-redux';
import {ControlLabel, Form, FormGroup} from 'react-bootstrap';
import _ from 'lodash';
import OpenMrsDatePicker from '../openmrs-date-picker/openmrs-date-picker';
import FormLabel from '../form-label/form-label';
import ErrorDesc from '../error-description/error-desc';

import {updatePatientTemplate} from '../../reducers/patient-template.reducer';
import {PatientTemplateUI} from '../../shared/model/patient-template-ui';
import {TemplateUI} from '../../shared/model/template-ui';
import {TemplateFieldValueUI} from '../../shared/model/template-field-value-ui';
import {TemplateFieldType} from '../../shared/model/template-field-type';
import DynamicRadioButton from './form/dynamic-radio-button';
import DynamicMultiselect from './form/dynamic-multiselect';
import DynamicCheckboxButton from './form/dynamic-checbox-button';
import InputField from './form/input-field';
import RadioWrappedContainer, {InitInput} from './form/radio-wrapper';
import {PATIENT_ROLE, PATIENT_TEMPLATE_END_DATE, PATIENT_TEMPLATE_START_DATE} from '../../shared/utils/messages';
import {
  getDayOfWeekValues,
  getMessagingFrequencyDailyOrWeeklyOrMonthlyValues,
  getMessagingFrequencyWeeklyOrMonthlyValues,
  getServiceTypeValues
} from '../../shared/utils/field-values';
import {factory} from './form/type-factory';
import {InputTypeEnum} from './form/radio-wrapper/parsable-input';
import {IActor} from '../../shared/model/actor.model';
import {IRootState} from '../../reducers';
import {IActorType} from '../../shared/model/actor-type.model';
import MultiselectOption from '../../shared/model/multiselect-option';

interface IReactProps {
  patientTemplate: PatientTemplateUI | undefined;
  template: TemplateUI;
  patientId: number;
  actor: IActor;
}

interface IProps extends IReactProps, DispatchProps, StateProps {
  locale?: string
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
    const actorType: IActorType | undefined = this.getActorType(props.actor && props.actor.relationshipTypeId)
    this.state = {
      defaultPatientTemplate: PatientTemplateUI.getNew(
        props.patientId,
        props.template,
        actorType && actorType.relationshipTypeDirection ? actorType.relationshipTypeDirection : undefined,
        actorType && actorType.relationshipTypeId ? actorType.relationshipTypeId : undefined,
        props.actor
      )
    }
  }

  componentDidMount = () => {
    this.props.updatePatientTemplate(this.getPatientTemplate(), [this.props.template], this.props.locale, this.isDefaultPatientTemplateLoaded());
  };

  getActorType = (relationshipTypeId: number) =>
    this.props.actorTypes.find((actorType) => actorType.relationshipTypeId === relationshipTypeId);

  getPatientTemplate = () => this.props.patientTemplate || this.state.defaultPatientTemplate;

  isDefaultPatientTemplateLoaded = () => !this.props.patientTemplate;

  onTemplateFieldValueChange = (templateFieldLocalId: string, value: string) => {
    const patientTemplate = this.getPatientTemplate();
    const tfvToUpdate = patientTemplate.templateFieldValues.find(tfv => tfv.localId === templateFieldLocalId)!;
    tfvToUpdate.value = value;
    tfvToUpdate.isTouched = true;
    this.props.updatePatientTemplate(patientTemplate, [this.props.template], this.props.locale);
  };

  renderField = (tfv: TemplateFieldValueUI) => {
    const fieldType: TemplateFieldType = tfv.getFieldType(this.props.template);
    const fieldName: string = tfv.getFieldName(this.props.template);
    const isMandatory: boolean = tfv.isMandatory(this.props.template);
    switch (fieldType) {
      case TemplateFieldType.SERVICE_TYPE:
        const possibleValues = tfv.getFieldDefinitions(this.props.template).possibleValues;
        return this.renderDynamicRadioButton(tfv, getServiceTypeValues(possibleValues, this.props.locale), fieldName, isMandatory);
      case TemplateFieldType.DAY_OF_WEEK:
        return this.renderDynamicDayOfWeekButton(tfv, getDayOfWeekValues(this.props.locale), fieldName, isMandatory);
      case TemplateFieldType.DAY_OF_WEEK_SINGLE:
        return this.renderDynamicRadioButton(tfv, getDayOfWeekValues(this.props.locale), fieldName, isMandatory);
      case TemplateFieldType.MESSAGING_FREQUENCY_DAILY_OR_WEEKLY_OR_MONTHLY:
        return this.renderDynamicRadioButton(tfv, getMessagingFrequencyDailyOrWeeklyOrMonthlyValues(this.props.locale),
          fieldName, isMandatory);
      case TemplateFieldType.MESSAGING_FREQUENCY_WEEKLY_OR_MONTHLY:
        return this.renderDynamicRadioButton(tfv, getMessagingFrequencyWeeklyOrMonthlyValues(this.props.locale),
          fieldName, isMandatory);
      case TemplateFieldType.CATEGORY_OF_MESSAGE:
        return this.renderHealthTipCategoryMultiselect(
            tfv,
            fieldName,
            isMandatory
        );
      case TemplateFieldType.START_OF_MESSAGES:
        return this.renderDatePicker(tfv, PATIENT_TEMPLATE_START_DATE, isMandatory,
          ['Start of daily messages', 'Start of messages'].includes(fieldName));
      case TemplateFieldType.END_OF_MESSAGES:
        return (
          <RadioWrappedContainer
            key={(this.props.actor ? this.props.actor.actorId : this.props.patientId) + ' ' + tfv.localId}
            id={tfv.localId}
            initValue={tfv.value}
            mandatory={isMandatory}
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

  renderHealthTipCategoryMultiselect = (tfv: TemplateFieldValueUI,
                                        fieldName: string,
                                        isMandatory: boolean) => {
      const multiselectOptions =
          this.props.healthTipCategories ?
              this.props.healthTipCategories.map(category => new MultiselectOption(category.label, category.name)) : [];

      return <DynamicMultiselect
        options={multiselectOptions}
        selectedOptions={tfv.value}
        label={fieldName}
        key={(this.props.actor ? this.props.actor.actorId : this.props.patientId) + ' ' + tfv.localId}
        mandatory={isMandatory}
        onSelectChange={(value: string) => this.onTemplateFieldValueChange(tfv.localId, value)}
      />
    };

  renderDynamicDayOfWeekButton = (tfv: TemplateFieldValueUI,
    options: ReadonlyArray<string>,
    fieldName: string,
    isMandatory: boolean) => {
    if (this.isDayOfWeekMultiple()) {
      return this.renderDynamicCheckboxButton(tfv, options, fieldName, isMandatory);
    } else {
      const isMultipleValueProvided = tfv.value != null && tfv.value.indexOf(',') !== -1;
      if (isMultipleValueProvided) {
        tfv.value = '';
      }
      return this.renderDynamicRadioButton(tfv, options, fieldName, isMandatory);
    }
  }

  renderDynamicRadioButton = (tfv: TemplateFieldValueUI,
    options: ReadonlyArray<string>,
    fieldName: string,
    isMandatory: boolean) => {
    return (
      <DynamicRadioButton
        options={options}
        selectedOption={tfv.value}
        label={fieldName}
        key={(this.props.actor ? this.props.actor.actorId : this.props.patientId) + ' ' + tfv.localId}
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
        key={(this.props.actor ? this.props.actor.actorId : this.props.patientId) + ' ' + tfv.localId}
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
        key={(this.props.actor ? this.props.actor.actorId : this.props.patientId) + ' ' + tfv.localId}
        mandatory={isMandatory}
        handleChange={(value: string) => {
          this.onTemplateFieldValueChange(tfv.localId, value)
        }} />
    );
  };

  renderDatePicker = (tfv: TemplateFieldValueUI, fieldName: string, isMandatory: boolean, isStartDate: boolean) => (
    <FormGroup controlId={tfv.localId} key={(this.props.actor ? this.props.actor.actorId : this.props.patientId) + ' ' + tfv.localId}>
      <FormLabel
        label={fieldName}
        mandatory={isMandatory} />
      <OpenMrsDatePicker
        value={tfv.value}
        onChange={isoDate => this.onTemplateFieldValueChange(tfv.localId, isoDate)}
        minDate={isStartDate ? new Date() : undefined}
      />
    </FormGroup>
  );

  private isDayOfWeekMultiple = () => {
    const patientTemplate = this.getPatientTemplate();
    const weeklyMonthlyFrequency = patientTemplate
      .templateFieldValues
      .find(f => f.getFieldType(this.props.template) === TemplateFieldType.MESSAGING_FREQUENCY_WEEKLY_OR_MONTHLY);
    if (!!weeklyMonthlyFrequency) {
      return false;
    }

    const dailyWeeklyMonthlyFrequency = patientTemplate
      .templateFieldValues
      .find(f => f.getFieldType(this.props.template) === TemplateFieldType.MESSAGING_FREQUENCY_DAILY_OR_WEEKLY_OR_MONTHLY);
    if (!!dailyWeeklyMonthlyFrequency) {
      const daily = getMessagingFrequencyDailyOrWeeklyOrMonthlyValues(this.props.locale)[0];
      return dailyWeeklyMonthlyFrequency.value === daily;
    }

    return true;
  }

  render = () => {
    const patientTemplate = this.getPatientTemplate();
    const templateName = this.props.template ? this.props.template.name : '';
    const { actor } = this.props;

    return (
      <div>
        <Form className="fields-form">
          <ControlLabel className="fields-form-title">
            <h4>{`${templateName} - ${actor && actor.actorName ? actor.actorName : PATIENT_ROLE}`}</h4>
          </ControlLabel>
          {patientTemplate.templateFieldValues.map(tfv => this.addErrorDesc(
            this.renderField(tfv), tfv.templateFieldId ? this.getPatientTemplate().errors[tfv.templateFieldId] : null))}
        </Form>
      </div>
    );
  };
}

const mapStateToProps = ({ adminSettings }: IRootState) => ({
  actorTypes: adminSettings.actorTypes,
  healthTipCategories: adminSettings.healthTipCategories
});

const mapDispatchToProps = ({
  updatePatientTemplate
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PatientTemplateForm);
