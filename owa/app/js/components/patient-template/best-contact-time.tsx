import React from 'react';
import { Button } from 'react-bootstrap';
import * as Msg from '../../shared/utils/messages';
import { IRootState } from '../../reducers';
import { RouteComponentProps } from 'react-router';
import { connect } from 'react-redux';
import { getBestContactTime, putBestContactTime } from '../../reducers/best-contact-time.reducer';
import './best-contact-time.scss';

interface IBestContactTimeProps extends DispatchProps, StateProps, RouteComponentProps<{ patientId: string }> {
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

  handleSave = () => {
    alert('Not yet supported');
    //TODO: CFLM-377: this.props.putBestContactTime(this.props.match.params.patientId);
  }

  renderTimePickers = () =>
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

  renderSaveButton = () =>
    <Button
      className="btn btn-success btn-md pull-right"
      onClick={this.handleSave}>
      {Msg.SAVE_BUTTON_LABEL}
    </Button>

  render() {
    return (
      <div>
        <fieldset id="best-contact-time-fieldset">
          <legend>Best Contact Time</legend>
          {this.renderTimePickers()}
          {this.renderSaveButton()}
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
