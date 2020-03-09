/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import React from 'react';
import { connect } from 'react-redux';
import { IRootState } from '../../reducers';
import {
  getPersonStatus,
  closeModal,
  openModal,
  putPersonStatus,
  getPossibleReasons
} from '../../reducers/person-status.reducer'
import * as Msg from '../../shared/utils/messages';
import './person-status-notification.scss';

interface IPersonStatusNotificationProps extends DispatchProps, StateProps {
  patientUuid: string
}

interface IPersonStatusNotificationState {
}

class PersonStatusNotification extends React.PureComponent<IPersonStatusNotificationProps, IPersonStatusNotificationState> {

  constructor(props: IPersonStatusNotificationProps) {
    super(props);
  }

  componentDidMount() {
    const shouldFetchStatus = !this.props.personStatus.status.value && !this.props.personStatus.personStatusLoading;
    if (shouldFetchStatus) {
      this.props.getPersonStatus(this.props.patientUuid);
    }
  };

  renderDeactivatedStatusNotificationIfNeeded = () => {
    switch (this.props.personStatus.status.value) {
      case Msg.PERSON_STATUSES.DEACTIVATED.value:
        return this.renderNotification(Msg.DEACTIVATED_STATUS_NOTIFICATION);
      case Msg.PERSON_STATUSES.NO_CONSENT.value:
        return this.renderNotification(Msg.NO_CONSENT_STATUS_NOTIFICATION);
      case Msg.PERSON_STATUSES.MISSING_VALUE.value:
        return this.renderNotification(Msg.MISSING_VALUE_STATUS_NOTIFICATION);
      default:
        return null;
    }
  };

  renderNotification = (message: string) => {
    return (
      <div className="note-container">
        <div className="note danger">
          <div className="text">
            <span className="toast-item-image toast-item-image-alert" />
            <p>{message}</p>
          </div>
        </div>
      </div>
    );
  };

  render() {
    return (
      <div className="person-status-notification">
        {this.renderDeactivatedStatusNotificationIfNeeded()}
      </div>
    );
  };
}

const mapStateToProps = ({ personStatus }: IRootState) => ({
  personStatus
});

const mapDispatchToProps = ({
  getPersonStatus,
  closeModal,
  openModal,
  putPersonStatus,
  getPossibleReasons
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PersonStatusNotification);