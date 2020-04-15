import React from 'react';
import { Button } from 'react-bootstrap';
import * as Msg from '../../shared/utils/messages';
import { IRootState } from '../../reducers';
import { connect } from 'react-redux';
import { getBestContactTime, postBestContactTime, updateBestConstactTime } from '../../reducers/best-contact-time.reducer';
import { getBestContactTimes } from '../../reducers/admin-settings.reducer';
import { history } from '../../config/redux-store';
import './best-contact-time.scss';
import { TimePicker } from 'antd';
import 'antd/dist/antd.css';
import { IBestContactTime } from '../../shared/model/best-contact-time.model';
import _ from 'lodash';
import { Moment } from 'moment';
import { IActor } from '../../shared/model/actor.model';
import { IContactTime } from '../../shared/model/contact-time.model';
import {DashboardType} from "../../shared/model/dashboard-type";

interface IBestContactTimeProps extends DispatchProps, StateProps {
  patientId: number,
  patientUuid: string,
  onSaveClickCallback?: Function,
  dashboardType: DashboardType
};

interface IBestContactTimeState {
};

class BestContactTime extends React.PureComponent<IBestContactTimeProps, IBestContactTimeState> {

  constructor(props: IBestContactTimeProps) {
    super(props);
  }

  private isPatient() {
    return !!this.props.dashboardType ? this.props.dashboardType.toUpperCase() === DashboardType.PATIENT : true;
  }

  componentDidMount() {
    this.props.getBestContactTimes();
    this.refreshBestContactTimeList();
  }

  componentDidUpdate(prevProps) {
    if (this.props.actorResultList !== prevProps.actorResultList) {
      this.refreshBestContactTimeList();
    }
  }

  refreshBestContactTimeList = () => {
    const personIds: Array<number> = [];
    personIds.push(this.props.patientId);
    if (this.isPatient()) {
      this.props.actorResultList.forEach(e => personIds.push(e.actorId));
    }
    this.props.getBestContactTime(personIds);
  }

  handleCalendarOverview = () => {
    const { patientId, patientUuid, dashboardType } = this.props;
    history.push(`/messages/${dashboardType}/${patientId}&patientUuid=${patientUuid}`);
  }

  handleSave = () => {
    if (!!this.props.bestContactTimes && !!this.props.patientId) {
      this.props.postBestContactTime(this.applyDefaultValuesIfNeeded(this.props.bestContactTimes));
      if (!!this.props.onSaveClickCallback) {
        this.props.onSaveClickCallback();
      }
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
      contactTime.time = time;
      this.props.updateBestConstactTime(contactTimes);
    }
  }

  applyDefaultValuesIfNeeded = (bestContactTimes: Array<IBestContactTime>): Array<IBestContactTime> => {
    const clonedValues = _.clone(bestContactTimes);
    clonedValues.map(contactTime => {
      if (!contactTime.time) {
        contactTime.time = this.getDefaultValue(contactTime);
      }
    });
    return clonedValues;
  }

  getDefaultValue = (contactTime: IBestContactTime): Moment | undefined => {
    let defaultValue;
    if (this.isActorPatient(contactTime)) {
      defaultValue = this.findDefaultValueForActor('global');
    } else {
      defaultValue = this.getDefaultValueForActor(contactTime.personId);
    }
    if (defaultValue) {
      return defaultValue.time;
    }
  }

  getDefaultValueForPatient = (): IContactTime | undefined => this.findDefaultValueForActor('global');

  getDefaultValueForActor = (personId: number): IContactTime | undefined => {
    const actorId: number = _.findIndex(this.props.actorResultList, (a) => a.actorId === personId);
    if (actorId !== -1) {
      return this.findDefaultValueForActor(this.props.actorResultList[actorId].relationshipTypeUuid);
    }
  }

  findDefaultValueForActor = (actor?: string) => _.find(this.props.defaultBestContactTimes, a => a.actor === actor);

  isActorPatient = (contactTime: IBestContactTime) => contactTime.personId === this.props.patientId;

  renderTimePickers() {
    const bestContactTimes = this.applyDefaultValuesIfNeeded(this.props.bestContactTimes);
    return (
      <div className="sections">
        {bestContactTimes.map((e, i) => {
          let label: string = `Person ${e.personId}`;
          if (!this.isPatient()) {
            label = Msg.CAREGIVER_ROLE;
          } else if (this.isActorPatient(e)) {
            label = Msg.PATIENT_ROLE;
          } else {
            const actor: IActor | undefined = _.find(this.props.actorResultList, (a) => a.actorId === e.personId);
            if (!!actor) {
              label = `${actor.actorTypeName} - ${actor.actorName}`;
            }
          }
          return (
            <div className="time-section" key={`time-section-${i}`}>
              <span>{label}</span>
              <TimePicker
                value={e.time}
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

const mapStateToProps = ({ bestContactTime, actor, adminSettings }: IRootState) => ({
  bestContactTimes: bestContactTime.bestContactTimes,
  actorResultList: actor.actorResultList,
  loading: bestContactTime.bestContactTimesLoading || actor.actorResultListLoading,
  defaultBestContactTimes: adminSettings.defaultBestContactTimes
});;

const mapDispatchToProps = ({
  getBestContactTime,
  postBestContactTime,
  updateBestConstactTime,
  getBestContactTimes
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(BestContactTime);
