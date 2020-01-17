/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import React from 'react';
import DefaultSettingsTable from './default-settings/default-settings-table';
import { connect } from 'react-redux';
import { Button } from 'react-bootstrap';

import { IRootState } from '../reducers';
import { getConfig, saveConfig } from '../reducers/admin-settings.reducer';
import * as Msg from '../shared/utils/messages';
import BestContactTime from './default-settings/default-best-contact-time';

interface IProps extends StateProps, DispatchProps { }

class AppManagement extends React.Component<IProps> {

    componentDidMount = () => this.props.getConfig();

    handleSave = () => this.props.saveConfig(this.props.templates, this.props.defaultBestContactTimes);

    render = () =>
        <div className="body-wrapper">
            <div className="content">
                <h3>{Msg.DEFAULT_SETTINGS}</h3>
                <BestContactTime
                    loading={this.props.loading}
                    bestContactTimes={this.props.defaultBestContactTimes}
                    actorTypes={this.props.actorTypes} />
                <DefaultSettingsTable templates={this.props.templates} />
                <div className="flex-justify-end u-mt-8">
                    <Button
                        className="btn btn-success btn-md"
                        onClick={this.handleSave}>
                        {Msg.SAVE_BUTTON_LABEL}
                    </Button>
                </div>
            </div>
        </div>
};

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
