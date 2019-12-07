import React from 'react';
import { connect } from 'react-redux';
import { IRootState } from '../../reducers';
import './patient-template.scss';
//TODO in CFLM-376: use '@bit/soldevelo-omrs.cfl-components.table' after updating the shared table component 
import Table from './table/table';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  getTemplatesPage
} from '../../reducers/patient-template.reducer'

interface IScheduledMessagesProps extends DispatchProps, StateProps {
  patientId: string
};

interface IScheduledMessagesState {
};

class ScheduledMessages extends React.PureComponent<IScheduledMessagesProps, IScheduledMessagesState> {
  render() {
    //TODO in CFLM-376: Add real data, fetched from the backend
    const data = [{
      messageType: 'Mocked message type',
      patientSchedule: 'Daily, Every day, Starts: 01.01.2020, Ends: 30.09.2020',
      caregiverSchedule: 'Daily, Every day, Starts: 01.01.2020, Ends: 30.09.2020',
      actions: ''
    }]

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
        Header: 'Patient',
        accessor: 'patientSchedule',
        getProps: wrapedTextProps
      },
      {
        Header: 'Caregiver',
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
          fetchDataCallback={getTemplatesPage}
        />
      </div>
    );
  }
}

const mapStateToProps = ({ patientTemplate }: IRootState) => ({
});

const mapDispatchToProps = ({
  getTemplatesPage
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ScheduledMessages);
