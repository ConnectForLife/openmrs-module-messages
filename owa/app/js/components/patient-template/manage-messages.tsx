import React from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import BestContactTime from './best-contact-time';
import ScheduledMessages from './scheduled-messages';
import { checkIfDefaultValuesUsed, generateDefaultPatientTemplates } from '../../reducers/patient-template.reducer';
import { IRootState } from '../../reducers';
import * as Msg from '../../shared/utils/messages';
import './patient-template.scss';

interface IManageMessagesProps extends DispatchProps, StateProps, RouteComponentProps<{ patientId: string, patientUuid: string }> {
};

interface IManageMessagesState {
};

class ManageMessages extends React.PureComponent<IManageMessagesProps, IManageMessagesState> {

  componentDidMount() {
    this.props.checkIfDefaultValuesUsed(parseInt(this.props.match.params.patientId, 10));
  }

  handleSave() {
    if (!!this.props.defaultValuesUsed) {
      this.props.generateDefaultPatientTemplates(parseInt(this.props.match.params.patientId, 10));
    }
  }

  private renderDefaultValuesNotificationIfNeeded() {
    if (this.props.defaultValuesUsed) {
      return (
        <div className="note-container">
          <div className="note warning">
            <div className="text">
              <span className="toast-item-image toast-item-image-alert" />
              <p>{Msg.DEFAULT_VALUES_USED_MESSAGE}</p>
            </div>
          </div>
        </div>
      );
    } else return null;
  }

  render() {
    const { patientId, patientUuid } = this.props.match.params;

    return (
      <>
        <h2>Manage messages</h2>
        {this.renderDefaultValuesNotificationIfNeeded()}
        <div className="panel-body">
          <BestContactTime
            patientId={parseInt(patientId)}
            patientUuid={patientUuid}
            onSaveClickCallback={() => this.handleSave()}
          />
          <ScheduledMessages
            patientId={patientId}
            patientUuid={patientUuid} />
        </div>
      </>
    );
  }
}

const mapStateToProps = ({ patientTemplate }: IRootState) => ({
  defaultValuesUsed: patientTemplate.defaultValuesUsed
});

const mapDispatchToProps = ({
  checkIfDefaultValuesUsed,
  generateDefaultPatientTemplates
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ManageMessages);
