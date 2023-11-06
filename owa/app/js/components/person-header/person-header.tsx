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
import { getShowGenderPersonHeader, getShowAgePersonHeader } from "../../reducers/global-property.reducer"
import { patientUtil } from '@openmrs/react-components';
import { formatAge } from "@openmrs/react-components/lib/features/patient/utils";
import { PropsWithIntl } from '../../components/translation/PropsWithIntl';
import { injectIntl } from 'react-intl';

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

class Header extends React.Component<PropsWithIntl<IHeaderProps>, IHeaderState> {
  constructor(props: PropsWithIntl<IHeaderProps>) {
    super(props);
    this.handlePatientLink = this.handlePatientLink.bind(this);
  }

  componentDidMount = () => {
    this.props.getShowGenderPersonHeader();
    this.props.getShowAgePersonHeader();
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
      window.location.href = this.props.redirectUrl;
    }
  }

  private renderDemographics(personDetails) {
    const maleMsg = this.props.intl.formatMessage({ id: 'reactcomponents.male' });
    const femaleMsg = this.props.intl.formatMessage({ id: 'reactcomponents.female' });
    const otherMsg = this.props.intl.formatMessage({ id: 'reactcomponents.other' });
    const unknownGenderMsg = this.props.intl.formatMessage({ id: 'reactcomponents.unknown' });
    const givenName = this.props.intl.formatMessage({ id: 'person.header.name.given' });
    const middleName = this.props.intl.formatMessage({ id: 'person.header.name.middle' });
    const familyName = this.props.intl.formatMessage({ id: 'person.header.name.family' });
    const telephoneNumber = this.props.intl.formatMessage({ id: 'person.header.phonenumber' });
    const unknownAgeMsg =  this.props.intl.formatMessage({ id: 'person.header.age.unknown' });
    let gender = unknownGenderMsg;
    if (this.props.isShowGenderPersonHeader && this.props.isShowGenderPersonHeader!['value'].toUpperCase() === 'TRUE') {
        gender = (personDetails.gender === 'M' ? maleMsg : (personDetails.gender === 'F' ? femaleMsg : (personDetails.gender === 'O' ? otherMsg : unknownGenderMsg)));
    }
    let age = unknownAgeMsg;
    if (this.props.isShowAgePersonHeader && this.props.isShowAgePersonHeader!['value'].toUpperCase() === 'TRUE') {
        age = (personDetails.birthdate ? formatAge(personDetails.birthdate, this.props.intl)!['age'] : unknownAgeMsg);
    }
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
              <span className="gender">{gender}</span>
              <span className="age">{age}</span>
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
    const SHIP_ID_UUID = "871fd410-b8ab-11eb-988e-0242ac120002";
    const identifiers = personDetails.identifiers;
    const shipId = identifiers.find( ({ identifierType }) => identifierType.uuid === SHIP_ID_UUID);
    const patientId = personDetails.isPerson ? this.props.intl.formatMessage({ id: 'person.header.personId' })
      : this.props.intl.formatMessage({ id: "reactcomponents.patient.id" });
    return (this.hasPreferredId(personDetails) &&
      <div className="identifiers">
        {shipId && <span id="shipId">{shipId.identifier}</span>}
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

const mapStateToProps = ({ patient, person, globalPropertyReducer }: any) => ({
  loading: patient.patientLoading || person.personLoading,
  patient: patient.patient,
  person: person.person,
  isShowGenderPersonHeader: globalPropertyReducer.isShowGenderPersonHeader,
  isShowAgePersonHeader: globalPropertyReducer.isShowAgePersonHeader
});

const mapDispatchToProps = ({
  getPatient,
  getPerson,
  getShowGenderPersonHeader,
  getShowAgePersonHeader
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

// @ts-ignore
export default injectIntl(connect(mapStateToProps, mapDispatchToProps)(Header));
