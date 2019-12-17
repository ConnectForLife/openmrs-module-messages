import React from 'react';
import { Button } from 'react-bootstrap';
import * as Msg from '../../shared/utils/messages';
import { IRootState } from '../../reducers';
import { connect } from 'react-redux';
import { getBestContactTime, postBestContactTime, updateBestConstactTime } from '../../reducers/best-contact-time.reducer';
import { history } from '../../config/redux-store';
import './best-contact-time.scss';
import { TimePicker } from 'antd';
import 'antd/dist/antd.css';
import { IBestContactTime } from '../../shared/model/best-contact-time.model';
import _ from 'lodash';
import moment, { Moment } from 'moment';
import { IActor } from '../../shared/model/actor.model';

interface IBestContactTimeProps extends DispatchProps, StateProps {
  patientId: number,
  patientUuid: string
};

interface IBestContactTimeState {
};

class BestContactTime extends React.PureComponent<IBestContactTimeProps, IBestContactTimeState> {

  constructor(props: IBestContactTimeProps) {
    super(props);
  }

  componentDidMount() {
    this.refreshBestContactTimeList();
  }

  componentDidUpdate(prevprops) {
    if (this.props.actorResultList !== prevprops.actorResultList) {
      this.refreshBestContactTimeList();
    }
  }

  refreshBestContactTimeList = () => {
    const personIds: Array<number> = [];
    personIds.push(this.props.patientId);
    this.props.actorResultList.forEach(e => personIds.push(e.actorId));
    this.props.getBestContactTime(personIds);
  }

  handleCalendarOverview = () => {
    const { patientId, patientUuid } = this.props;
    history.push(`/messages/${patientId}&patientUuid=${patientUuid}`);
  }

  handleSave = () => {
    if (!!this.props.bestContactTimes && !!this.props.patientId) {
      this.props.postBestContactTime(this.props.bestContactTimes);
    }
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

  onTimeChange = (time: Moment, timeString: string, personId: number | null) => {
    let contactTimes: Array<IBestContactTime> = _.cloneDeep(this.props.bestContactTimes);
    const contactTime = _.find(contactTimes, (e) => e.personId === personId);
    if (!!contactTime) {
      contactTime.time = timeString;
      this.props.updateBestConstactTime(contactTimes);
    }
  }

  getTimeValue = (stringDate) => {
    var date = moment(stringDate, 'HH:mm');
    return stringDate && date.isValid() ? date : undefined;
  };

  renderTimePickers() {
    const { bestContactTimes, actorResultList, patientId } = this.props;
    return (
      <div className="sections">
        {bestContactTimes.map((e, i) => {
          let label: string = `Person ${e.personId}`;
          if (e.personId === patientId) {
            label = 'Patient';
          } else {
            const actor: IActor | undefined = _.find(actorResultList, (a) => a.actorId === e.personId);
            if (!!actor) {
              label = `${actor.actorTypeName} - ${actor.actorName}`;
            }
          }
          return (
            <div className="time-section">
              <span>{label}</span>
              <TimePicker
                value={this.getTimeValue(e.time)}
                onChange={(date, dateString) => this.onTimeChange(date, dateString, e.personId)}
                format="HH:mm" />
            </div>
          )
        })}
      </div>
    );
  }

  render() {
    const { loading } = this.props;
    return (
      <div id="best-contact-time">
        <div className="button-section">
          {this.renderCalendarOverviewButton()}
          {this.renderSaveButton()}
        </div>
        <fieldset>
          <legend>{Msg.BEST_CONTACT_TIME_LABEL}</legend>
          {!loading && this.renderTimePickers()}
        </fieldset>
      </div>
    );
  }
};

const mapStateToProps = ({ bestContactTime, actor }: IRootState) => ({
  bestContactTimes: bestContactTime.bestContactTimes,
  actorResultList: actor.actorResultList,
  loading: bestContactTime.bestContactTimesLoading || actor.actorResultListLoading
});;

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
