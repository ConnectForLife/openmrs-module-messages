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
import { Header } from '@openmrs/react-components';
import AppManagement from './components/app-management';
import PatientTemplate from './components/patient-template/routes';
import BreadCrumb from './components/bread-crumb';
import { CalendarWithHeader as Calendar } from './components/hoc/with-patient-header';

export default (store) => (
    <div>
        <Header />
        <BreadCrumb />
        <Switch>
            <Route path="/messages/:dashboardType/:patientId&patientuuid=:patientUuid/patient-template" component={PatientTemplate} />
            <Route path="/messages/manage/:activeSection" component={AppManagement} />
            <Route path="/messages/manage" component={AppManagement} />
            <Route path="/messages/:dashboardType/:patientId&patientuuid=:patientUuid" component={Calendar} />
        </Switch>
    </div>
);

