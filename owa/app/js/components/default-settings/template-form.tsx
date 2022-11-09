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
import {Form, FormGroup} from 'react-bootstrap';
import _ from 'lodash';
import OpenMrsDatePicker from '../openmrs-date-picker/openmrs-date-picker';
import FormLabel from '../form-label/form-label';

import {PATIENT_TEMPLATE_END_DATE, PATIENT_TEMPLATE_START_DATE,} from '../../shared/utils/messages';
import {
  getDayOfWeekValues,
  getMessagingFrequencyDailyOrWeeklyOrMonthlyValues,
  getMessagingFrequencyWeeklyOrMonthlyValues,
  getServiceTypeValues
} from '../../shared/utils/field-values';
import {TemplateFieldUI} from '../../shared/model/template-field-ui';
import RadioWrappedContainer, {InitInput} from '../patient-template/form/radio-wrapper';
import {factory} from '../patient-template/form/type-factory';
import {InputTypeEnum} from '../patient-template/form/radio-wrapper/parsable-input';
import DynamicMultiselect from '../patient-template/form/dynamic-multiselect';
import DynamicRadioButton from '../patient-template/form/dynamic-radio-button';
import DynamicCheckboxButton from '../patient-template/form/dynamic-checbox-button';
import InputField from '../patient-template/form/input-field';
import {TemplateUI} from '../../shared/model/template-ui';
import {TemplateFieldType} from '../../shared/model/template-field-type';
import {IActorType} from '../../shared/model/actor-type.model';
import {IHealthTipCategory} from "../../shared/model/health-tip-category.model";
import MultiselectOption from "../../shared/model/multiselect-option";

interface IProps {
  template: TemplateUI;
  actorType: IActorType;
  updateTemplate: (template: TemplateUI) => void;
  healthTipCategories: IHealthTipCategory[];
  locale?: string;
}

const elements: InitInput[] = [
  factory(InputTypeEnum.NO_DATE),
  factory(InputTypeEnum.AFTER_TIMES),
  factory(InputTypeEnum.OTHER)
];

// WARNING this class overrides shouldComponentUpdate, pay special attention
export class TemplateForm extends React.Component<IProps> {

  shouldComponentUpdate = (nextProps: IProps) => {
    const currentTemplate = this.props.template;
    const nextTemplate = nextProps.template;
    const result = this.hasDefaultValueChanged(currentTemplate, nextTemplate)
      || this.hasDefaultValuesChanged(currentTemplate, nextTemplate);
    return result;
  };

  hasDefaultValueChanged = (currentTemplate: TemplateUI, nextTemplate: TemplateUI) => currentTemplate.templateFields
    .some(field => nextTemplate.templateFields
      .find(nextField => nextField.localId === field.localId)!.defaultValue !== field.defaultValue);

  hasDefaultValuesChanged = (currentTemplate: TemplateUI, nextTemplate: TemplateUI) => currentTemplate.templateFields
    .some(field => nextTemplate.templateFields
      .find(nextField => nextField.localId === field.localId)!.defaultValues !== field.defaultValues);

  isPatientTemplate = () => this.props.actorType && this.props.actorType.uuid === '';

  onTemplateFieldValueChange = (fieldLocalId: string, value: string) => {
    const { relationshipTypeId, relationshipTypeDirection } = this.props.actorType;
    const template = _.cloneDeep(this.props.template);
    const fieldToUpdate = template.templateFields.find(field => field.localId === fieldLocalId)!;
    if (this.isPatientTemplate()) {
      fieldToUpdate.defaultValue = value;
    } else {
      let existingField = fieldToUpdate.defaultValues.find((f) => f.direction === relationshipTypeDirection
        && f.relationshipTypeId === relationshipTypeId && f.templateFieldId === fieldToUpdate.id);
      if (!!existingField) {
        existingField.defaultValue = value;
      } else {
        fieldToUpdate.defaultValues.push({
          relationshipTypeId: relationshipTypeId,
          direction: relationshipTypeDirection,
          templateFieldId: fieldToUpdate.id ? fieldToUpdate.id : undefined,
          defaultValue: value
        });
      }
    }
    this.props.updateTemplate(template);
  };

  getValueForField = (templateField: TemplateFieldUI) => {
    if (this.isPatientTemplate()) {
      return templateField.defaultValue;
    } else {
      const tfdv = templateField.defaultValues && templateField.defaultValues.find(d => this.props.actorType.relationshipTypeId === d.relationshipTypeId
        && this.props.actorType.relationshipTypeDirection === d.direction)
      return !!tfdv ? tfdv.defaultValue : '';
    }
  }

  setValueForField = (templateField: TemplateFieldUI, value: string) => {
    if (this.isPatientTemplate()) {
      templateField.defaultValue = value;
    } else {
      const tfdv = templateField.defaultValues && templateField.defaultValues.find(d => this.props.actorType.relationshipTypeId === d.relationshipTypeId
        && this.props.actorType.relationshipTypeDirection === d.direction)
      if (!!tfdv) {
        tfdv.defaultValue = value;
      }
    }
  }

