import React from 'react';
import { connect } from 'react-redux';
import { IRootState } from '../../reducers';
import './patient-template.scss';
//TODO in CFLM-376: use '@bit/soldevelo-omrs.cfl-components.table' after updating the shared table component 
import Table from './table/table';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { getMessages } from '../../reducers/patient-template.reducer'
import MessageDetails from '../../shared/model/message-details';

interface IScheduledMessagesProps extends DispatchProps, StateProps {
  patientId: string
  messageDetails: MessageDetails
};

interface IScheduledMessagesState {
};

class ScheduledMessages extends React.PureComponent<IScheduledMessagesProps, IScheduledMessagesState> {
  componentDidMount() {
    this.props.getMessages(null, null, null, null, this.props.patientId);
  }

  // TODO: Remove when switching to real data usage
  mockMessageDetails = () => {
    return [{
      messageType: 'Adherence report daily',
      patientSchedule: 'Daily, Every day, Starts: 01.01.2020, Ends: 30.09.2020',
      caregiverSchedule: 'Daily, Every day, Starts: 01.01.2020, Ends at 22.03.2020',
      actions: ''
    },
    {
      messageType: 'Adherence report weekly',
      patientSchedule: 'Weekly, Every day, Starts: 30.09.2020, Ends: 20.12.2020',
      caregiverSchedule: 'Weekly, Every day, Starts: 30.09.2020, Ends: 20.12.2020',
      actions: ''
    },
    {
      messageType: 'Adherence feedback',
      patientSchedule: 'Weekly, Monday, Starts: 01.01.2020, Ends: after 20 times',
      caregiverSchedule: 'Weekly, Monday, Starts: 01.01.2020, Ends: after 20 times',
      actions: ''
    },
    {
      messageType: 'Health tip',
      patientSchedule: 'Weekly, Monday, Starts: 01.01.2020, Categories: diet, lifestyle, Ends: after 20 times',
      caregiverSchedule: 'Weekly, Monday, Starts: 01.01.2020, Ends: after 20 times',
      actions: ''
    },
    {
      messageType: 'Visit reminder',
      patientSchedule: 'Weekly, Monday, Starts: 01.01.2020, Ends: after 20 times',
      caregiverSchedule: 'Weekly, Monday, Starts: 01.01.2020, Ends: after 20 times',
      actions: ''
    },
    {
      messageType: 'Survey',
      patientSchedule: 'Weekly, Monday, Starts: 01.01.2020, Ends: after 20 times',
      caregiverSchedule: 'Weekly, Monday, Starts: 01.01.2020, Ends: after 20 times',
      actions: ''
    },
    ];
  }

  mapMessageDetailsToData = () => {
    let data = [] as any[];

    if (this.props.messageDetails) {
      // TODO in CFLM-319: add schedule strings
      this.props.messageDetails.messages.forEach((m) => {
        data.push({
          messageType: m.type,
          patientSchedule: '(this data is not available yet)',
          caregiverSchedule: '(this data is not available yet)',
          actions: ''
        })
      })
    }

    // TODO in CFLM-376: connect entries with the same message type
    return data;
  }

  render() {
    // TODO: To use real data switch below lines    
    // const data = this.mapMessageDetailsToData();
    const data = this.mockMessageDetails();

    const wrapedTextProps = () => {
      return {
        style: {
          whiteSpace: 'normal'
        },
      };
    }

    const columns = [
      {
        Header: 'Message Type',
        accessor: 'messageType',
      },
      {
        Header: 'Patient ID',
        accessor: 'patientSchedule',
        getProps: wrapedTextProps
      },
      {
        Header: 'Caregiver ID',
        accessor: 'caregiverSchedule',
        getProps: wrapedTextProps
      },
      {
        Header: 'Actions',
        accessor: 'actions',
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
          //TODO to proper messages sidebar val
          const link = `#messages/${this.props.patientId}/patient-template/edit/${props.value}`;
          return (
            <span>
              <a href={link}>
                <FontAwesomeIcon icon={['fas', 'pencil-alt']} size="lg" />
              </a>
            </span>
          );
        }
      }
    ];

    return (
      <div className="body-wrapper">
        <h4>Scheduled messages</h4>
        <Table
          data={data}
          columns={columns}
          loading={false}
          //TODO in CFLM-377: Change to pages size
          pages={1}
          fetchDataCallback={getMessages}
        />
      </div>
    );
  }
}

const mapStateToProps = ({ patientTemplate }: IRootState) => ({
  messageDetails: patientTemplate.messageDetails
});

const mapDispatchToProps = ({
  getMessages
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ScheduledMessages);
