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
import Calendar from '@bit/soldevelo-omrs.cfl-components.calendar';
import {
  Col,
  Row,
  Button,
  Checkbox
} from 'react-bootstrap';
import { Tabs, Tab } from 'react-bootstrap'
import { IRootState } from '../../reducers';
import { getServiceResultLists } from '../../reducers/calendar.reducer'
import { ServiceStatus } from '../../shared/enums/service-status';
import { ServiceResultListUI } from '../../shared/model/service-result-list-ui';
import moment, { Moment } from 'moment';
import {
  getTemplates
} from '../../reducers/patient-template.reducer'
import { TemplateUI } from '../../shared/model/template-ui';
import _ from 'lodash';
import { RouteComponentProps } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

interface ICalendarViewProps extends DispatchProps, StateProps, RouteComponentProps<{ patientId: string }> {
};

interface ICalendarViewState {
  startDate?: Moment;
  endDate?: Moment;
  activeTabKey?: string;
  filters: Object;
}

interface ICalendarMessageEvent {
  serviceName: string;
  status: ServiceStatus;
  executionDate: Moment;
  messageId: Object;
}

interface IActorIdWithEvents {
  actorId: string;
  actorDisplayName: string;
  events: Array<ICalendarMessageEvent>;
}


class CalendarView extends React.Component<ICalendarViewProps, ICalendarViewState> {
  constructor(props: ICalendarViewProps) {
    super(props);
    this.state = {
      startDate: undefined,
      endDate: undefined,
      activeTabKey: 'Patient',
      filters: {}
    };
  }

  componentDidMount() {
    this.props.getTemplates();
  }

  componentWillUpdate(nextProps: ICalendarViewProps, nextState: ICalendarViewState) {
    if (nextProps.templatesLoading === false && this.props.templatesLoading === true) {
      let initialFilters = {};
      nextProps.templates.forEach((template) => initialFilters[template.name as string] = true);
      this.setState({ filters: initialFilters });
    }
  }

  private dateRangeChanged = (start: Date, end: Date, tabKey: string) => {
    const patientId = parseInt(this.props.match.params.patientId, 10);
    if (patientId && tabKey === this.state.activeTabKey &&
      ((this.state.startDate && this.state.startDate.day()) !== start.getDay()
        || (this.state.endDate && this.state.endDate.day()) !== end.getDay())) {
      this.setState({
        startDate: moment(start),
        endDate: moment(end)
      }, () => this.props.getServiceResultLists(start, end, patientId));
    }
  }

  private prepareActorsData = () => {
    const actorsResults = [] as Array<IActorIdWithEvents>;
    const patientId = this.props.match.params.patientId;
    // We always want calendar for a patient, and it needs to be first
    actorsResults.push({
      actorId: patientId as string,
      actorDisplayName: 'Patient',
      events: []
    })
    this.props.serviceResultLists.forEach((resultList) => {
      var existingIndex = actorsResults.findIndex(a => a.actorId === (resultList.actorId as number).toString());
      // Temporary naming until endpoint for retrieving them is exposed
      var actorDisplayName = resultList.actorId === resultList.patientId ? 'Patient' : 'Caregiver-';
      var actorWithEvents = existingIndex !== -1
        ? actorsResults[existingIndex]
        : {
          actorId: (resultList.actorId as number).toString(),
          actorDisplayName: actorDisplayName,
          events: []
        } as IActorIdWithEvents;
      if (this.isServiceSelected(resultList.serviceName)) {
        this.parseEvents(resultList, actorWithEvents.events);
      }
      existingIndex !== -1 ? actorsResults[existingIndex] = actorWithEvents : actorsResults.push(actorWithEvents);
    });
    return actorsResults;
  }

