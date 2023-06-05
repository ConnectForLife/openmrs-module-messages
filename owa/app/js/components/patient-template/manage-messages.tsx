/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import React from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import BestContactTime from './best-contact-time';
import ScheduledMessages from './scheduled-messages';
import { checkIfDefaultValuesUsed, generateDefaultPatientTemplates } from '../../reducers/patient-template.reducer';
import { IRootState } from '../../reducers';
import './patient-template.scss';
import Timezone from '../timezone/timezone';
import { DashboardType } from '../../shared/model/dashboard-type';
import { injectIntl, FormattedMessage } from 'react-intl';
import { PropsWithIntl } from '../../components/translation/PropsWithIntl';

interface IManageMessagesProps extends DispatchProps, StateProps, RouteComponentProps<{ patientId: string, patientUuid: string, dashboardType: DashboardType }> {
  
};

interface IManageMessagesState {
};

class ManageMessages extends React.PureComponent<PropsWithIntl<IManageMessagesProps>, IManageMessagesState> {

  componentDidMount() {
    if (this.isPatient()) {
      this.props.checkIfDefaultValuesUsed(parseInt(this.props.match.params.patientId, 10));
    } else {
    }
  }

  handleSave() {
    if (!!this.props.defaultValuesState.defaultValuesUsed) {
      this.props.generateDefaultPatientTemplates(parseInt(this.props.match.params.patientId, 10), this.props.intl);
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
      this.props.intl.formatMessage({ id: 'messages.allDefaultValuesUsedMessage' }) :
      this.props.intl.formatMessage({ id: 'messages.someDefaultValuesUsedMessage' });
  }

  render() {
    const { patientId, patientUuid, dashboardType } = this.props.match.params;
    return (
      <>
        <h2><FormattedMessage id="messages.manageMessagesLabel" /></h2>
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

export default injectIntl(connect(
  mapStateToProps,
  mapDispatchToProps
)(ManageMessages));
