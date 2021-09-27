import React from 'react'
import { Route, Switch } from 'react-router-dom';
import {
  PatientTemplateEditWithHeader as PatientTemplateEdit,
  ManageMessagesWithHeader as ManageMessages 
} from '../hoc/with-patient-header';

const Routes = (props) => (
  <>
    <Switch>
      <Route
        path={'/messages/:dashboardType/:patientId&patientuuid=:patientUuid/patient-template/new'}
        render={(props) => <PatientTemplateEdit {...props} isNew={true} />}
      />
      <Route
        path={'/messages/:dashboardType/:patientId&patientuuid=:patientUuid/patient-template/edit/:activeSection'}
        render={(props) => <PatientTemplateEdit {...props} isNew={false} />}
      />
      <Route
        path={'/messages/:dashboardType/:patientId&patientuuid=:patientUuid/patient-template'}
        render={(props) => <ManageMessages {...props} />}
      />
    </Switch>
  </>
);

export default Routes;
