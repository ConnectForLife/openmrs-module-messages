import React from 'react';
import { Button } from 'react-bootstrap';
import * as Msg from '../../shared/utils/messages';
import { IRootState } from '../../reducers';
import { RouteComponentProps } from 'react-router';
import { connect } from 'react-redux';
import { getBestContactTime, putBestContactTime } from '../../reducers/best-contact-time.reducer';
import { history } from '../../config/redux-store';
import './best-contact-time.scss';

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
    //TODO: CFLM-377: this.props.getBestContactTime(this.props.match.params.patientId);
  }

  handleCalendarOverview = () => {
    const patientId = this.props.patientId;
    history.push(`/messages/${patientId}`);
  }

  handleSave = () => {
    alert('Not yet supported');
    //TODO: CFLM-377: this.props.putBestContactTime(this.props.match.params.patientId);
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

  renderTimePickers() {
    return (
      <div className="sections">
        <div className="time-section">
          <span>Patient</span>
          <input type="time"></input>
        </div>
        <div className="time-section">
          <span>Caregiver</span>
          <input type="time"></input>
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
  putBestContactTime,
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(BestContactTime);
