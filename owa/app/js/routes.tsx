/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
import React from 'react'
import { Route, Switch } from 'react-router-dom';
import App from './components/app'
import AppManagement from './components/app-management';

export default (store) => {
    // combine store and onEnter if you need to fire an action when going to a route. Example:
    //   onEnter={ (nextState) => {store.dispatch(loadPatientAction(nextState.params.patientUuid)} }

    return (
        <Switch>
            <Route path="/messages/manage" component={AppManagement} />
            <Route path="/messages" component={App} />
        </Switch>
    );
}
