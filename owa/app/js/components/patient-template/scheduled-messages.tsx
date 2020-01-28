import React from 'react';
import { connect } from 'react-redux';
import { IRootState } from '../../reducers';
import './patient-template.scss';
import Table from '@bit/soldevelo-omrs.cfl-components.table/table';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { getMessages } from '../../reducers/patient-template.reducer';
import MessageDetails from '../../shared/model/message-details';
import _ from 'lodash';
import ActorSchedule from '../../shared/model/actor-schedule';
import MessageRowData from '../../shared/model/message-row-data';
import { getActorList } from '../../reducers/actor.reducer'

interface IScheduledMessagesProps extends DispatchProps, StateProps {
  patientId: number,
  patientUuid: string,
  messageDetails: MessageDetails,
  loading: boolean
};

interface IScheduledMessagesState {
};

class ScheduledMessages extends React.PureComponent<IScheduledMessagesProps, IScheduledMessagesState> {
  componentDidMount() {
    this.props.getActorList(this.props.patientId);
  }

  private mapActorSchedules = (actorSchedules: Array<ActorSchedule>) => {
    const result = {};
    actorSchedules.forEach((actorSchedule) => {
      const id = actorSchedule.actorId ? actorSchedule.actorId : this.props.patientId;
      result[id] = actorSchedule.schedule;
    });
    return result;
  }

  private mapMessageDetailsToData = (): Array<MessageRowData> => {
    let data = [] as Array<MessageRowData>;

    if (!this.props.messageDetails) {
      return data;
    }

    let messages = this.props.messageDetails.messages;
    messages = _.orderBy(messages, ['createdAt'],['asc']);
    messages.forEach((m) => {
      data.push({
        messageType: m.type,
        schedules: this.mapActorSchedules(m.actorSchedules)
      })
    });

    return data;
  }

  private getMessages = () => {
    this.props.getMessages(this.props.patientId);
  }

  private renderMessagesTable = () => {
    const data = this.mapMessageDetailsToData();

    const columns = _.concat(
      this.getTypeColumnDefinition(),
      this.getSchedulesColumnDefinition(),
      this.getActionsColumnDefinition()
    );

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
    Header: 'Message Type',
    accessor: 'messageType',
  });

  private getSchedulesColumnDefinition = () => {
    if (!this.props.messageDetails) {
      return [];
    }
    const wrapedTextProps = () => {
      return {
        style: {
          whiteSpace: 'normal'
        },
      };
    }

    const actorSchedules = _.uniq(_.flatten(this.props.messageDetails.messages
      .map((msg) => msg.actorSchedules)));

    const columns = actorSchedules.map((actorSchedule: ActorSchedule) => {
      const id = actorSchedule.actorId ? actorSchedule.actorId : this.props.patientId;
      const name = this.getActorName(actorSchedule);
      const type = actorSchedule.actorType;
      return {
        Header: _.filter([type, name]).join(' - '),
        accessor: `schedules[${id}]`,
        getProps: wrapedTextProps
      };
    });
    return _.uniqBy(columns, 'Header');
  };

  private getActionsColumnDefinition = () => ({
    Header: 'Actions',
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
      //TODO in CFLm-376: Change link to proper messages sidebar val
      const link = `#messages/${this.props.patientId}&patientuuid=${this.props.patientUuid}/patient-template/edit/${props.value}`;
      return (
        <span>
          <a href={link}>
            <FontAwesomeIcon icon={['fas', 'pencil-alt']} size="lg" />
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
        <h4>Scheduled messages</h4>
        {this.renderMessagesTable()}
      </div>
    );
  }
}

const mapStateToProps = ({ patientTemplate, actor }: IRootState) => ({
  messageDetails: patientTemplate.messageDetails,
  loading: patientTemplate.messageDetailsLoading || actor.actorResultListLoading,
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
