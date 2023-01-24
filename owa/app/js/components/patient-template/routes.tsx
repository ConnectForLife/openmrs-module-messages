/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import React from 'react'
import { Route, Switch } from 'react-router-dom';
import {
  PatientTemplateEditWithHeader as PatientTemplateEdit,
  ManageMessagesWithHeader as ManageMessages 
} from '../hoc/with-patient-header';
import {withLocalization} from '@openmrs/react-components';

const LocalizedPatientTemplateEdit = withLocalization(PatientTemplateEdit);
const LocalizedManageMessages = withLocalization(ManageMessages);

const Routes = (props) => (
  <>
    <Switch>
      <Route
        path={'/messages/:dashboardType/:patientId&patientuuid=:patientUuid/patient-template/new'}
        render={(props) => <LocalizedPatientTemplateEdit {...props} isNew={true} />}
      />
      <Route
        path={'/messages/:dashboardType/:patientId&patientuuid=:patientUuid/patient-template/edit/:activeSection'}
        render={(props) => <LocalizedPatientTemplateEdit {...props} isNew={false} />}
      />
      <Route
        path={'/messages/:dashboardType/:patientId&patientuuid=:patientUuid/patient-template'}
        render={(props) => <LocalizedManageMessages {...props} />}
      />
    </Switch>
  </>
);

export default Routes;
