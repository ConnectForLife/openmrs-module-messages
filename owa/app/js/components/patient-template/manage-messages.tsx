import React from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import './patient-template.scss';
import BestContactTime from './best-contact-time';
import ScheduledMessages from './scheduled-messages';

interface IManageMessagesProps extends DispatchProps, StateProps, RouteComponentProps<{ patientId: string, patientUuid: string }> {
};

interface IManageMessagesState {
};

class ManageMessages extends React.PureComponent<IManageMessagesProps, IManageMessagesState> {
  render() {
    const { patientId, patientUuid } = this.props.match.params;

    return (
      <div className="body-wrapper">
        <h2>Manage messages</h2>
        <div className="panel-body">
          <BestContactTime patientId={parseInt(patientId)} patientUuid={patientUuid} />
          <ScheduledMessages patientId={patientId} patientUuid={patientUuid} />
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({}) => ({
});

const mapDispatchToProps = ({
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ManageMessages);
