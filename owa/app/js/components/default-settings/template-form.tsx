import React from 'react';
import { Form, FormGroup, ControlLabel } from 'react-bootstrap';
import _ from 'lodash';
import { TemplateUI } from '../../shared/model/template-ui';
import { TemplateFieldType } from '../../shared/model/template-field-type';
import OpenMrsDatePicker from '@bit/soldevelo-omrs.cfl-components.openmrs-date-picker';
import FormLabel from '@bit/soldevelo-omrs.cfl-components.form-label';
import {
  PATIENT_TEMPLATE_START_DATE,
  PATIENT_TEMPLATE_END_DATE
} from '../../shared/utils/messages';
import { TemplateFieldUI } from '../../shared/model/template-field-ui';
import RadioWrappedContainer, { InitInput } from '../patient-template/form/radio-wrapper';
import { factory } from '../patient-template/form/type-factory';
import { InputTypeEnum } from '../patient-template/form/radio-wrapper/parsable-input';
import { CATEGORIES_MAP } from '../patient-template/form/dynamic-multiselect.constants';
import DynamicMultiselect from '../patient-template/form/dynamic-multiselect';
import DynamicRadioButton from '../patient-template/form/dynamic-radio-button';
import DynamicCheckboxButton from '../patient-template/form/dynamic-checbox-button';
import InputField from '../patient-template/form/input-field';

interface IProps {
  template: TemplateUI;
  updateTemplate: (template: TemplateUI) => void;
}

const elements: InitInput[] = [
  factory(InputTypeEnum.NO_DATE),
  factory(InputTypeEnum.DATE_PICKER),
  factory(InputTypeEnum.AFTER_TIMES)
];

export class TemplateForm extends React.Component<IProps> {

  shouldComponentUpdate = (nextProps: IProps) => {
    const currentTemplate = this.props.template;
    const nextTemplate = nextProps.template;
    const result = currentTemplate.templateFields
      .some(field => nextTemplate.templateFields
        .find(nextField => nextField.localId === field.localId)!.defaultValue !== field.defaultValue);
    return result;
  }

  onTemplateFieldValueChange = (fieldLocalId: string, value: string) => {
    const template = _.cloneDeep(this.props.template);
    const fieldToUpdate = template.templateFields.find(field => field.localId === fieldLocalId)!;
    fieldToUpdate.defaultValue = value;
    this.props.updateTemplate(template);
  };

  renderField = (templateField: TemplateFieldUI) => {
    const fieldType: TemplateFieldType = templateField.type;
    const fieldName: string = templateField.name;
    
    switch (fieldType) {
      case TemplateFieldType.SERVICE_TYPE:
        return this.renderDynamicRadioButton(templateField, ['Call', 'SMS', 'Deactivate service'],
          fieldName);
      case TemplateFieldType.DAY_OF_WEEK:
        return this.renderDynamicCheckboxButton(templateField,
          ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'],
          fieldName);
      case TemplateFieldType.MESSAGING_FREQUENCY:
        return this.renderDynamicRadioButton(templateField, ['Daily', 'Weekly', 'Monthly'], fieldName);
      case TemplateFieldType.CATEGORY_OF_MESSAGE:
        return this.renderDynamicMultiselect(templateField, Object.keys(CATEGORIES_MAP), fieldName);
      case TemplateFieldType.START_OF_MESSAGES:
        return this.renderDatePicker(templateField, PATIENT_TEMPLATE_START_DATE);
      case TemplateFieldType.END_OF_MESSAGES:
        return (
          <RadioWrappedContainer
            key={templateField.localId}
            id={templateField.localId}
            initValue={templateField.defaultValue}
            label={PATIENT_TEMPLATE_END_DATE}
            fieldName={fieldName}
            initElements={elements}
            fieldValueChange={this.onTemplateFieldValueChange} />
        );
      default:
        return this.renderInputField(templateField, fieldName);
    };
  };

  renderDynamicMultiselect = (field: TemplateFieldUI,
    options: ReadonlyArray<string>,
    fieldName: string) => (
      <DynamicMultiselect
        options={options}
        selectedOptions={field.defaultValue}
        label={fieldName}
        key={field.localId}
        onSelectChange={(value: string) => this.onTemplateFieldValueChange(field.localId, value)}
      />
    )

  renderDynamicRadioButton = (field: TemplateFieldUI,
    options: ReadonlyArray<string>,
    fieldName: string) => {
    return (
      <DynamicRadioButton
        options={options}
        selectedOption={field.defaultValue}
        label={fieldName}
        key={field.localId}
        id={field.localId}
        onSelectChange={(value: string) => this.onTemplateFieldValueChange(field.localId, value)}
      />
    );
  }

  renderDynamicCheckboxButton = (field: TemplateFieldUI,
    options: ReadonlyArray<string>,
    fieldName: string) => {
    const value = _.split(field.defaultValue, ',');
    return (
      <DynamicCheckboxButton
        options={options}
        selectedOptions={value}
        label={fieldName}
        fieldName={fieldName}
        key={field.localId}
        id={field.localId}
        onSelectChange={(value: string) => this.onTemplateFieldValueChange(field.localId, value)} />
    );
  };

  renderInputField = (field: TemplateFieldUI,
    fieldName: string) => {
    return (
      <InputField
        fieldName={fieldName}
        value={field.defaultValue}
        label={fieldName}
        key={field.localId}
        handleChange={(value: string) => {
          this.onTemplateFieldValueChange(field.localId, value)
        }} />
    );
  };

  renderDatePicker = (field: TemplateFieldUI, fieldName: string) => (
    <FormGroup controlId={field.localId} key={field.localId}>
      <FormLabel label={fieldName} />
      <OpenMrsDatePicker
        value={field.defaultValue}
        onChange={isoDate => this.onTemplateFieldValueChange(field.localId, isoDate)}
      />
    </FormGroup>
  );

  render = () => {
    const { template } = this.props;
    const templateName = template ? template.name : '';
    return (
      <Form>
        <strong><h4>{templateName}</h4></strong>
        {template.templateFields.map(this.renderField)}
      </Form>
    );
  };
};
