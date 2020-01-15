/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import React from 'react';
import { connect } from 'react-redux';

import { updateTemplate } from '../../reducers/admin-settings.reducer';
import { IDefaultBestContactTime } from '../../shared/model/default-best-contact-time.model';
import { IActorType } from '../../shared/model/actor-type.model';
import { TimePicker } from 'antd';
import _ from 'lodash';
import moment, { Moment } from 'moment';
import * as Msg from '../../shared/utils/messages';
import './best-contact-time.scss';

interface IProps extends DispatchProps {
  bestContactTimes: ReadonlyArray<IDefaultBestContactTime>
  actorTypes: ReadonlyArray<IActorType>,
  loading: boolean
}

class BestContactTime extends React.Component<IProps> {

  onTimeChange = (time: Moment, timeString: string, actor: IDefaultBestContactTime | undefined) => {
    // todo in CFLM-517: handle change
  }

  renderTimePickers = () => {
    const { bestContactTimes, actorTypes } = this.props;
    const patientContactTime = _.find(bestContactTimes, a => a.actor === 'global');
    return (
      <div className="sections">
        <div className="time-section" key={`patient-time-section`}>
          <span>{"Patient"}</span>
          <TimePicker
            value={patientContactTime ? patientContactTime.time : moment()}
            onChange={(date, dateString) => this.onTimeChange(date, dateString, patientContactTime)}
            format="HH:mm" />
        </div>
        {actorTypes.map((type, i) => {
          let contactTime = _.find(bestContactTimes, a => a.actor === type.uuid);
          return (
            <div className="time-section" key={`time-section-${i}`}>
              <span>{type.display}</span>
              <TimePicker
                value={contactTime ? contactTime.time : moment()}
                onChange={(date, dateString) => this.onTimeChange(date, dateString, contactTime)}
                format="HH:mm" />
            </div>
          )
        })}
      </div>
    );
  }

  render = () => {
    const { loading } = this.props;
    return (
      <div id="best-contact-time">
        <fieldset>
          <legend>{Msg.BEST_CONTACT_TIME_LABEL}</legend>
          {!loading && this.renderTimePickers()}
        </fieldset>
      </div>
    );
  }
};

const mapDispatchToProps = ({
  updateTemplate,
});

type DispatchProps = typeof mapDispatchToProps;

export default connect(
  undefined,
  mapDispatchToProps
)(BestContactTime);
