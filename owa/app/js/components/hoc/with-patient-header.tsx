/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import PatientStatusNotification from '../person-status-notification/person-status-notification';
import React from 'react';
import CalendarView from '../calendar/calendar';
import PatientTemplateEdit from '../patient-template/patient-template-edit';
import {RouteComponentProps} from 'react-router-dom';
import ManageMessages from '../patient-template/manage-messages';
import {DashboardType} from "../../shared/model/dashboard-type";
import PersonStatus from '../person-status/person-status';
import Header from '../person-header/person-header';

interface IWrappedComponentProps extends RouteComponentProps<{ patientUuid: string, dashboardType?: DashboardType }> {
  isNew?: boolean;
  locale?: string;
}

const withPatientHeader = (WrappedComponent) => {
  return class extends React.Component<IWrappedComponentProps> {

    getBaseUrl = () => {
      const path = window.location.pathname;
      return path.substring(0, path.indexOf('/owa/')) + '/';
    }

    render() {
      const newProps = {
        ...this.props,
        patientUuid: this.props.match.params.patientUuid,
        dashboardType: this.props.match.params.dashboardType,
        redirectUrl: this.getBaseUrl() + "coreapps/clinicianfacing/patient.page?patientId="
          + this.props.match.params.patientUuid,
        displayTelephone: true
      };
      return (
        <div className="body-wrapper">
          <Header {...newProps}>
            <PersonStatus {...newProps}/>
          </Header>
          <PatientStatusNotification {...newProps}/>
          <div className="content">
            <WrappedComponent {...newProps}/>
          </div>
        </div>
      )
    }
  }
};

export const CalendarWithHeader = withPatientHeader(CalendarView);
export const PatientTemplateEditWithHeader = withPatientHeader(PatientTemplateEdit);
export const ManageMessagesWithHeader = withPatientHeader(ManageMessages);
