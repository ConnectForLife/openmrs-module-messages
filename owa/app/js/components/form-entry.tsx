import React from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Tabs, Tab, Row, Col, Nav, NavItem } from 'react-bootstrap';
import './tab.scss';
import { FormSection } from '../shared/model/form-definition';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

interface IFormEntryProps {
  sections: Array<FormSection>
};

interface IFormEntryState {
};

export default class FormEntry extends React.PureComponent<IFormEntryProps, IFormEntryState> {
  render() {
    return (
      <Tab.Container id="left-tabs-example" defaultActiveKey="first">
        <Row className="clearfix">
          <Col md={3}>
            {this.props.sections.map((section) =>
              <Nav key={`section-${section.title}`} bsStyle="pills" stacked>
                <NavItem disabled={true}> {section.title} </NavItem>
                {section.subsections.map((subSection) =>
                  <NavItem key={`subSection-${subSection.name}`}
                    eventKey={subSection.name}>
                    <FontAwesomeIcon size="lg" icon={['far', 'check-circle']}
                      className={subSection.completed ? "icon-completed" : "icon-default"} />
                    <p>{` ${subSection.name}`}</p>
                  </NavItem>
                )}
              </Nav>
            )}
          </Col>
          <Col md={9}>
            <Tab.Content animation className="form-body">
              {this.props.sections.map((section) =>
                section.subsections.map((subSection) =>
                  <Tab.Pane key={`tabContent-${subSection.name}`} eventKey={subSection.name}>{subSection.body}</Tab.Pane>
                ))}
            </Tab.Content>
          </Col>
        </Row>
      </Tab.Container>
    );
  }
}
