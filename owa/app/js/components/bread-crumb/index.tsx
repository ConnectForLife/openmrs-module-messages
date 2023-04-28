/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import React, { ReactFragment } from 'react';
import { connect } from 'react-redux';
import { Link, withRouter, RouteComponentProps } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import UrlPattern from 'url-pattern';
import './bread-crumb.scss';
import { UnregisterCallback } from 'history';
import * as Default from '../../shared/utils/messages';
import { getPerson } from '../../reducers/person.reducer';
import { IRootState } from '../../reducers';
import { PropsWithIntl } from '../../components/translation/PropsWithIntl';
import { injectIntl } from 'react-intl';

const ids = 'patientId&patientuuid=:patientUuid';

const PATIENT_TEMPLATE_PATTERN = new UrlPattern(`/messages/:dashboardType/:${ids}/patient-template/:newOrEdit*`);
const MANAGE_PATIENT_TEMPLATE_PATTERN = new UrlPattern(`/messages/:dashboardType/:${ids}/patient-template`);
const MANAGE_PATTERN = new UrlPattern(`/messages/manage*`);
const BASE_MESSAGES_PATTERN = new UrlPattern(`/messages/:dashboardType/:${ids}*`);

const MODULE_ROUTE = '/';
const OMRS_ROUTE = '../../';
const PATIENT_TEMPLATE_ROUTE = (patientId, patientUuid, dashboardType) => `/messages/${dashboardType}/${patientId}&patientUuid=${patientUuid}/patient-template`;
const CALENDAR_OVERVIEW_ROUTE = (patientId, patientUuid, dashboardType) => `/messages/${dashboardType}/${patientId}&patientUuid=${patientUuid}`;
const PATIENT_DASHBOARD_ROUTE = (patientUuid: string, dashboardType: string) => `${OMRS_ROUTE}coreapps/clinicianfacing/patient.page?patientId=${patientUuid}&dashboard=${dashboardType}`;
const SYSTEM_ADMINISTRATION_ROUTE = `${OMRS_ROUTE}coreapps/systemadministration/systemAdministration.page`;

interface IBreadCrumbProps extends DispatchProps, StateProps, RouteComponentProps {
  
}

interface IBreadCrumbState {
  current: string
}

class BreadCrumb extends React.PureComponent<PropsWithIntl<IBreadCrumbProps>, IBreadCrumbState>{
  unlisten: UnregisterCallback;

  constructor(props: PropsWithIntl<IBreadCrumbProps>) {
    super(props);

    const { history } = this.props;
    this.state = {
      current: history.location.pathname
    };
  }

  componentDidMount = () => {
    const { history } = this.props;
    this.unlisten = history.listen((location) => {
      const current = location.pathname;
      this.setState({ current });
    });
  }

  componentWillUnmount = () => {
    this.unlisten();
  }

  render = () => {
    return this.buildBreadCrumb();
  }

  buildBreadCrumb = () => {
    const { current } = this.state;

    return (
      <div id="breadcrumbs" className="breadcrumb">
        {this.renderCrumbs(this.getCrumbs(current))}
      </div>
    );
  }

  getCrumbs = (path: string): Array<ReactFragment> => {
    if (!!PATIENT_TEMPLATE_PATTERN.match(path.toLowerCase())) {
      return this.getPatientTemplateCrumbs(path);
    } else if (!!MANAGE_PATIENT_TEMPLATE_PATTERN.match(path.toLowerCase())) {
      return this.getManagePatientTemplateCrumbs(path);
    } else if (!!MANAGE_PATTERN.match(path.toLowerCase())) {
      return this.getManageBreadCrumbs();
    } else if (!!BASE_MESSAGES_PATTERN.match(path.toLowerCase())) {
      return this.getBaseMessagesCrumbs(path);
    } else {
      return [this.renderLastCrumb(this.props.intl.formatMessage({ id: 'messages.generalModuleBreadcrumb' }))];
    }
  }

  getBaseMessagesCrumbs = (path: string) => {
    return [
      this.getPatientNameCrumb(path),
      this.renderLastCrumb(this.props.intl.formatMessage({ id: 'messages.generalModuleBreadcrumb' }))
    ];
  }

