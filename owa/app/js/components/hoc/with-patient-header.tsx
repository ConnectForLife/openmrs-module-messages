import Header from '../patient-header/header';
import PatientStatusNotification from '../person-status-notification/person-status-notification';
import React from 'react';
import CalendarView from '../calendar/calendar';
import PatientTemplateEdit from '../patient-template/patient-template-edit';
import {RouteComponentProps} from 'react-router-dom';
import ManageMessages from '../patient-template/manage-messages';
import {DashboardType} from "../../shared/model/dashboard-type";

interface IWrappedComponentProps extends RouteComponentProps<{ patientUuid: string, dashboardType?: DashboardType }> {
  isNew?: boolean;
}

const withPatientHeader = (WrappedComponent) => {
  return class extends React.Component<IWrappedComponentProps> {
    render() {
      const newProps = {
        ...this.props,
        patientUuid: this.props.match.params.patientUuid,
        dashboardType: this.props.match.params.dashboardType
      };
      return (
        <div className="body-wrapper">
          <Header patientUuid={newProps.patientUuid} dashboardType={newProps.dashboardType} />
          <PatientStatusNotification patientUuid={newProps.patientUuid}/>
          <div className="content">
            <WrappedComponent  {...newProps}/>
          </div>
        </div>
      )
    }
  }
};

export const CalendarWithHeader = withPatientHeader(CalendarView);
export const PatientTemplateEditWithHeader = withPatientHeader(PatientTemplateEdit);
export const ManageMessagesWithHeader = withPatientHeader(ManageMessages); 
