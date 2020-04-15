/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import React from 'react';
import {connect} from 'react-redux';
import {IRootState} from '../../reducers';
import {PatientHeader} from '@openmrs/react-components';
import {getPatient} from '../../reducers/patient.reducer';
import {getPerson} from '../../reducers/person.reducer';
import PersonStatus from './person-status/person-status';
import './patient-header.scss';
import {DashboardType} from "../../shared/model/dashboard-type";

interface IHeaderProps extends DispatchProps, StateProps {
  patientUuid: string;
  dashboardType: string;
};

class Header extends React.Component<IHeaderProps> {
  constructor(props: IHeaderProps) {
    super(props);
  }

  componentDidMount = () => {
    if (this.isPatient()) {
      this.props.getPatient(this.props.patientUuid);
    } else {
      this.props.getPerson(this.props.patientUuid);
    }
  }

  private isPatient() {
    return !!this.props.dashboardType ? this.props.dashboardType.toUpperCase() === DashboardType.PATIENT : true;
  }

  private getPersonDetails() {
    if (this.isPatient()) {
      return this.props.patient;
    } else {

      return {
        person: this.props.person,
        identifiers: [{
          identifierType: {},
          preferred: true,
          identifier: this.getPersonIdentifier()
        }]
      };
    }
  }

  private getPersonIdentifier() {
    const identifierName = 'Person identifier';
    this.setPersonIdentifierLabel(identifierName);
    let identifierValues;
    if (!!this.props.person && !!this.props.person.attributes) {
      identifierValues = this.props.person.attributes
        .filter(a => a.attributeType.display === identifierName);
    }
    return !!identifierValues && identifierValues.length > 0 ? identifierValues[0].value : '';
  }

  private setPersonIdentifierLabel(label) {
    // workaround to adjust patient header for person
    try {
      let checkExist = setInterval(function() {
        if (!!document.getElementsByClassName("identifiers")[0]) {
          // @ts-ignore
          document.getElementsByClassName("identifiers")[0].children[0].innerText = label;
          clearInterval(checkExist);
        }
      }, 100);
    } catch (e) {
      console.debug('Skipping setting the person identifier');
    }
  }

  render() {
    const personDetails = this.getPersonDetails();
    return (
      <div className="patient-header-container">
        {!this.props.loading && (
          <>
            <PatientHeader
              patient={personDetails}
              note={[]}
            />
            <PersonStatus patientUuid={this.props.patientUuid}/>
          </>
        )}
      </div>
    );
  }
}

const mapStateToProps = ({patient, person}: IRootState) => ({
  loading: patient.patientLoading || person.personLoading,
  patient: patient.patient,
  person: person.person
});

const mapDispatchToProps = ({
  getPatient,
  getPerson
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Header);

