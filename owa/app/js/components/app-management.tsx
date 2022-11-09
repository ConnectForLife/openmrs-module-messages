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

import { IRootState } from '../reducers';
import { getConfig, saveConfig } from '../reducers/admin-settings.reducer';
import * as Default from '../shared/utils/messages';
import BestContactTime from './default-settings/default-best-contact-time';
import AdminSettings from './admin-settings/admin-settings';
import { RouteComponentProps } from 'react-router-dom';
import Timezone from './timezone/timezone';
import { LocalizedMessage } from '@openmrs/react-components';

interface IProps extends StateProps, DispatchProps, RouteComponentProps<{
    activeSection?: string
}> { locale?: string }

class AppManagement extends React.Component<IProps> {

    componentDidMount = () => this.props.getConfig();

    handleSave = () => this.props.saveConfig(this.props.templates, this.props.defaultBestContactTimes, this.props.locale);

    render = () =>
        <div className="body-wrapper">
            <div className="content">
                <Timezone />
                <h2><LocalizedMessage id="MESSAGES_DEFAULT_SETTINGS" defaultMessage={Default.DEFAULT_SETTINGS} /></h2>
                <BestContactTime
                    loading={this.props.loading}
                    bestContactTimes={this.props.defaultBestContactTimes}
                    actorTypes={this.props.actorTypes} />
                <AdminSettings templates={this.props.templates}
                    activeSection={this.props.match.params.activeSection}
                    onSaveCallback={this.handleSave} />
            </div>
        </div>
}

const mapStateToProps = ({ adminSettings }: IRootState) => ({
    templates: adminSettings.defaultTemplates,
    loading: adminSettings.loading,
    actorTypes: adminSettings.actorTypes,
    defaultBestContactTimes: adminSettings.defaultBestContactTimes
});

const mapDispatchToProps = ({
    getConfig,
    saveConfig
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(AppManagement);
