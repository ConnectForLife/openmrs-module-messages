import PatientStatusNotification from '../person-status-notification/person-status-notification';
import React from 'react';
import CalendarView from '../calendar/calendar';
import PatientTemplateEdit from '../patient-template/patient-template-edit';
import {RouteComponentProps} from 'react-router-dom';
import ManageMessages from '../patient-template/manage-messages';
import {DashboardType} from "../../shared/model/dashboard-type";
import PersonStatus from '@bit/soldevelo-omrs.cfl-components.person-status';
import Header from '@bit/soldevelo-omrs.cfl-components.person-header';

interface IWrappedComponentProps extends RouteComponentProps<{ patientUuid: string, dashboardType?: DashboardType }> {
  isNew?: boolean;
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
            <PersonStatus patientUuid={newProps.patientUuid}/>
          </Header>
          <PatientStatusNotification patientUuid={newProps.patientUuid}/>
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
