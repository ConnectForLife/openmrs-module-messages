/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
import React from 'react';
import { connect } from 'react-redux';
import { IRootState } from '../../reducers';
import { PatientHeader } from '@openmrs/react-components';
import { getPatient } from '../../reducers/patient.reducer'
import { Accordion } from '@openmrs/react-components';

interface IHeaderProps extends DispatchProps, StateProps {
  patientUuid: string;
};

class Header extends React.Component<IHeaderProps> {
  constructor(props: IHeaderProps) {
    super(props);
  }

  componentDidMount = () => {
    this.props.getPatient(this.props.patientUuid);
  }

  render() {
    return (
      <>
        {!this.props.patientLoading && !!this.props.patient &&
          <PatientHeader
            patient={this.props.patient}
            note={[]}
          />
        } 
      </>
    );
  }
}

const mapStateToProps = ({ patient }: IRootState) => ({
  patientLoading: patient.patientLoading,
  patient: patient.patient
});

const mapDispatchToProps = ({
  getPatient
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Header);

