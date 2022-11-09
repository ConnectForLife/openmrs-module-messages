/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */


import React from 'react';
import { connect } from 'react-redux';
import Calendar from '../calendar-overview/calendar-overview';
import { Button, Checkbox, Col, Row, Tab, Tabs } from 'react-bootstrap';
import { IRootState } from '../../reducers';
import { getServiceResultLists } from '../../reducers/calendar.reducer'
import { ServiceStatus } from '../../shared/enums/service-status';
import { ServiceResultListUI } from '../../shared/model/service-result-list-ui';
import moment, { Moment } from 'moment';
import { getTemplates } from '../../reducers/patient-template.reducer'
import { TemplateUI } from '../../shared/model/template-ui';
import _ from 'lodash';
import { RouteComponentProps } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { getPersonStatus } from '../person-status/person-status.reducer';
import * as Default from '../../shared/utils/messages';
import { getActorList } from '../../reducers/actor.reducer';
import Timezone from '../timezone/timezone';
import { DashboardType } from '../../shared/model/dashboard-type';
import { getPersonStatusConfig } from '../../shared/utils/person-status';
import { MESSAGE_STATUS_FUTURE } from '../../shared/utils/statuses';
import { LocalizedMessage } from '@openmrs/react-components';

interface ICalendarViewProps extends DispatchProps, StateProps, RouteComponentProps<{ patientId: string }> {
  patientUuid: string;
  dashboardType: DashboardType,
  locale?: string
};

interface ICalendarViewState {
  startDate?: Moment;
  endDate?: Moment;
  activeTabKey?: string;
  filters: Object;
  patientId: number;
  patientUuid: string;
}

interface ICalendarMessageEventService {
  name: string,
  status: ServiceStatus
}

interface ICalendarMessageEvent {
  serviceName: string;
  status: ServiceStatus;
  executionDate: Moment;
  messageId: Object;
  services: Array<ICalendarMessageEventService>
}

interface IActorIdWithEvents {
  actorId: number;
  actorDisplayName: string;
  events: Array<ICalendarMessageEvent>;
}

const DATE_RANGE_CHANGE_CALLBACK_DEBOUNCE_DELAY = 200;
const MONTH = 'M';

class CalendarView extends React.Component<ICalendarViewProps, ICalendarViewState> {
  constructor(props: ICalendarViewProps) {
    super(props);
    this.state = {
      startDate: undefined,
      endDate: undefined,
      activeTabKey: undefined,
      filters: {},
      patientId: parseInt(this.props.match.params.patientId, 10),
      patientUuid: props.patientUuid
    };
  }

  componentDidMount() {
    this.props.getTemplates();
    this.props.getActorList(this.state.patientId, this.isPatient());
    let { startDate, endDate, patientId } = this.state;
    startDate = startDate ? startDate : moment().subtract(1, MONTH);
    endDate = endDate ? endDate : moment().add(1, MONTH);
    this.props.getServiceResultLists(startDate.toDate(), endDate.toDate(), patientId, this.isPatient());
    if (_.isEmpty(this.props.personStatus)) {
      this.props.getPersonStatus(this.state.patientUuid)
    }
  }

  componentWillUpdate(nextProps: ICalendarViewProps, nextState: ICalendarViewState) {
    if (nextProps.loading === false && this.props.loading === true) {
      let initialFilters = {};
      nextProps.templates.forEach((template) => initialFilters[template.name as string] = true);
      this.setState({ filters: initialFilters });
    }
  }

  private isPatient() {
    return !!this.props.dashboardType ? this.props.dashboardType.toUpperCase() === DashboardType.PATIENT : true;
  }

  // We have to debounce calling this method because fullcalendar 
  // calls dateRangeChangedCallback few times with the same values.
  // Debounce allows us to wait until last component update is performed
  private dateRangeChanged = _.debounce((start, end, tabKey) => {
    if (this.state.patientId && tabKey === this.state.activeTabKey &&
      ((this.state.startDate && !moment(this.state.startDate).isSame(moment(start)))
        || (this.state.endDate && !moment(this.state.endDate).isSame(moment(end)))
        || (this.state.startDate == null && this.state.endDate == null))) {
      this.setState({
        startDate: moment(start),
        endDate: moment(end)
      }, () => this.props.getServiceResultLists(start, end, this.state.patientId, this.isPatient()));
    }
  }, DATE_RANGE_CHANGE_CALLBACK_DEBOUNCE_DELAY);

