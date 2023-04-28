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
import { IRootState } from '../../reducers';
import {
  getPersonStatus,
  closeModal,
  openModal,
  putPersonStatus,
  getPossibleReasons
} from '../person-status/person-status.reducer';
import * as Default from '../../shared/utils/messages';
import './person-status-notification.scss';
import { getPersonStatusConfig } from '../../shared/utils/person-status'
import { PropsWithIntl } from '../../components/translation/PropsWithIntl';
import { injectIntl } from 'react-intl';

interface IPersonStatusNotificationProps extends DispatchProps, StateProps {
  patientUuid: string
}

interface IPersonStatusNotificationState {
}

class PersonStatusNotification extends React.PureComponent<PropsWithIntl<IPersonStatusNotificationProps>, IPersonStatusNotificationState> {

  constructor(props: PropsWithIntl<IPersonStatusNotificationProps>) {
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
      case getPersonStatusConfig(this.props.intl).DEACTIVATED.value:
        return this.renderNotification(
          this.props.intl.formatMessage({ id: 'messages.deactivatedStatusNotification' }));
      case getPersonStatusConfig(this.props.intl).NO_CONSENT.value:
        return this.renderNotification(
          this.props.intl.formatMessage({ id: 'messages.noConsentStatusNotification' }));
      case getPersonStatusConfig(this.props.intl).MISSING_VALUE.value:
        return this.renderNotification(
          this.props.intl.formatMessage({ id: 'messages.missingValueStatusNotification' })
        );
      default:
        return null;
    }
  };

  renderNotification = (message: string) => {
    return (
      <div className="note-container">
        <div className="note danger">
          <div className="note-content">
            <span className="toast-item-image toast-item-image-alert" />
            <div className="message">{message}</div>
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

export default injectIntl(connect(
  mapStateToProps,
  mapDispatchToProps
)(PersonStatusNotification));
