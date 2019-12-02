//TODO: CFLM-355 File to correct

import React from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { Link, withRouter } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import UrlPattern from 'url-pattern';

import './bread-crumb.scss';

export const PATIENT_TEMPLATE_NEW_FLOW_ROUTE = '#/messages/patient-template/new';
const PATIENT_TEMPLATE_NEW_PATTERN = new UrlPattern('/messages/patient-template/new*');
const PATIENT_TEMPLATE_ROUTE = '/messages/patient-template/';
const PATIENT_TEMPLATE_PATTERN = new UrlPattern('/patient-template*');
const MODULE_ROUTE = '/';
const OMRS_ROUTE = '../../';
const PATIENT_TEMPLATE_NEW_BREADCRUMB = 'Patient Template';
const PATIENT_TEMPLATE_NEW_BREADCRUMB_NEW = 'New';

class BreadCrumb extends React.Component {
  constructor(props) {
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

  renderCrumb = (link, txt) => {
    return <Link to={link} className="breadcrumb-link-item">{txt}</Link>;
  }

  renderLastCrumb = (txt) => {
    return <span className="breadcrumb-last-item">{txt}</span>;
  }

  renderCrumbs = elements => {
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

  buildPathDynamically = (pattern, path) => {
    return pattern.match(path)._.split('/')
      .filter(e => !!e)
      .map((e, i) => 
          <span key={`${pattern}-${i}`} >
            {this.renderLastCrumb(e)}
          </span>
        
      );
  }

  buildPatientTemplateBreadCrumb = (path) => {
    const patientTemplateName = PATIENT_TEMPLATE_NEW_BREADCRUMB;
    const patientTemplateCrumbs = [
      this.renderCrumb(MODULE_ROUTE, "Messages")
    ];

    if (PATIENT_TEMPLATE_NEW_PATTERN.match(path)) {
      patientTemplateCrumbs.push(this.renderCrumb(PATIENT_TEMPLATE_ROUTE, patientTemplateName));
      patientTemplateCrumbs.push(this.renderLastCrumb(PATIENT_TEMPLATE_NEW_BREADCRUMB_NEW));
    } else if (PATIENT_TEMPLATE_PATTERN.match(path)._) {
      patientTemplateCrumbs.push(this.renderCrumb(PATIENT_TEMPLATE_ROUTE, patientTemplateName));
      patientTemplateCrumbs.push(this.buildPathDynamically(PATIENT_TEMPLATE_PATTERN, path));
    } else {
      patientTemplateCrumbs.push(this.renderLastCrumb(patientTemplateName));
    }

    console.log(patientTemplateCrumbs);
    return (
      <div className="breadcrumb">
        {this.renderCrumbs(patientTemplateCrumbs)}
      </div>
    );
  }

  buildBreadCrumb = () => {
    const { current } = this.state;
    console.log(current);

    console.log(PATIENT_TEMPLATE_NEW_PATTERN.match(current));
    console.log(PATIENT_TEMPLATE_NEW_PATTERN);

    if (!!PATIENT_TEMPLATE_PATTERN.match(current.toLowerCase())) {
      return this.buildPatientTemplateBreadCrumb(current);
    } else {
      return (
        <div className="breadcrumb">
          {this.renderCrumbs([this.renderLastCrumb("Messages")])}
        </div>
      );
    }
  }

  render = () => {
    return this.buildBreadCrumb();
  }
}

BreadCrumb.propTypes = {
  history: PropTypes.shape({}).isRequired
};

export default withRouter(connect()(BreadCrumb));
