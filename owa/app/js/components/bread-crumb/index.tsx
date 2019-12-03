
import React from 'react';
import { connect } from 'react-redux';
import { Link, withRouter, RouteComponentProps } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import UrlPattern from 'url-pattern';
import './bread-crumb.scss';
import { UnregisterCallback } from 'history';

const PATIENT_TEMPLATE_PATTERN = new UrlPattern('/messages/patient-template*');
const NEW_PATIENT_TEMPLATE_PATTERN = new UrlPattern('/messages/patient-template/new*');
const EDIT_PATIENT_TEMPLATE_PATTERN = new UrlPattern('/messages/patient-template/edit*');
const MANAGE_PATTERN = new UrlPattern('/messages/manage*');

const MODULE_ROUTE = '/';
const OMRS_ROUTE = '../../';
const PATIENT_TEMPLATE_ROUTE = '/messages/patient-template/';

const GENERAL_MODULE_BREADCRUMB = 'Messages';
const PATIENT_TEMPLATE_BREADCRUMB = 'Patient Template';
const MANAGE_BREADCRUMB = 'Manage';
const NEW_PATIENT_TEMPLATE_BREADCRUMB = 'New';
const EDIT_PATIENT_TEMPLATE_BREADCRUMB = 'Edit';

interface IBreadCrumbProps extends RouteComponentProps {
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
    if (!!PATIENT_TEMPLATE_PATTERN.match(current.toLowerCase())) {
      return this.buildPatientTemplateBreadCrumb(current);
    } else if (!!MANAGE_PATTERN.match(current.toLowerCase())) {
      return this.buildManageBreadCrumb(current);
    } else {
      return (
        <div className="breadcrumb">
          {this.renderCrumbs([this.renderLastCrumb(GENERAL_MODULE_BREADCRUMB)])}
        </div>
      );
    }
  }

  buildPatientTemplateBreadCrumb = (path: string) => {
    const patientTemplateCrumbs = [
      this.renderCrumb(MODULE_ROUTE, GENERAL_MODULE_BREADCRUMB)
    ];

    if (NEW_PATIENT_TEMPLATE_PATTERN.match(path)) {
      patientTemplateCrumbs.push(this.renderCrumb(PATIENT_TEMPLATE_ROUTE, PATIENT_TEMPLATE_BREADCRUMB));
      patientTemplateCrumbs.push(this.renderLastCrumb(NEW_PATIENT_TEMPLATE_BREADCRUMB));
    } else if (EDIT_PATIENT_TEMPLATE_PATTERN.match(path)) {
      patientTemplateCrumbs.push(this.renderCrumb(PATIENT_TEMPLATE_ROUTE, PATIENT_TEMPLATE_BREADCRUMB));
      patientTemplateCrumbs.push(this.renderLastCrumb(EDIT_PATIENT_TEMPLATE_BREADCRUMB));
    } else {
      patientTemplateCrumbs.push(this.renderLastCrumb(PATIENT_TEMPLATE_BREADCRUMB));
    }

    return (
      <div className="breadcrumb">
        {this.renderCrumbs(patientTemplateCrumbs)}
      </div>
    );
  }

  buildManageBreadCrumb = (path: string) => {
    const manageCrumbs = [
      this.renderCrumb(MODULE_ROUTE, GENERAL_MODULE_BREADCRUMB)
    ];

    manageCrumbs.push(this.renderLastCrumb(MANAGE_BREADCRUMB));

    return (
      <div className="breadcrumb">
        {this.renderCrumbs(manageCrumbs)}
      </div>
    );
  }

  renderCrumbs = (elements: Array<any>) => {
    const delimiter = this.renderDelimiter();
    const lastElementId = elements.length - 1;
    return (
      <React.Fragment>
        {this.renderHomeCrumb()}
        {elements.map((e, i) =>
          <React.Fragment key={`crumb-${i}`}>
            {e}
            {i !== lastElementId && delimiter}
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

  renderCrumb = (link: string, txt: string) => {
    return <Link to={link} className="breadcrumb-link-item">{txt}</Link>;
  }

  renderLastCrumb = (txt: string) => {
    return <span className="breadcrumb-last-item">{txt}</span>;
  }
}

export default withRouter(connect()(BreadCrumb));
