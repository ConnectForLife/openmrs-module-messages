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
import { getPatient } from './patient.reducer';
import { getPerson } from './person.reducer';
import './patient-header.scss';
import { patientUtil } from '@openmrs/react-components';
import { getIntl } from "@openmrs/react-components/lib/components/localization/withLocalization";
import { formatAge } from "@openmrs/react-components/lib/features/patient/utils";
import { DATE_FORMAT } from "@openmrs/react-components/lib/constants";
import dateFns from 'date-fns';

export enum DashboardType {
  PATIENT = 'PATIENT',
  PERSON = 'PERSON',
}

interface IHeaderProps extends DispatchProps, StateProps {
  patientUuid: string;
  dashboardType: string;
  redirectUrl?: string;
  displayTelephone?: boolean;
};

interface IHeaderState {
};

class Header extends React.Component<IHeaderProps, IHeaderState> {
  constructor(props: IHeaderProps) {
    super(props);
    this.handlePatientLink = this.handlePatientLink.bind(this);
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
    let identifierValues;
    if (!!this.props.person && !!this.props.person.attributes) {
      identifierValues = this.props.person.attributes
        .filter(a => a.attributeType.display === identifierName);
    }
    return !!identifierValues && identifierValues.length > 0 ? identifierValues[0].value : '';
  }

  private handlePatientLink() {
    if (this.props.redirectUrl) {
      location.href = this.props.redirectUrl;
    }
  }

  private renderDemographics(personDetails) {
    const intl = getIntl();
    const maleMsg = intl.formatMessage({ id: "reactcomponents.male", defaultMessage: "Male" });
    const femaleMsg = intl.formatMessage({ id: "reactcomponents.female", defaultMessage: "Female" });
    const givenName = intl.formatMessage({ id: "person.header.name.given", defaultMessage: "Given" });
    const middleName = intl.formatMessage({ id: "person.header.name.middle", defaultMessage: "Middle" });
    const familyName = intl.formatMessage({ id: "person.header.name.family", defaultMessage: "Family Name" });
    const telephoneNumber = intl.formatMessage({ id: "person.header.phonenumber", defaultMessage: "Telephone number:" });
    const { age } = formatAge(personDetails.birthdate, intl);
    return (
      <div className="demographics" onClick={this.handlePatientLink}>
        <h1 className="name">
          <span>
            <span className="PersonName-givenName">{patientUtil.getGivenName(personDetails)}</span>
            <em>{givenName}</em>
          </span>
          &nbsp;
          {
            patientUtil.getMiddleName(personDetails) &&
            <span>
              <span className="PersonName-middleName">{patientUtil.getMiddleName(personDetails)}</span>
              <em>{middleName}</em>
            </span>
          }
          &nbsp;
          <span>
            <span className="PersonName-familyName">{patientUtil.getFamilyName(personDetails)}</span>
            <em>{familyName}</em>
          </span>
          &nbsp;
          &nbsp;
          <div className="details-section">
            <span className="gender-age">
              <span className="gender">{personDetails.gender === 'M' ? maleMsg : femaleMsg}</span>
              <span className="age">
                {age}
                {personDetails.birthdate && ('(' + (personDetails.birthdateEstimated ? '~' : '') + dateFns.format(personDetails.birthdate, DATE_FORMAT) + ')')}
              </span>
            </span>

            {
              this.props.displayTelephone && patientUtil.getTelephoneNumber(personDetails) &&
              <span className="telephone">
                <span>
                  {telephoneNumber}
                </span>
                &nbsp;
                <span>
                  {patientUtil.getTelephoneNumber(personDetails)}
                </span>
              </span>
            }
          </div>
        </h1>
      </div>
    );
  }

  hasPreferredId = (personDetails) => {
    let hasPreferredId = false;
    if (personDetails.identifiers) {
      hasPreferredId = !!(personDetails.identifiers.filter(identifier => identifier.preferred && identifier.identifier).length);
    }
    return hasPreferredId;
  }

  renderPatientIdentifier(personDetails) {
    const intl = getIntl();
    const identifiers = personDetails.identifiers;
    const patientId = personDetails.isPerson ? intl.formatMessage({ id: "person.header.personId", defaultMessage: "Caregiver ID" })
      : intl.formatMessage({ id: "reactcomponents.patient.id", defaultMessage: "Patient ID" });
    return (this.hasPreferredId(personDetails) &&
      <div className="identifiers">
        <em onClick={this.handlePatientLink}>{patientId}</em>
        <div className="identifiers-number">
          {identifiers.map(identifier => { return (identifier.preferred && <span key={identifier.identifier}>{identifier.identifier}</span>) })}
        </div>
        <br />
      </div>
    );
  }

  render() {
    const personDetailsRaw = this.getPersonDetails();
    const personDetails = patientUtil.createFromRestRep(personDetailsRaw);
    if (personDetailsRaw && personDetailsRaw.person) {
      personDetails.birthdateEstimated = personDetailsRaw.person.birthdateEstimated;
      personDetails.isPerson = !this.isPatient();
    }

    return (
      <div className="patient-header-container">
        <div className="patient-header ">
          {!this.props.loading && this.renderDemographics(personDetails)}
          {!this.props.loading && this.renderPatientIdentifier(personDetails)}
        </div>
        <div className="secondLineFragments">
          {this.props.children}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ patient, person }: any) => ({
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

// @ts-ignore
export default connect(mapStateToProps, mapDispatchToProps)(Header);
