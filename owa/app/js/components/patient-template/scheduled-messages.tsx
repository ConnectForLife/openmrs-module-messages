import React from 'react';
import { connect } from 'react-redux';
import { IRootState } from '../../reducers';
import './patient-template.scss';
import Table from '@bit/soldevelo-omrs.cfl-components.table/table';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { getMessagesPage } from '../../reducers/patient-template.reducer'
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

  mapActorSchedules = (actorSchedules: Array<ActorSchedule>) => {
    const result = {};
    actorSchedules.forEach((actorSchedule) => {
      result[actorSchedule.actorType] = actorSchedule.schedule
    });
    return result;
  }

  mapMessageDetailsToData = (): Array<MessageRowData> => {
    let data = [] as Array<MessageRowData>;

    if (!this.props.messageDetails) {
      return data;
    }

    this.props.messageDetails.messages.forEach((m) => {
      data.push({
        messageType: m.type,
        schedules: this.mapActorSchedules(m.actorSchedules)
      })
    })

    return data;
  }

  getMessages = (activePage: number, itemsPerPage: number, sort: string, order: string, filters: {}) => {
    this.props.getMessagesPage(activePage, itemsPerPage, this.props.patientId);
  }

  renderMessagesTable = () => {
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
        pages={this.props.pages}
        fetchDataCallback={this.getMessages}
      />);
  };

  getTypeColumnDefinition = () => ({
    Header: 'Message Type',
    accessor: 'messageType',
  });

  getSchedulesColumnDefinition = () => {
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

    const actorTypes = _.flatten(this.props.messageDetails.messages
      .map((msg) => msg.actorSchedules
        .map((s) => s.actorType)));

    return _.uniq(actorTypes).map((type) => ({
      Header: `${type} ID`,
      accessor: `schedules[${type}]`,
      getProps: wrapedTextProps
    }));
  };

  getActionsColumnDefinition = () => ({
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

  render() {
    return (
      <div className="body-wrapper">
        <h4>Scheduled messages</h4>
        {this.renderMessagesTable()}
      </div>
    );
  }
}

const mapStateToProps = ({ patientTemplate }: IRootState) => ({
  pages: patientTemplate.messageDetailsPages,
  messageDetails: patientTemplate.messageDetails,
  loading: patientTemplate.messageDetailsLoading
});

const mapDispatchToProps = ({
  getMessagesPage,
  getActorList
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ScheduledMessages);
