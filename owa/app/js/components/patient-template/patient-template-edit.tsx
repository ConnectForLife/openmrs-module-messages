import React from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { 
  getTemplates,
  getPatientTemplates,
  putPatientTemplates
} from '../../reducers/patient-template.reducer'
import { IRootState } from '../../reducers';
import { Button } from 'react-bootstrap';
import * as Msg from '../../shared/utils/messages';

interface IPatientTemplateEditProps extends DispatchProps, StateProps, RouteComponentProps<{ patientId: string }> {
  isNew: boolean
};

interface IPatientTemplateEditState {
};

class PatientTemplateEdit extends React.PureComponent<IPatientTemplateEditProps, IPatientTemplateEditState> {

  constructor(props: IPatientTemplateEditProps) {
    super(props);
  }

  componentDidMount() {
    this.props.getTemplates();
    this.props.getPatientTemplates(parseInt(this.props.match.params.patientId));
  }

  componentWillUpdate(nextProps: IPatientTemplateEditProps, nextState: IPatientTemplateEditState) {
  }

  handleSave = () => {
    //TODO: CFLM-304: Add validation .then .catch
    this.props.putPatientTemplates(this.props.patientTemplates);
  }

  renderTemplateState = () => {
    if (this.props.isNew) {
      return (
        <div>
          The patient template is new
          for patient (patientId: {this.props.match.params.patientId})
        </div>
      );
    } else {
      return (
        <div>
          The patient template is not new
          (patientId: {this.props.match.params.patientId})
        </div>
      );
    }
  }

  render() {
    const { isNew } = this.props;
    return (
      <div className="body-wrapper">
        <div className="panel-body">
          <h2>Patient Template Edit page</h2>
          {this.renderTemplateState()}
        </div>
        <div className="panel-body">
          <Button
            className="btn btn-success btn-md pull-right"
            onClick={this.handleSave}>
            {Msg.SAVE_BUTTON_LABEL}
          </Button>
        </div>
      </div>
    );
  }
}

export const mapStateToProps = ({ patientTemplate }: IRootState) => (patientTemplate);

const mapDispatchToProps = ({
  getTemplates,
  getPatientTemplates,
  putPatientTemplates
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PatientTemplateEdit);
