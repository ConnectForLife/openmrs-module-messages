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
import CalendarView from './calendar/calendar'
import { RouteComponentProps } from 'react-router';

class App extends React.Component<RouteComponentProps<{ patientId: string }>> {
    render() {
        return (
            <CalendarView patientId={parseInt(this.props.match.params.patientId, 10)}/>
        );
    }
}

export default connect()(App);