  private prepareActorsData = () => {
    let actorsResults = [] as Array<IActorIdWithEvents>;
    if (this.isPatient()) {
      actorsResults = this.prepareActorResultsForPatient();
    } else {
      actorsResults = this.prepareActorResultsForPerson();
    }

    return this.appendDefaultDummyActorsIfNeeded(actorsResults);
  };

  private prepareActorResultsForPatient() {
    // We always want calendar for a patient, and it needs to be first
    const actorsResults = [] as Array<IActorIdWithEvents>;
    actorsResults.push({
      actorId: this.state.patientId,
      actorDisplayName: 'Patient',
      events: []
    });
    this.props.serviceResultLists.forEach((resultList) => {
      let existingIndex = actorsResults.findIndex(a => a.actorId === resultList.actorId);
      let actorDisplayName = this.getActorDisplayName(resultList.actorId, resultList.patientId);
      if (!!actorDisplayName) {
        let actorWithEvents = existingIndex !== -1
          ? actorsResults[existingIndex]
          : {
            actorId: resultList.actorId,
            actorDisplayName: actorDisplayName,
            events: []
          } as IActorIdWithEvents;
        if (this.isServiceSelected(resultList.serviceName)) {
          this.parseEvents(resultList, actorWithEvents.events);
        }
        existingIndex !== -1 ? actorsResults[existingIndex] = actorWithEvents : actorsResults.push(actorWithEvents);
      }
    });
    return actorsResults;
  }

  private prepareActorResultsForPerson() {
    const actorsResults = [] as Array<IActorIdWithEvents>;
    this.props.serviceResultLists.forEach((resultList) => {
      let existingIndex = actorsResults.findIndex(a => a.actorId === resultList.patientId);
      let actorDisplayName = this.getPatientDisplayName(resultList.actorId, resultList.patientId);
      if (!!actorDisplayName) {
        let actorWithEvents = existingIndex !== -1
          ? actorsResults[existingIndex]
          : {
            actorId: resultList.patientId,
            actorDisplayName: actorDisplayName,
            events: []
          } as IActorIdWithEvents;
        if (this.isServiceSelected(resultList.serviceName)) {
          this.parseEventsForPatient(resultList, actorWithEvents.events);
        }
        existingIndex !== -1 ? actorsResults[existingIndex] = actorWithEvents : actorsResults.push(actorWithEvents);
      }
    });
    return actorsResults;
  }

  private getActorDisplayName(actorId: number | null, patientId: number | null): string {
    let displayName = '';
    if (actorId === patientId) {
      return 'Patient';
    }

    if (actorId) {
      const actor = _.find(this.props.actorResultList, actor => actor.actorId === actorId);
      const role = actor && actor.actorTypeName ? actor.actorTypeName! : '';
      const name = actor && actor.actorName ? actor.actorName! : '';
      displayName = this.buildDisplayName(role, name);
    } else {
      console.error('Actor id is not specified');
    }
    return displayName;
  }

  private getPatientDisplayName(actorId: number | null, patientId: number | null) {
    let displayName = '';
    if (actorId) {
      const actor = _.find(this.props.actorResultList, actor => actor.actorId === patientId);
      const role = actor && actor.actorTypeName ? actor.actorTypeName! : '';
      const name = actor && actor.actorName ? actor.actorName! : '';
      displayName = this.buildDisplayName(role, name);
    } else {
      console.error('Actor id is not specified');
    }
    return displayName;
  }

  private getDefaultDummyActorResults(): Array<IActorIdWithEvents> {
    return this.props.actorResultList.map(result => {
      return {
        actorId: result.actorId,
        actorDisplayName: this.buildDisplayName(result.actorTypeName, result.actorName),
        events: []
      } as IActorIdWithEvents
    });
  }

  private buildDisplayName(role: string | null, name: string | null): string {
    return _.filter([role, name]).join(' - ');
  }

