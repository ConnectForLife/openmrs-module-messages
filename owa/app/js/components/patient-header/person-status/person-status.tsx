/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import React from 'react';
import _ from 'lodash';
import { connect } from 'react-redux';
import { IRootState } from '../../../reducers';
import {
    getPersonStatus,
    closeModal,
    openModal,
    putPersonStatus,
    getPossibleReasons
} from '../../../reducers/person-status.reducer'
import './person-status.scss';
import * as Msg from '../../../shared/utils/messages';
import ChangeStatusModal from './modal/change-status-modal';
import { PersonStatusUI } from '../../../shared/model/person-status.model-ui';

interface IPersonStatusProps extends DispatchProps, StateProps {
    patientUuid: string
}

interface IPersonStatusState {
}

class PersonStatus extends React.PureComponent<IPersonStatusProps, IPersonStatusState> {

    constructor(props: IPersonStatusProps) {
        super(props);
    }

    componentDidMount() {
        this.props.getPersonStatus(this.props.patientUuid);
        this.props.getPossibleReasons();
    };

    getStatusLabel = (key, set) => {
        return _.get(set, `${key}.label`, key);
    };

    handleClose = () => {
        this.props.closeModal();
    };

    handleConfirm = (value: string, reason?: string) => {
        const status = _.cloneDeep(this.props.personStatus.status);
        status.value = value;
        status.reason = reason;
        this.props.putPersonStatus(status);
        this.props.closeModal();
    };

    handleChangeStatus = (event) => {
        this.props.openModal(event.target.id);
    };

    renderStatus = () => {
        const { personStatus } = this.props;
        return (
            <p>{Msg.PERSON_STATUS_LABEL + ' ' + this.getStatusLabel(personStatus.status.value, Msg.PERSON_STATUSES)}</p>
        );
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
    }

    prepereStatusForModal = (status: PersonStatusUI): PersonStatusUI => {
        const modalStatus = _.cloneDeep(status);
        modalStatus.value = this.getInitialValue(modalStatus);
        return modalStatus;
    };

    getInitialValue = (status: PersonStatusUI) =>  (!!status.value &&
        status.value != Msg.MISSING_VALUE_KEY && status.value != Msg.NO_CONSENT_KEY) ? status.value : Msg.ACTIVATED_KEY;

    render() {
        const { status, showModal, personStatusLoading, submitDisabled, possibleResults } = this.props.personStatus;
        const statusStyle = status.styleObject || {};
        return (
            <>
                <ChangeStatusModal
                    cancel={this.handleClose}
                    confirm={this.handleConfirm}
                    show={showModal}
                    submitDisabled={submitDisabled}
                    status={this.prepereStatusForModal(status)} 
                    possibleResults={possibleResults} />
                <div className="person-status" style={statusStyle} onClick={this.handleChangeStatus}>
                    {!personStatusLoading && this.renderStatus()}
                </div>
                {this.renderDeactivatedStatusNotificationIfNeeded()}
            </>
        );
    };
};

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
)(PersonStatus);
