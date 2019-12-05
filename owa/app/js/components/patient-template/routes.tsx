import React from 'react'
import { Route, Switch } from 'react-router-dom';
import PatientTemplateEdit from './patient-template-edit';
import ManageMessages from './manage-messages';

const Routes = (props) => (
  <>
    <Switch>
      <Route
        path={`${props.match.url}/:patientId/new`}
        render={(props) => <PatientTemplateEdit {...props} isNew={true} />}
      />
      <Route
        path={`${props.match.url}/:patientId/edit`}
        render={(props) => <PatientTemplateEdit {...props} isNew={false} />}
      />
      <Route
        path={`${props.match.url}/:patientId`}
        render={(props) => <ManageMessages {...props} />}
      />
    </Switch>
  </>
);

export default Routes;
