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
import { Tab, Row, Col, Nav, NavItem } from 'react-bootstrap';
import './form-entry.scss';
import FormSubSection from './model/form-subsection';
import FormSection from './model/form-section';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

interface IFormEntryProps {
  sections: Array<FormSection>,
  activeSection?: string
};

interface IFormEntryState {
  activeKey?: string
};

export default class FormEntry extends React.PureComponent<IFormEntryProps, IFormEntryState> {
  constructor(props: IFormEntryProps) {
    super(props);
    this.state = {
      activeKey: props.activeSection
    };
  }

  componentDidUpdate(prevProps: IFormEntryProps, prevState: IFormEntryState) {
    if (prevProps.activeSection !== this.props.activeSection) {
      this.setState({
        activeKey: this.props.activeSection
      })
    }
  };

  handleSelect = (e, subSection: FormSubSection) => {
    this.setState({
      activeKey: subSection.name
    }, () => {
      if (subSection.onSelect) {
        subSection.onSelect(e);
      }
    });
  };

  renderNavSubSection = (subSection: FormSubSection) =>
    <NavItem key={`subSection-${subSection.name}`} onSelect={(e) => { this.handleSelect(e, subSection) }}
      eventKey={subSection.name}>
      <FontAwesomeIcon size="lg" icon={['far', 'check-circle']}
        className={subSection.isTouched ?
          subSection.isValid ? 'icon-completed' : 'icon-error'
          : 'icon-default'}
      />
      <p>{` ${subSection.name}`}</p>
    </NavItem>

  renderNavSection = (section: FormSection) =>
    <Nav key={`section-${section.title}`} bsStyle="pills" stacked>
      <NavItem disabled={true}> {section.title} </NavItem>
      {section.subsections.map((subSection) =>
        this.renderNavSubSection(subSection)
      )}
    </Nav>

  renderBodySection = () =>
    <Tab.Content animation className="form-body">
      {this.props.sections.map((section) =>
        section.subsections.map((subSection) =>
          <Tab.Pane key={`tabContent-${subSection.name}`} eventKey={subSection.name}>{subSection.body}</Tab.Pane>
        ))}
    </Tab.Content>

  render() {
    return (
      <Tab.Container id="form-entry" activeKey={this.state.activeKey} defaultActiveKey="first">
        <Row className="clearfix">
          <Col md={3}>
            {this.props.sections.map((section) =>
              this.renderNavSection(section)
            )}
          </Col>
          <Col md={9}>
            {this.renderBodySection()}
          </Col>
        </Row>
      </Tab.Container>
    );
  }
}
