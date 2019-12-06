import React from 'react';
import { connect } from 'react-redux';
import { updatePatientTemplate } from '../../reducers/patient-template.reducer';
import { Form, FormGroup, ControlLabel, FormControl } from 'react-bootstrap';
import { PatientTemplateUI } from '../../shared/model/patient-template-ui';
import { TemplateUI } from '../../shared/model/template-ui';
import { TemplateFieldValueUI } from '../../shared/model/template-field-value-ui';

interface IProps extends DispatchProps { 
  patientTemplate: PatientTemplateUI | undefined;
  template: TemplateUI;
}

interface IState {
  defaultPatientTemplate: PatientTemplateUI;
}

class PatientTemplateForm extends React.Component<IProps, IState> {

  constructor(props: IProps) {
    super(props);
    this.state = {
      defaultPatientTemplate: PatientTemplateUI.getNew(props.template)
    }
  }

  getPatientTemplate = () => this.props.patientTemplate || this.state.defaultPatientTemplate;

  onTemplateFieldValueChange = (templateFieldLocalId: string, value: string) => {
    const patientTemplate = this.getPatientTemplate();
    const tfvToUpdate = patientTemplate.templateFieldValues.find(tfv => tfv.localId === templateFieldLocalId)!;
    tfvToUpdate.value = value;
    this.props.updatePatientTemplate(patientTemplate, [this.props.template]);
  };

  renderField = (tfv: TemplateFieldValueUI) => (
    <FormGroup controlId={tfv.localId} key={tfv.localId}>
      <ControlLabel>{tfv.getFieldName(this.props.template)}</ControlLabel>
      <FormControl 
        type="text"
        name={tfv.getFieldName(this.props.template)}
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
