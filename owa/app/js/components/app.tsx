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

class App extends React.Component {
    render() {
        return (
            <div className="body-wrapper">
                <div className="row">
                    <div className="col-md-12 col-xs-12">
                        <h2>Messages</h2>
                    </div>
                </div>
                <div className="panel-body"></div>
            </div>
        );

    }
}

export default connect()(App);
