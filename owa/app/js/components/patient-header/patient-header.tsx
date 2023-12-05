/*
 *  This Source Code Form is subject to the terms of the Mozilla Public License,
 *  v. 2.0. If a copy of the MPL was not distributed with this file, You can
 *  obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 *  the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *  <p>
 *  Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 *  graphic logo is a trademark of OpenMRS Inc.
 */

import React from 'react';
import { connect } from 'react-redux';
import { getPatient } from '../../reducers/patient.reducer';
import { getPerson } from '../../reducers/person.reducer';
import { getPatientHeaderFieldValue } from './patient-header-util';
import './patient-header.scss';
import { getAppById } from '../../reducers/apps';
import { searchLocations } from '../../reducers/location';

export enum DashboardType {
  PATIENT = 'PATIENT',
  PERSON = 'PERSON',
}

interface IPatientHeaderProps extends DispatchProps, StateProps {
  patientUuid: string;
  dashboardType: string;
  headerAppConfig: any;
  intl: any;
}

interface IPatientHeaderState {
};

class PatientHeader extends React.Component<IPatientHeaderProps, IPatientHeaderState> {
  constructor(props: IPatientHeaderProps) {
    super(props);
  }

  componentDidMount = () => {
    this.props.getAppById("cfl.configurablePatientHeader");
    this.props.searchLocations();

    if (this.isPatient()) {
      this.props.getPatient(this.props.patientUuid);
    } else {
      this.props.getPerson(this.props.patientUuid);
    }
  }

  isPatient() {
    return !!this.props.dashboardType ? this.props.dashboardType.toUpperCase() === DashboardType.PATIENT : true;
  }

  renderHeaderAttributeFields = (data, patient) => {
    const columns : any[] = [];
    let currentColumn : any[]  = [];
    const MAX_NUMBER_ELEMENTS_IN_COLUMN = 4;
    
    data.forEach((attrField, index) => {
      if (index > 0 && index % MAX_NUMBER_ELEMENTS_IN_COLUMN === 0) {
        columns.push(
          <div className='column'>
            {currentColumn}
          </div>
        );
        currentColumn = [];
      }

      currentColumn.push(
        <div className='field'>
          <span className='label-field'>{this.props.intl.formatMessage({ id: `${attrField.label}`, defaultMessage: `${attrField.label}` })}:</span>
          <span className='value-field'>{getPatientHeaderFieldValue(patient, attrField, this.props.locations)}</span>
        </div>
      );
    });

    if (currentColumn.length > 0) {
      columns.push(
        <div className='column'>
          {currentColumn}
        </div>
      );
    }

    return columns;
  }

  render() {
    const { patient, headerAppConfig, locations } = this.props;
    const isPatientExist = Object.keys(patient).length !== 0;

    const componentsLoaded = headerAppConfig != null && isPatientExist && locations != null;

    if (componentsLoaded) {
      let extraTitleInfoText = '';   
      headerAppConfig.titleFields.filter(titleField => !titleField.mainTitleField).forEach((titleField, index) => {
        extraTitleInfoText = extraTitleInfoText + getPatientHeaderFieldValue(patient, titleField, locations);
        if (index + 1 < headerAppConfig.titleFields.filter(titleField => !titleField.mainTitleField).length) {
          extraTitleInfoText = extraTitleInfoText + '/';
        }
      });

      extraTitleInfoText = extraTitleInfoText.endsWith('/') ? extraTitleInfoText.slice(0, -1) : extraTitleInfoText;
      extraTitleInfoText = extraTitleInfoText ? `(${extraTitleInfoText})` : '';

      return (
        <div className='header-container'>
          <div className='title-container'>
            <span className='title-field'>
              {headerAppConfig.titleFields.filter(titleField => titleField.mainTitleField).map(titleField => {
                return (
                  <>
                    {getPatientHeaderFieldValue(patient, titleField, locations) + ' '} 
                  </>
                );
              })}
              </span>
   
            <span className='title-field'>{extraTitleInfoText}</span>
  
            <span className='buttons-span'>
              {headerAppConfig.updateStatusButtonVisible && (
                <>
                  {this.props.children}
                </>
              )}
            </span>
          </div>

          <div className='fields-container'>
            {this.renderHeaderAttributeFields(headerAppConfig.attributeFields, patient)}
          </div>
        </div>
      );
    } else {
      return (
        <div className='header-container'>

        </div>
      );
    }
  }
}

const mapStateToProps = ({ patient, person, apps, location }: any) => ({
  loading: patient.patientLoading || person.personLoading,
  patient: patient.patient,
  person: person.person,
  headerAppConfig: apps.app?.config,
  locations: location.locations
});
  
const mapDispatchToProps = ({
  getPatient,
  getPerson,
  getAppById,
  searchLocations
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PatientHeader);