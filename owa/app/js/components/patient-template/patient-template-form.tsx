import React from 'react';
import { connect } from 'react-redux';
import { updatePatientTemplate } from '../../reducers/patient-template.reducer';
import { Form, FormGroup, ControlLabel, FormControl } from 'react-bootstrap';
import { PatientTemplateUI } from '../../shared/model/patient-template-ui';
import { TemplateUI } from '../../shared/model/template-ui';
import { TemplateFieldValueUI } from '../../shared/model/template-field-value-ui';
import { TemplateFieldType } from '../../shared/model/template-field-type';
import DynamicRadioButton from './form/dynamic-radio-button';

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
    switch (fieldType) {
      case TemplateFieldType.SERVICE_TYPE:
        return this.renderDynamicRadioButton(tfv, ['Call', 'SMS', 'Deactivate service'], fieldName);
      case TemplateFieldType.DAY_OF_WEEK:
        return this.renderDynamicRadioButton(tfv, ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'], fieldName);
      case TemplateFieldType.MESSAGING_FREQUENCY:
        return this.renderDynamicRadioButton(tfv, ['Daily', 'Weekly', 'Monthly'], fieldName);
      default:
        return this.renderInputField(tfv, fieldName);
    };
  };

  renderDynamicRadioButton = (tfv: TemplateFieldValueUI, options: ReadonlyArray<string>, fieldName: string) => (
      <DynamicRadioButton
        options={options}
        selectedOption={tfv.value}
        label={fieldName}
        key={tfv.localId}
        onSelectChange={(value: string) => this.onTemplateFieldValueChange(tfv.localId, value)}
      />
  )

  renderInputField = (tfv: TemplateFieldValueUI, fieldName: string) => (
    <FormGroup controlId={tfv.localId} key={tfv.localId}>
      <ControlLabel>{fieldName}</ControlLabel>
      <FormControl
        type="text"
        name={fieldName}
        value={tfv.value}
        onChange={e => this.onTemplateFieldValueChange(tfv.localId, e.target.value)}
        className="form-control" />
    </FormGroup>
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
