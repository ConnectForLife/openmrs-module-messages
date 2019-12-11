import React from 'react';
import { connect } from 'react-redux';
import { updatePatientTemplate } from '../../reducers/patient-template.reducer';
import { Form } from 'react-bootstrap';
import _ from 'lodash';

import { PatientTemplateUI } from '../../shared/model/patient-template-ui';
import { TemplateUI } from '../../shared/model/template-ui';
import { TemplateFieldValueUI } from '../../shared/model/template-field-value-ui';
import { TemplateFieldType } from '../../shared/model/template-field-type';
import DynamicRadioButton from './form/dynamic-radio-button';
import DynamicCheckboxButton from './form/dynamic-checbox-button';
import InputField from './form/input-field';

interface IProps extends DispatchProps {
  patientTemplate: PatientTemplateUI | undefined;
  template: TemplateUI;
  patientId: number;
  actorId: number;
}

interface IState {
  defaultPatientTemplate: PatientTemplateUI;
}

class PatientTemplateForm extends React.Component<IProps, IState> {

  constructor(props: IProps) {
    super(props);
    this.state = {
      defaultPatientTemplate: PatientTemplateUI.getNew(props.patientId, props.actorId, props.template)
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
        return this.renderDynamicRadioButton(tfv, ['Daily', 'Weekly', 'Monthly'],
          fieldName, isMandatory);
      default:
        return this.renderInputField(tfv, fieldName, isMandatory);
    };
  };

  renderDynamicRadioButton = (tfv: TemplateFieldValueUI,
    options: ReadonlyArray<string>,
    fieldName: string,
    isMandatory: boolean) => (
      <DynamicRadioButton
        options={options}
        selectedOption={tfv.value}
        label={fieldName}
        key={tfv.localId}
        mandatory={isMandatory}
        onSelectChange={(value: string) => this.onTemplateFieldValueChange(tfv.localId, value)}
      />
    );

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
        key={tfv.localId}
        mandatory={isMandatory}
        onSelectChange={(value: string) => this.onTemplateFieldValueChange(tfv.localId, value)} />
    );
  };

  renderInputField = (tfv: TemplateFieldValueUI,
    fieldName: string,
    isMandatory: boolean) => (
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

  render = () => {
    const patientTemplate = this.getPatientTemplate();
    return (
      <div>
        <Form>
          {patientTemplate.templateFieldValues.map(tfv => this.renderField(tfv))}
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
