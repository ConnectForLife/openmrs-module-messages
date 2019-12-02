import React from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { RouteQueryParams } from '../../shared/model/RouteQueryParams';

interface IPatientTemplateEditProps extends RouteComponentProps<{ patientId: string }> {
  isNew: boolean
};

interface IPatientTemplateEditState {
};

export class PatientTemplateEdit extends React.PureComponent<IPatientTemplateEditProps, IPatientTemplateEditState> {

  constructor(props) {
    super(props);
  }

  componentDidMount = () => {
    //remove if not needed
  }

  componentWillUpdate = (nextProps: IPatientTemplateEditProps, nextState: IPatientTemplateEditState) => {
    //remove if not needed
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