  private parseEvents(resultList: ServiceResultListUI, actorEvents: Array<ICalendarMessageEvent>) {
    resultList.results.forEach((result) => {
      if (result.executionDate !== null) {
        // If there already exists event for an actor with same type and message id we need to group those as it was one message
        var index = actorEvents.findIndex(r => r.executionDate.valueOf() === (result.executionDate && result.executionDate.valueOf())
          && r.messageId === result.messageId);
        // append to existing one
        if (index !== -1) {
          actorEvents[index] = {
            ...actorEvents[index],
            serviceName: `${actorEvents[index].serviceName},\n${resultList.serviceName}`
          };
          // or push new one
        } else {
          actorEvents.push({
            serviceName: resultList.serviceName,
            status: result.serviceStatus,
            executionDate: result.executionDate,
            messageId: result.messageId
          });
        }
      }
    });
  }

  private parseDataToCalendarEvents = (data: Array<ICalendarMessageEvent>) => {
    return data.map(entry => ({
      id: `${entry.messageId.toString()}-${entry.executionDate.valueOf().toString()}`,
      title: '\n' + entry.serviceName,
      status: entry.status,
      start: entry.executionDate.toISOString()
    }))
  }

  private tabSelected = (key) => this.setState({ activeTabKey: key })

  private isServiceSelected = (val: string) => _.has(this.state.filters, val) && !!this.state.filters[val];

  fiterCheckboxChanged = (key: string, val: boolean) => {
    this.setState((prevState: Readonly<ICalendarViewState>) => {
      let oldFilters = prevState.filters;
      oldFilters[key] = val;
      return {
        filters: oldFilters
      }
    })
  }

  renderTemplateFilter = (template: TemplateUI) =>
    <Row key={template.name as string} className="u-pl-1_5em u-mr-0">
      <span>
        <Checkbox
          type="checkbox"
          inline
          className="u-mt-1_4em_neg"
          checked={this.isServiceSelected(template.name as string)}
          onChange={(evt) => this.fiterCheckboxChanged(template.name as string, (evt.target as HTMLInputElement).checked)} />
        {` ${template.name}`}
      </span>
    </Row>

  render() {
    const actorsResults: Array<IActorIdWithEvents> = this.prepareActorsData();

    return (
      <>
        <div className="row">
          <div className="col-md-12 col-xs-12">
            <h3>Calendar Overview</h3>
            <Tabs activeKey={this.state.activeTabKey} onSelect={this.tabSelected} >
              {actorsResults.map((actorWithResults, index) => {
                const tabName = actorWithResults.actorDisplayName === 'Patient' ? actorWithResults.actorDisplayName : actorWithResults.actorDisplayName + index.toString();
                return (
                  <Tab title={tabName} key={tabName} eventKey={tabName}>
                    <Row className="u-pl-1em">
                      <Col sm={9}>
                        <a href={`${window.location.href}/patient-template`}>
                          <Button className="btn btn-md pull-right btn-manage-messages">
                            Manage messages
                        </Button>
                        </a>
                        <Calendar
                          dateRangeChangedCallback={(startDate, endDate) => this.dateRangeChanged(startDate, endDate, tabName)}
                          events={this.parseDataToCalendarEvents(actorWithResults.events)}
                          key={tabName}
                        />
                      </Col>
                      <Col sm={3} className="u-p-0 u-mt-4_5em u-mr-0 calendar-filters">
                        <span>
                          <FontAwesomeIcon icon={['fas', 'filter']} />{' '}
                          Display:
                        </span>
                        {_.uniqBy(this.props.templates, 'name').map((template) => this.renderTemplateFilter(template))}
                      </Col>
                    </Row>
                  </Tab>)
              })}
            </Tabs>
          </div>
        </div>
      </>
    );
  }
}

const mapStateToProps = ({ calendar, patientTemplate }: IRootState) => ({
  serviceResultLists: calendar.serviceResultLists,
  templates: patientTemplate.templates,
  templatesLoading: patientTemplate.templatesLoading
});

const mapDispatchToProps = ({
  getServiceResultLists,
  getTemplates
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CalendarView);

