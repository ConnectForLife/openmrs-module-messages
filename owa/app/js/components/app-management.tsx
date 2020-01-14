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
import { Button } from 'react-bootstrap';
import { connect } from 'react-redux';

import { IRootState } from '../reducers';
import { getTemplates, putTemplates } from '../reducers/admin-settings.reducer';
import * as Msg from '../shared/utils/messages';

interface IProps extends StateProps, DispatchProps { }

class AppManagement extends React.Component<IProps> {

    componentDidMount = () => this.props.getTemplates();

    handleSave = () => this.props.putTemplates(this.props.templates);
    
    render() {
        return (
            <div className="body-wrapper">
                <div className="content">
                    <h1>Hello, world - Management</h1>
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
        );
    };
};

const mapStateToProps = ({ adminSettings }: IRootState) => ({
    templates: adminSettings.defaultTemplates,
    loading: adminSettings.loading
});

const mapDispatchToProps = ({
    getTemplates,
    putTemplates
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(AppManagement);