  getPatientNameCrumb = (path: string) => {
    const match = BASE_MESSAGES_PATTERN.match(path.toLowerCase());
    const { patientUuid, dashboardType } = match;

    if (this.props.person.uuid != patientUuid) {
      this.props.getPerson(patientUuid);
    }

    const personName = this.props.person ? this.props.person.display : '';

    return this.renderCrumb(PATIENT_DASHBOARD_ROUTE(patientUuid, dashboardType), personName, true);
  }

  getManagePatientTemplateCrumbs = (path: string): Array<ReactFragment> => {
    const match = MANAGE_PATIENT_TEMPLATE_PATTERN.match(path.toLowerCase());

    return [
      this.getPatientNameCrumb(path),
      this.renderCrumb(CALENDAR_OVERVIEW_ROUTE(match.patientId, match.patientUuid, match.dashboardType),
        this.props.intl.formatMessage({ id: 'messages.generalModuleBreadcrumb' })),
      this.renderLastCrumb(this.props.intl.formatMessage({ id: 'messages.patientTemplateBreadcrumb' }))
    ];
  }

  getPatientTemplateCrumbs = (path: string): Array<ReactFragment> => {
    const match = PATIENT_TEMPLATE_PATTERN.match(path.toLowerCase());
    let msg = "";

    if (match.newOrEdit === "new") {
      msg = this.props.intl.formatMessage({ id: 'messages.newPatientTemplateBreadcrumb' });
    } else if (match.newOrEdit === "edit") {
      msg = this.props.intl.formatMessage({ id: 'messages.editPatientTemplateBreadcrumb' });
    }

    return [
      this.getPatientNameCrumb(path),
      this.renderCrumb(CALENDAR_OVERVIEW_ROUTE(match.patientId, match.patientUuid, match.dashboardType),
        this.props.intl.formatMessage({ id: 'messages.generalModuleBreadcrumb' })),
      this.renderCrumb(PATIENT_TEMPLATE_ROUTE(match.patientId, match.patientUuid, match.dashboardType),
        this.props.intl.formatMessage({ id: 'messages.managePatientTemplateBreadcrumb' })),
      this.renderLastCrumb(msg)
    ];
  }

  getManageBreadCrumbs = () => {
    return [
      this.renderCrumb(SYSTEM_ADMINISTRATION_ROUTE,
        this.props.intl.formatMessage({ id: 'messages.systemAdministrationBreadcrumb' }),
        true),
      this.renderLastCrumb(this.props.intl.formatMessage({ id: 'messages.manageBreadcrumb' }))
    ];
  }

  renderCrumbs = (elements: Array<ReactFragment>) => {
    const delimiter = this.renderDelimiter();

    return (
      <React.Fragment>
        {this.renderHomeCrumb()}
        {elements.map((e, i) =>
          <React.Fragment key={`crumb-${i}`}>
            {delimiter}
            {e}
          </React.Fragment>)}
      </React.Fragment>
    );
  }

  renderDelimiter = () => {
    return (
      <span className="breadcrumb-link-item breadcrumb-delimiter">
        <FontAwesomeIcon size="sm" icon={['fas', 'chevron-right']} />
      </span>);
  }

  renderHomeCrumb = () => {
    return (
      <a href={OMRS_ROUTE} className="breadcrumb-link-item home-crumb">
        <FontAwesomeIcon icon={['fas', 'home']} />
      </a>);
  }

  renderCrumb = (link: string, txt: string, isAbsolute?: boolean) => {
    if (isAbsolute) {
      return (
        <a href={link} className="breadcrumb-link-item" >{txt}</a>
      );
    } else {
      return <Link to={link} className="breadcrumb-link-item">{txt}</Link>;
    }
  }

  renderLastCrumb = (txt: string) => {
    return <span className="breadcrumb-last-item">{txt}</span>;
  }
}

const mapStateToProps = ({ person }: IRootState) => ({
  person: person.person
});

const mapDispatchToProps = ({
  getPerson
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default injectIntl(withRouter(connect(
  mapStateToProps,
  mapDispatchToProps
)(BreadCrumb)));