  renderField = (templateField: TemplateFieldUI) => {
    const fieldType: TemplateFieldType = templateField.type;
    const fieldName: string = templateField.name;
    const value = this.getValueForField(templateField);

    switch (fieldType) {
      case TemplateFieldType.SERVICE_TYPE:
        return this.renderDynamicRadioButton(templateField, getServiceTypeValues(templateField.possibleValues, this.props.locale), fieldName, value);
      case TemplateFieldType.DAY_OF_WEEK:
        return this.renderDynamicDayOfWeekButton(templateField, getDayOfWeekValues(this.props.locale), fieldName, value);
      case TemplateFieldType.DAY_OF_WEEK_SINGLE:
        return this.renderDynamicRadioButton(templateField, getDayOfWeekValues(this.props.locale), fieldName, value);
      case TemplateFieldType.MESSAGING_FREQUENCY_DAILY_OR_WEEKLY_OR_MONTHLY:
        return this.renderDynamicRadioButton(templateField,
          getMessagingFrequencyDailyOrWeeklyOrMonthlyValues(this.props.locale), fieldName, value);
      case TemplateFieldType.MESSAGING_FREQUENCY_WEEKLY_OR_MONTHLY:
        return this.renderDynamicRadioButton(templateField,
          getMessagingFrequencyWeeklyOrMonthlyValues(this.props.locale), fieldName, value);
      case TemplateFieldType.CATEGORY_OF_MESSAGE:
        return this.renderHealthTipCategoryMultiselect(
            templateField,
            fieldName,
            value
        );
      case TemplateFieldType.START_OF_MESSAGES:
        return this.renderDatePicker(templateField, PATIENT_TEMPLATE_START_DATE, value);
      case TemplateFieldType.END_OF_MESSAGES:
        return (
          <RadioWrappedContainer
            key={this.props.actorType.uuid + ' ' + templateField.localId}
            id={templateField.localId}
            initValue={value}
            label={PATIENT_TEMPLATE_END_DATE}
            fieldName={fieldName}
            initElements={elements}
            fieldValueChange={this.onTemplateFieldValueChange} />
        );
      default:
        return this.renderInputField(templateField, fieldName, value);
    };
  };

  renderHealthTipCategoryMultiselect = (field: TemplateFieldUI,
                                        fieldName: string,
                                        value: string) => {
    const multiselectOptions =
        this.props.healthTipCategories ?
            this.props.healthTipCategories.map(category => new MultiselectOption(category.label, category.name)) : [];

    return <DynamicMultiselect
        options={multiselectOptions}
        selectedOptions={value}
        label={fieldName}
        key={this.props.actorType.uuid + ' ' + field.localId}
        onSelectChange={(value: string) => this.onTemplateFieldValueChange(field.localId, value)}
    />
  };

  renderDynamicDayOfWeekButton = (field: TemplateFieldUI,
    options: ReadonlyArray<string>,
    fieldName: string,
    value: string) => {
    if (this.isDayOfWeekMultiple()) {
      return this.renderDynamicCheckboxButton(field, options, fieldName, value);
    } else {
      const isMultipleValueProvided = this.getValueForField(field).indexOf(',') !== -1;
      if (isMultipleValueProvided) {
        this.setValueForField(field, '');
      }
      return this.renderDynamicRadioButton(field, options, fieldName, value);
    }
  }

  renderDynamicRadioButton = (field: TemplateFieldUI,
    options: ReadonlyArray<string>,
    fieldName: string,
    value: string) => {
    return (
      <DynamicRadioButton
        options={options}
        selectedOption={value}
        label={fieldName}
        key={this.props.actorType.uuid + ' ' + field.localId}
        id={field.localId}
        onSelectChange={(value: string) => this.onTemplateFieldValueChange(field.localId, value)}
      />
    );
  }

  renderDynamicCheckboxButton = (field: TemplateFieldUI,
    options: ReadonlyArray<string>,
    fieldName: string,
    value: string) => {
    const formattedValue = _.split(value, ',');
    return (
      <DynamicCheckboxButton
        options={options}
        selectedOptions={formattedValue}
        label={fieldName}
        fieldName={fieldName}
        key={this.props.actorType.uuid + ' ' + field.localId}
        id={field.localId}
        onSelectChange={(value: string) => this.onTemplateFieldValueChange(field.localId, value)} />
    );
  };

  renderInputField = (field: TemplateFieldUI,
    fieldName: string,
    value: string) => {
    return (
      <InputField
        fieldName={fieldName}
        value={value}
        label={fieldName}
        key={this.props.actorType.uuid + ' ' + field.localId}
        handleChange={(value: string) => {
          this.onTemplateFieldValueChange(field.localId, value)
        }} />
    );
  };

  renderDatePicker = (field: TemplateFieldUI, fieldName: string,
    value: string) => (
      <FormGroup controlId={field.localId} key={this.props.actorType.uuid + ' ' + field.localId}>
        <FormLabel label={fieldName} />
        <OpenMrsDatePicker
          value={value}
          onChange={isoDate => this.onTemplateFieldValueChange(field.localId, isoDate)}
        />
      </FormGroup>
    );

  private isDayOfWeekMultiple = () => {
    const weeklyMonthlyFrequency = this.props
      .template
      .templateFields
      .find(f => f.type === TemplateFieldType.MESSAGING_FREQUENCY_WEEKLY_OR_MONTHLY);
    if (!!weeklyMonthlyFrequency) {
      return false;
    }

    const dailyWeeklyMonthlyFrequency = this.props
      .template
      .templateFields
      .find(f => f.type === TemplateFieldType.MESSAGING_FREQUENCY_DAILY_OR_WEEKLY_OR_MONTHLY);
    if (!!dailyWeeklyMonthlyFrequency) {
      const daily = getMessagingFrequencyDailyOrWeeklyOrMonthlyValues(this.props.locale)[0];
      return this.getValueForField(dailyWeeklyMonthlyFrequency) === daily;
    }

    return true;
  }

  render = () => {
    const { template } = this.props;
    const templateName = template ? template.name : '';
    return (
      <Form className="one-column">
        <strong><h4>{`${templateName} - ${this.props.actorType.display}`}</h4></strong>
        {template.templateFields.map(this.renderField)}
      </Form>
    );
  };
}
