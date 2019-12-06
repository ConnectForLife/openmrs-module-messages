import React from 'react'
import { Route, Switch } from 'react-router-dom';
import PatientTemplateEdit from './patient-template-edit';
import ManageMessages from './manage-messages';

const Routes = (props) => (
  <>
    <Switch>
      <Route
        path={'/messages/:patientId/patient-template/new'}
        render={(props) => <PatientTemplateEdit {...props} isNew={true} />}
      />
      <Route
        path={'/messages/:patientId/patient-template/edit'}
        render={(props) => <PatientTemplateEdit {...props} isNew={false} />}
      />
      <Route
        path={'/messages/:patientId/patient-template'}
        render={(props) => <ManageMessages {...props} />}
      />
    </Switch>
  </>
);

export default Routes;
