import React from 'react';
import { Button } from 'react-bootstrap';
import * as Msg from '../../shared/utils/messages';
import { IRootState } from '../../reducers';
import { RouteComponentProps } from 'react-router';
import { connect } from 'react-redux';
import { getBestContactTime, postBestContactTime, updateBestConstactTime } from '../../reducers/best-contact-time.reducer';
import { history } from '../../config/redux-store';
import './best-contact-time.scss';
import { TimePicker } from 'antd';
import 'antd/dist/antd.css';
import { IBestContactTime } from '../../shared/model/best-contact-time.model';
import _ from 'lodash';
import moment from 'moment';

interface IBestContactTimeProps extends DispatchProps, StateProps {
  patientId: string
};

interface IBestContactTimeState {
};

class BestContactTime extends React.PureComponent<IBestContactTimeProps, IBestContactTimeState> {

  constructor(props: IBestContactTimeProps) {
    super(props);
  }

  componentDidMount() {
    this.props.getBestContactTime(this.props.patientId);
  }

  handleCalendarOverview = () => {
    const patientId = this.props.patientId;
    history.push(`/messages/${patientId}`);
  }

  handleSave = () => {
    if (this.props.bestContactTime.patientTime) {
      this.props.postBestContactTime(this.props.patientId, this.props.bestContactTime.patientTime);      
    }
    //TODO add save for caregivers
  }

  renderCalendarOverviewButton() {
    return (
      <Button
        className="btn btn-secondary btn-md"
        onClick={this.handleCalendarOverview}>
        {Msg.CALENDAR_OVERVIEW_LABEL}
      </Button>
    );
  }

  renderSaveButton() {
    return (
      <Button
        className="btn btn-success btn-md"
        onClick={this.handleSave}>
        {Msg.SAVE_BUTTON_LABEL}
      </Button>
    );
  }
  
  onPatientBestContactTime = (time, timeString) => {
    let newValue : IBestContactTime = _.cloneDeep(this.props.bestContactTime);
    newValue.patientTime = timeString;
    this.props.updateBestConstactTime(newValue);
  }

  onCaregiverBestContactTime = (time, timeString) => {
    let newValue : IBestContactTime = _.cloneDeep(this.props.bestContactTime);
    newValue.caregiverTime = timeString;
    this.props.updateBestConstactTime(newValue);
  }

  renderTimePickers() {
    const {bestContactTime} = this.props;
    return (
      <div className="sections">
        <div className="time-section">
          <span>Patient</span>
          <TimePicker value={moment(bestContactTime.patientTime, 'HH:mm')} onChange={this.onPatientBestContactTime} format="HH:mm"/>
        </div>
        <div className="time-section">
          <span>Caregiver</span>
          <TimePicker value={moment(bestContactTime.caregiverTime, 'HH:mm')} onChange={this.onCaregiverBestContactTime} format="HH:mm"/>
        </div>
      </div>
    );
  }

  render() {
    return (
      <div id="best-contact-time">
        <div className="button-section">
          {this.renderCalendarOverviewButton()}
          {this.renderSaveButton()}
        </div>
        <fieldset>
          <legend>{Msg.BEST_CONTACT_TIME_LABEL}</legend>
          {this.renderTimePickers()}
        </fieldset>
      </div>
    );
  }
};

const mapStateToProps = ({ bestContactTime }: IRootState) => (bestContactTime);

const mapDispatchToProps = ({
  getBestContactTime,
  postBestContactTime,
  updateBestConstactTime
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(BestContactTime);
