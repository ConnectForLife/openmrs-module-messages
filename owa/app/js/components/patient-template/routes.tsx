import React from 'react'
import { Route, Switch } from 'react-router-dom';
import { PatientTemplateEdit } from './patient-template-edit';
import queryString from 'query-string';

function extractQueryParam(query: string, paramName: string) {
  //TODO: move to util or to external repository
  try {
    const parsed = queryString.parse(query);
    return !!parsed ? parsed[paramName]![0] : '';
  } catch (ex) {
    console.error('Cannot parse query params', ex);
    return '';
  }
}

const Routes = (props) => (
  <>
    <Switch>
      <Route path={`${props.match.url}/new`}
        render={(props) =>
          <PatientTemplateEdit {...props}
            patientId={extractQueryParam(props.location.search, 'patientId')} />} />
      <Route path={`${props.match.url}/edit/:patientTemplateId`} component={PatientTemplateEdit} />
    </Switch>
  </>
);

export default Routes;

