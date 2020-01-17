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
    putPersonStatus
} from '../../../reducers/person-status.reducer'
import './person-status.scss';
import * as Msg from '../../../shared/utils/messages';
import ChangeStatusModal from './modal/change-status-modal';
import { PersonStatusUI } from '../../../shared/model/person-status.model-ui';

interface IPersonStatusProps extends DispatchProps, StateProps {
    patientUuid: string
};

interface IPersonStatusState {
};

class PersonStatus extends React.PureComponent<IPersonStatusProps, IPersonStatusState> {

    constructor(props: IPersonStatusProps) {
        super(props);
    }

    componentDidMount() {
        this.props.getPersonStatus(this.props.patientUuid);
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

    prepereStatusForModal = (status: PersonStatusUI): PersonStatusUI => {
        const modalStatus = _.cloneDeep(status);
        modalStatus.value = this.getInitialValue(modalStatus);
        return modalStatus;
    }

    getInitialValue = (status: PersonStatusUI) =>  (!!status.value &&
        status.value != Msg.MISSING_VALUE_KEY && status.value != Msg.NO_CONSENT_KEY) ? status.value : Msg.ACTIVATED_KEY;

    render() {
        const { status, showModal, personStatusLoading, submitDisabled } = this.props.personStatus;
        const statusStyle = status.styleObject || {};
        return (
            <>
                <ChangeStatusModal
                    cancel={this.handleClose}
                    confirm={this.handleConfirm}
                    show={showModal}
                    submitDisabled={submitDisabled}
                    status={this.prepereStatusForModal(status)} />
                <div className="person-status" style={statusStyle} onClick={this.handleChangeStatus}>
                    {!personStatusLoading && this.renderStatus()}
                </div>
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
    putPersonStatus
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(PersonStatus);