  private appendDefaultDummyActorsIfNeeded(actorsResults: IActorIdWithEvents[]): Array<IActorIdWithEvents> {
    return _.unionBy(actorsResults, this.getDefaultDummyActorResults(), 'actorId');
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
            serviceName: `${actorEvents[index].serviceName},\n${resultList.serviceName}`,
            services: [...actorEvents[index].services, {
              name: resultList.serviceName,
              status: result.serviceStatus
            }]
          };
          // or push new one
        } else {
          actorEvents.push({
            serviceName: resultList.serviceName,
            status: result.serviceStatus,
            executionDate: result.executionDate,
            messageId: result.messageId,
            services: [{
              name: resultList.serviceName,
              status: result.serviceStatus
            }]
          });
        }
      }
    });
  }

  private parseEventsForPatient(resultList: ServiceResultListUI, actorEvents: Array<ICalendarMessageEvent>) {
    resultList.results.forEach((result) => {
      if (result.executionDate !== null) {
        // If there already exists event for an actor with same type and message id we need to group those as it was one message
        var index = actorEvents.findIndex(r => r.executionDate.valueOf() === (result.executionDate && result.executionDate.valueOf())
          && r.messageId === result.messageId);
        // append to existing one
        if (index !== -1) {
          actorEvents[index] = {
            ...actorEvents[index],
            serviceName: `${actorEvents[index].serviceName},\n${resultList.serviceName}`,
            services: [...actorEvents[index].services, {
              name: resultList.serviceName,
              status: result.serviceStatus
            }]
          };
          // or push new one
        } else {
          actorEvents.push({
            serviceName: resultList.serviceName,
            status: result.serviceStatus,
            executionDate: result.executionDate,
            messageId: result.messageId,
            services: [{
              name: resultList.serviceName,
              status: result.serviceStatus
            }]
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
      start: entry.executionDate.toISOString(),
      services: entry.services,
      isDisabled: entry.status && entry.status.toString() === MESSAGE_STATUS_FUTURE
        && this.props.personStatus.value !== getPersonStatusConfig(this.props.locale).ACTIVATED.value
    }))
  };

  private tabSelected = (key) => this.setState({ activeTabKey: key })

  private isServiceSelected = (val: string) => _.has(this.state.filters, val) && !!this.state.filters[val];

  private getClassForCalendarArea = () => this.props.loading ? 'disable-element' : 'enable-element';

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
      <span className="template-name">
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
    let activeTabKey = this.state.activeTabKey;
    if (!activeTabKey && actorsResults.length > 0) {
      activeTabKey = actorsResults[0].actorDisplayName;
      this.setState({ activeTabKey: activeTabKey });
    }
    return (
      <>
        <Timezone />
        <div className="row">
          <div className="col-md-12 col-xs-12">
            <h2><LocalizedMessage id="MESSAGES_CALENDAR_OVERVIEW_LABEL" defaultMessage={Default.CALENDAR_OVERVIEW_LABEL} /></h2>
            <Tabs activeKey={activeTabKey} onSelect={this.tabSelected}>
              {actorsResults.map((actorWithResults, index) => {
                const tabName = actorWithResults.actorDisplayName;
                return (
                  <Tab title={tabName} key={tabName} eventKey={tabName}>
                    <Row className="u-pl-1em">
                      <Col sm={9}>
                        <a href={`${window.location.href}/patient-template`}>
                          <Button className="btn btn-md pull-right btn-manage-messages">
                            <LocalizedMessage id="MESSAGES_MANAGE_MESSAGES_LABEL" defaultMessage={Default.MANAGE_MESSAGES_LABEL} />
                          </Button>
                        </a>
                        <div className={this.getClassForCalendarArea()}>
                          <Calendar
                            dateRangeChangedCallback={(startDate, endDate) => this.dateRangeChanged(startDate, endDate, tabName)}
                            events={this.parseDataToCalendarEvents(actorWithResults.events)}
                            timezone={this.props.timezone}
                            key={tabName}
                          />
                        </div>
                      </Col>
                      <Col sm={3} className="u-p-0 u-mt-4_5em u-mr-0 calendar-filters">
                        <span>
                          <FontAwesomeIcon icon={['fas', 'filter']} />{' '}
                          <span className="display-header"><LocalizedMessage id="MESSAGES_DISPLAY_HEADER" defaultMessage={Default.DISPLAY_HEADER} /></span>
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

const mapStateToProps = ({ calendar, patientTemplate, actor, personStatus, timezone }: IRootState) => ({
  serviceResultLists: calendar.serviceResultLists,
  templates: patientTemplate.templates,
  loading: patientTemplate.templatesLoading || actor.actorResultListLoading || calendar.serviceResultListsLoading || personStatus.personStatusLoading || timezone.timezoneLoading,
  actorResultList: actor.actorResultList,
  personStatus: personStatus.status,
  timezone: timezone.timezone
});

const mapDispatchToProps = ({
  getServiceResultLists,
  getTemplates,
  getActorList,
  getPersonStatus
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CalendarView);

