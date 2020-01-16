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

interface IPersonStatusProps extends DispatchProps, StateProps {
    patientUuid: string
};

interface IPersonStatusState {
    statusLabels: Object
};

class PersonStatus extends React.PureComponent<IPersonStatusProps, IPersonStatusState> {

    constructor(props: IPersonStatusProps) {
        super(props);
        this.state = {
            statusLabels: {
                NO_CONSENT: Msg.PERSON_STATUS_NO_CONSENT,
                ACTIVE: Msg.PERSON_STATUS_ACTIVE,
                DEACTIVATE: Msg.PERSON_STATUS_DEACTIVATE,
                MISSING_VALUE: Msg.PERSON_STATUS_MISSING_VALUE
            }
        };
    }

    componentDidMount() {
        this.props.getPersonStatus(this.props.patientUuid);
    };

    getValueLabel = (key, set) => {
        return _.get(set, key, key);
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
        const { statusLabels } = this.state;
        return (
            <p>{Msg.PERSON_STATUS_LABEL + ' ' + this.getValueLabel(personStatus.status.value, statusLabels)}</p>
        );
    };

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
                    status={status} />
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
