import Header from '../patient-header/header';
import PatientStatusNotification from '../person-status-notification/person-status-notification';
import React from 'react';
import CalendarView from '../calendar/calendar';
import PatientTemplateEdit from '../patient-template/patient-template-edit';
import { RouteComponentProps } from 'react-router-dom';
import ManageMessages from '../patient-template/manage-messages';

interface IWrappedComponentProps extends RouteComponentProps<{ patientUuid: string }> {
  isNew?: boolean;
}

const withPatientHeader = (WrappedComponent) => {
  return class extends React.Component<IWrappedComponentProps> {
    render() {
      return (
          <div className="body-wrapper">
            <Header patientUuid={this.props.match.params.patientUuid} />
            <PatientStatusNotification />
            <div className="content">
              <WrappedComponent  {...this.props}/>
            </div>
          </div>
      )
    }
  }
}

export const CalendarWithHeader = withPatientHeader(CalendarView); 
export const PatientTemplateEditWithHeader = withPatientHeader(PatientTemplateEdit); 
export const ManageMessagesWithHeader = withPatientHeader(ManageMessages); 
