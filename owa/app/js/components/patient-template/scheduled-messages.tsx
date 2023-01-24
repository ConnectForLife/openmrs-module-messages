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
import {connect} from 'react-redux';
import {IRootState} from '../../reducers';
import './patient-template.scss';
import Table from '../table/table';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {getMessages} from '../../reducers/patient-template.reducer';
import MessageDetails from '../../shared/model/message-details';
import _ from 'lodash';
import ActorSchedule from '../../shared/model/actor-schedule';
import MessageRowData from '../../shared/model/message-row-data';
import {getActorList} from '../../reducers/actor.reducer'
import {DashboardType} from "../../shared/model/dashboard-type";
import {IActor} from "../../shared/model/actor.model";
import { LocalizedMessage } from '@openmrs/react-components';
import * as Default from '../../shared/utils/messages';

interface IScheduledMessagesProps extends DispatchProps, StateProps {
  patientId: number,
  patientUuid: string,
  messageDetails: MessageDetails,
  loading: boolean,
  dashboardType: DashboardType,
  isPatient: boolean
}

interface IScheduledMessagesState {
}

class ScheduledMessages extends React.PureComponent<IScheduledMessagesProps, IScheduledMessagesState> {

  componentDidMount() {
    this.props.getActorList(this.props.patientId, this.isPatient());
  }

  private mapActorSchedules = (actorSchedules: Array<ActorSchedule>) => {
    const result = {};
    if (this.isPatient()) {
      actorSchedules.forEach((actorSchedule) => {
        const id = actorSchedule.actorId ? actorSchedule.actorId : this.props.patientId;
        result[id] = actorSchedule.schedule;
      });
    } else {
      actorSchedules.forEach((actorSchedule) => {
        const id = actorSchedule.patientId!;
        result[id] = actorSchedule.schedule;
      });
    }
    return result;
  };

  private mapMessageDetailsToData = (): Array<MessageRowData> => {
    let data = [] as Array<MessageRowData>;

    if (!this.props.messageDetails) {
      return data;
    }

    let messages = this.props.messageDetails.messages;
    messages = _.orderBy(messages, ['createdAt'], ['asc']);
    messages.forEach((m) => {
      data.push({
        messageType: m.type,
        schedules: this.mapActorSchedules(m.actorSchedules)
      })
    });

    return data;
  };

  private getMessages = () => {
    if (!this.props.messageDetailsLoading) {
      this.props.getMessages(this.props.patientId, this.props.isPatient);
    }
  };

  private renderMessagesTable = () => {
    const data = this.mapMessageDetailsToData();
    let columns;
    if (this.isPatient()) {
      columns = _.concat(
        this.getTypeColumnDefinition(),
        this.getSchedulesColumnDefinition(),
        this.getActionsColumnDefinition()
      );
    } else {
      columns = _.concat(
        this.getTypeColumnDefinition(),
        this.getSchedulesColumnDefinition()
      );
    }

    return (
      <Table
        data={data}
        columns={columns}
        loading={this.props.loading}
        pages={0}
        fetchDataCallback={this.getMessages}
        showPagination={false}
        sortable={false}
        multiSort={false}
        resizable={true}
      />);
  };

  private getTypeColumnDefinition = () => ({
    Header: <LocalizedMessage id="MESSAGES_MESSAGE_TYPE_LABEL" defaultMessage={Default.MESSAGE_TYPE} />,
    accessor: 'messageType',
  });

  private isPatient() {
    const dashboardType = this.props.dashboardType;
    return !!dashboardType ? dashboardType.toUpperCase() === DashboardType.PATIENT : true;
  }

  private getSchedulesColumnDefinition = () => {
    if (!this.props.messageDetails) {
      return [];
    }
    const wrappedTextProps = () => {
      return {
        style: {
          whiteSpace: 'normal'
        },
      };
    };

    const actorSchedules = _.uniq(_.flatten(this.props.messageDetails.messages
      .map((msg) => msg.actorSchedules)));

    let columns: any[];
    if (this.isPatient()) {
      columns = this.renderColumnsForPatient(actorSchedules, wrappedTextProps);
    } else {
      columns = this.renderColumnsForPerson(actorSchedules, wrappedTextProps);
    }
    return _.uniqBy(columns, 'Header');
  };

  private renderColumnsForPerson(actorSchedules, wrappedTextProps) {
    const columns = this.props.actorResultList
      .map((actor: IActor) => {
        const id = actor.actorId;
        const name = actor.actorName;
        const type = 'For patient';
        return {
          Header: _.filter([type, name]).join(' - '),
          accessor: `schedules[${id}]`,
          getProps: wrappedTextProps
        };
      });
    return columns;
  }

  private renderColumnsForPatient(actorSchedules, wrappedTextProps) {
    const columns = actorSchedules
    .map((actorSchedule: ActorSchedule) => {
      const id = actorSchedule.actorId ? actorSchedule.actorId : this.props.patientId;
      const name = this.getActorName(actorSchedule);
      const type = actorSchedule.actorType;
      return {
        Header: _.filter([type, name]).join(' - '),
        accessor: `schedules[${id}]`,
        getProps: wrappedTextProps
      };
    });
    return columns;
  }

  private getActionsColumnDefinition = () => ({
    Header: <LocalizedMessage id="MESSAGES_ACTIONS_LABEL" defaultMessage={Default.ACTIONS} />,
    accessor: 'messageType',
    getProps: () => {
      return {
        style: {
          maxWidth: 30,
          textAlign: 'center',
          margin: 'auto'
        },
      };
    },
    Cell: props => {
      const {patientId, patientUuid, dashboardType} = this.props;

      const link = `#messages/${dashboardType}/${patientId}&patientuuid=${patientUuid}/patient-template/edit/${props.value}`;
      return (
        <span>
          <a href={link}>
            <FontAwesomeIcon icon={['fas', 'pencil-alt']} size="lg"/>
          </a>
        </span>
      );
    }
  });

  private getActorName(actorSchedule: ActorSchedule): string {
    const actor = actorSchedule.actorId ?
      _.find(this.props.actorResultList, r => actorSchedule.actorId === r.actorId) : null;
    if (!actor) {
      return '';
    }
    return actor!.actorName ? actor!.actorName : '';
  }

  render() {
    return (
      <div className="body-wrapper">
        <h4><LocalizedMessage id="MESSAGES_SCHEDULED_MESSAGES_LABEL" defaultMessage={Default.SCHEDULED_MESSAGES} /></h4>
        {this.renderMessagesTable()}
      </div>
    );
  }
}

const mapStateToProps = ({patientTemplate, actor}: IRootState) => ({
  messageDetails: patientTemplate.messageDetails,
  loading: patientTemplate.messageDetailsLoading || actor.actorResultListLoading,
  messageDetailsLoading: patientTemplate.messageDetailsLoading,
  actorResultList: actor.actorResultList
});

const mapDispatchToProps = ({
  getMessages,
  getActorList
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ScheduledMessages);
