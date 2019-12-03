import React from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { getTemplates, getPatientTemplates } from '../../reducers/patient-template.reducer'
import { IRootState } from '../../reducers';

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
      </div>
    );
  }
}

const mapStateToProps = ({ patientTemplate }: IRootState) => ({
});

const mapDispatchToProps = ({
  getTemplates,
  getPatientTemplates
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PatientTemplateEdit);
