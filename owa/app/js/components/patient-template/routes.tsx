import React from 'react'
import { Route, Switch } from 'react-router-dom';
import PatientTemplateEdit from './patient-template-edit';

const Routes = (props) => (
  <>
    <Switch>
      <Route
        path={`${props.match.url}/new/:patientId`}
        render={(props) => <PatientTemplateEdit {...props} isNew={true} />}
      />
      <Route
        path={`${props.match.url}/edit/:patientId`}
        render={(props) => <PatientTemplateEdit {...props} isNew={false} />}
      />
    </Switch>
  </>
);

export default Routes;
