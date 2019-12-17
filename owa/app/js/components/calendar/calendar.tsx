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
  Row
} from 'react-bootstrap';
import { Tabs, Tab } from 'react-bootstrap'
import { IRootState } from '../../reducers';
import { getServiceResultLists } from '../../reducers/calendar.reducer'
import { ServiceStatus } from '../../shared/enums/service-status';
import { Button } from 'react-bootstrap';
import { ServiceResultListUI } from '../../shared/model/service-result-list-ui';
import moment, { Moment } from 'moment';

interface ICalendarViewProps extends DispatchProps, StateProps {
  patientId: number;
};

interface ICalendarViewState {
  startDate?: Moment;
  endDate?: Moment;
  activeTabKey?: string;
}

interface ICalendarMessageEvent {
  serviceName: string;
  status: ServiceStatus;
  executionDate: Moment;
  messageId: Object;
}

interface IActorIdWithEvents {
  actorId: number;
  actorDisplayName: string;
  events: Array<ICalendarMessageEvent>;
}


class CalendarView extends React.Component<ICalendarViewProps, ICalendarViewState> {
  constructor(props: ICalendarViewProps) {
    super(props);
    this.state = {
      startDate: undefined,
      endDate: undefined,
      activeTabKey: 'Patient'
    };
  }

  private dateRangeChanged = (start: Date, end: Date, tabKey: string) => {
    if (this.props.patientId && tabKey === this.state.activeTabKey &&
      ((this.state.startDate && this.state.startDate.day()) !== start.getDay()
        || (this.state.endDate && this.state.endDate.day()) !== end.getDay())) {
      this.setState({
        startDate: moment(start),
        endDate: moment(end)
      }, () => this.props.getServiceResultLists(start, end, this.props.patientId));
    }
  }

  private prepareActorsData = () => {
    const actorsResults = [] as Array<IActorIdWithEvents>;
    // We always want calendar for a patient, and it needs to be first
    actorsResults.push({
      actorId: this.props.patientId,
      actorDisplayName: 'Patient',
      events: []
    })
    this.props.serviceResultLists.forEach((resultList) => {
      var existingIndex = actorsResults.findIndex(a => a.actorId === resultList.actorId);
      // Temporary naming until endpoint for retrieving them is exposed
      var actorDisplayName = resultList.actorId === resultList.patientId ? 'Patient' : 'Caregiver-';
      var actorWithEvents = existingIndex !== -1
        ? actorsResults[existingIndex]
        : {
          actorId: resultList.actorId,
          actorDisplayName: actorDisplayName,
          events: []
        } as IActorIdWithEvents;
      this.parseEvents(resultList, actorWithEvents.events);
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

  render() {
    const actorsResults: Array<IActorIdWithEvents> = this.prepareActorsData();

    return (
      <div className="body-wrapper">
        <div className="row">
          <div className="col-md-12 col-xs-12">
            <h3>Calendar Overview</h3>
            <Tabs activeKey={this.state.activeTabKey} onSelect={this.tabSelected} >
              {actorsResults.map((actorWithResults, index) => {
                const tabName = actorWithResults.actorDisplayName === 'Patient' ? actorWithResults.actorDisplayName : actorWithResults.actorDisplayName + index.toString();
                return (
                  <Tab title={tabName} key={tabName} eventKey={tabName}>
                    <Row>
                      <Col sm={1}>
                      </Col>
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
                      <Col sm={2}>
                      </Col>
                    </Row>
                  </Tab>)
              })}
            </Tabs>
          </div>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ calendar }: IRootState) => ({
  serviceResultLists: calendar.serviceResultLists
});

const mapDispatchToProps = ({
  getServiceResultLists
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CalendarView);

