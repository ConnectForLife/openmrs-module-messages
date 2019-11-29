import React from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import _ from 'lodash';

interface IPatientTemplateEditProps extends RouteComponentProps<{ patientTemplateId: string }> {
  patientId?: string
};

interface IPatientTemplateEditState {
  isNew: boolean;
};

export class PatientTemplateEdit extends React.PureComponent<IPatientTemplateEditProps, IPatientTemplateEditState> {

  constructor(props) {
    super(props);
    this.state = {
      isNew: !this.props.match.params || !this.props.match.params.patientTemplateId,
    };
  }

  componentDidMount = () => {
    //remove if not needed
  }

  componentWillUpdate = (nextProps: IPatientTemplateEditProps, nextState: IPatientTemplateEditState) => {
    //remove if not needed
  }

  renderTemplateState = () => {
    if (this.state.isNew) {
      return (
        <div>
          The patient template is new
          for patient (patientId: {this.props.patientId})
        </div>
      );
    } else {
      return (
        <div>
          The patient template is not new
          (id: {this.props.match.params.patientTemplateId})
        </div>
      );
    }
  }

  render() {
    const { isNew } = this.state;
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

// export const mapStateToProps = state => ({
// });

// const mapDispatchToProps = ({
// });

// type StateProps = ReturnType<typeof mapStateToProps>;
// type DispatchProps = typeof mapDispatchToProps;

// export default connect(
//   mapStateToProps,
//   mapDispatchToProps
// )(PatientTemplateEdit);
