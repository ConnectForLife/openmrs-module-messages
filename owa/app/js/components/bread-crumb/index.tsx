
import React, { ReactFragment } from 'react';
import { connect } from 'react-redux';
import { Link, withRouter, RouteComponentProps } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import UrlPattern from 'url-pattern';
import './bread-crumb.scss';
import { UnregisterCallback } from 'history';
import * as Msg from '../../shared/utils/messages';
import { getPatient } from '../../reducers/patient.reducer';
import { IRootState } from '../../reducers';

const ids = 'patientId&patientuuid=:patientUuid';

const PATIENT_TEMPLATE_PATTERN = new UrlPattern(`/messages/:${ids}/patient-template/:newOrEdit*`);
const MANAGE_PATTERN = new UrlPattern('/messages/manage*');
const BASE_MESSAGES_PATTERN = new UrlPattern(`/messages/:${ids}*`);

const MODULE_ROUTE = '/';
const OMRS_ROUTE = '../../';
const PATIENT_TEMPLATE_ROUTE = (patientId, patientUuid) => `/messages/${patientId}&patientUuid=${patientUuid}/patient-template/`;
const PATIENT_DASHBOARD_ROUTE = patientUuid => `${OMRS_ROUTE}coreapps/clinicianfacing/patient.page?patientId=${patientUuid}`;
const SYSTEM_ADMINISTRATION_ROUTE = `${OMRS_ROUTE}coreapps/systemadministration/systemAdministration.page`;

interface IBreadCrumbProps extends DispatchProps, StateProps, RouteComponentProps {
};

interface IBreadCrumbState {
  current: string
};

class BreadCrumb extends React.PureComponent<IBreadCrumbProps, IBreadCrumbState>{
  unlisten: UnregisterCallback;

  constructor(props: IBreadCrumbProps) {
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
      <div className="breadcrumb">
        {this.renderCrumbs(this.getCrumbs(current))}
      </div>
    );
  }

  getCrumbs = (path: string): Array<ReactFragment> => {
    if (!!PATIENT_TEMPLATE_PATTERN.match(path.toLowerCase())) {
      return this.getPatientTemplateCrumbs(path);
    } else if (!!MANAGE_PATTERN.match(path.toLowerCase())) {
      return this.getManageBreadCrumbs();
    } else if (!!BASE_MESSAGES_PATTERN.match(path.toLowerCase())) {
      return this.getBaseMessagesCrumbs(path);
    } else {
      return [this.renderLastCrumb(Msg.GENERAL_MODULE_BREADCRUMB)];
    }
  }

  getBaseMessagesCrumbs = (path: string) => {
    return [
      this.getPatientNameCrumb(path),
      this.renderLastCrumb(Msg.GENERAL_MODULE_BREADCRUMB)
    ];
  }

  getPatientNameCrumb = (path: string) => {
    const match = BASE_MESSAGES_PATTERN.match(path.toLowerCase());
    const patientUuid = match.patientUuid;

    if (this.props.patient.uuid != patientUuid) {
      this.props.getPatient(patientUuid);
    }

    const patientName = this.props.patient.person ? this.props.patient.person.display : '';
    return this.renderCrumb(PATIENT_DASHBOARD_ROUTE(patientUuid), patientName, true)
  }

  getPatientTemplateCrumbs = (path: string): Array<ReactFragment> => {
    const match = PATIENT_TEMPLATE_PATTERN.match(path.toLowerCase());
    let msg = "";

    if (match.newOrEdit === "new") {
      msg = Msg.NEW_PATIENT_TEMPLATE_BREADCRUMB;
    } else if (match.newOrEdit === "edit") {
      msg = Msg.EDIT_PATIENT_TEMPLATE_BREADCRUMB;
    }

    return [
      this.getPatientNameCrumb(path),
      this.renderCrumb(PATIENT_TEMPLATE_ROUTE(match.patientId, match.patientUuid), Msg.GENERAL_MODULE_BREADCRUMB),
      this.renderLastCrumb(msg)
    ];
  }

  getManageBreadCrumbs = () => {
    return [
      this.renderCrumb(SYSTEM_ADMINISTRATION_ROUTE, Msg.SYSTEM_ADMINITRATION_BREADCRUMB, true),
      this.renderLastCrumb(Msg.MANAGE_BREADCRUMB)
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
      <span className="breadcrumb-link-item">
        <FontAwesomeIcon size="xs" icon={['fas', 'chevron-right']} />
      </span>);
  }

  renderHomeCrumb = () => {
    return (
      <a href={OMRS_ROUTE} className="breadcrumb-link-item">
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

const mapStateToProps = ({ patient }: IRootState) => ({
  patient: patient.patient
});

const mapDispatchToProps = ({
  getPatient
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default withRouter(connect(
  mapStateToProps,
  mapDispatchToProps
)(BreadCrumb));
