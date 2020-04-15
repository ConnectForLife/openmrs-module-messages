import React from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import BestContactTime from './best-contact-time';
import ScheduledMessages from './scheduled-messages';
import { checkIfDefaultValuesUsed, generateDefaultPatientTemplates } from '../../reducers/patient-template.reducer';
import { IRootState } from '../../reducers';
import * as Msg from '../../shared/utils/messages';
import './patient-template.scss';
import Timezone from "../timezone/timezone";
import {DashboardType} from "../../shared/model/dashboard-type";

interface IManageMessagesProps extends DispatchProps, StateProps, RouteComponentProps<{ patientId: string, patientUuid: string, dashboardType: DashboardType }> {
};

interface IManageMessagesState {
};

class ManageMessages extends React.PureComponent<IManageMessagesProps, IManageMessagesState> {

  componentDidMount() {
    if (this.isPatient()) {
      this.props.checkIfDefaultValuesUsed(parseInt(this.props.match.params.patientId, 10));
    } else {
    }
  }

  handleSave() {
    if (!!this.props.defaultValuesState.defaultValuesUsed) {
      this.props.generateDefaultPatientTemplates(parseInt(this.props.match.params.patientId, 10));
    }
  }

  private isPatient() {
    const dashboardType = this.props.match.params.dashboardType;
    return !!dashboardType ? dashboardType.toUpperCase() === DashboardType.PATIENT : true;
  }

  private renderDefaultValuesNotificationIfNeeded() {
    if (this.props.defaultValuesState.defaultValuesUsed) {
      return (
        <div className="note-container">
          <div className="note warning">
            <div className="note-content">
              <span className="toast-item-image toast-item-image-alert" />
              <div className="message">{this.getDefaultValuesMessage()}</div>
            </div>
          </div>
        </div>
      );
    } else return null;
  }

  private getDefaultValuesMessage(): string {
    return this.props.defaultValuesState.allValuesDefault ?
      Msg.ALL_DEFAULT_VALUES_USED_MESSAGE : Msg.SOME_DEFAULT_VALUES_USED_MESSAGE;
  }

  render() {
    const { patientId, patientUuid, dashboardType } = this.props.match.params;
    return (
      <>
        <h2>Manage messages</h2>
        {this.renderDefaultValuesNotificationIfNeeded()}
        <Timezone />
        <div className="panel-body">
          <BestContactTime
            patientId={parseInt(patientId)}
            patientUuid={patientUuid}
            onSaveClickCallback={() => this.handleSave()}
            dashboardType={this.props.match.params.dashboardType}
          />
          <ScheduledMessages
            patientId={patientId}
            patientUuid={patientUuid}
            dashboardType={dashboardType}
            isPatient={this.isPatient()} />
        </div>
      </>
    );
  }
}

const mapStateToProps = ({ patientTemplate }: IRootState) => ({
  defaultValuesState: patientTemplate.defaultValuesState
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
