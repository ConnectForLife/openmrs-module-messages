/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import React from 'react';
import { connect } from 'react-redux';

import { updateTemplate, updateBestContactTime } from '../../reducers/admin-settings.reducer';
import { IDefaultBestContactTime } from '../../shared/model/default-best-contact-time.model';
import { IActorType } from '../../shared/model/actor-type.model';
import { TimePicker } from 'antd';
import _ from 'lodash';
import moment, { Moment } from 'moment';
import * as Default from '../../shared/utils/messages';
import { LocalizedMessage } from '@openmrs/react-components';
import './best-contact-time.scss';

interface IProps extends DispatchProps {
  bestContactTimes: ReadonlyArray<IDefaultBestContactTime>
  actorTypes: ReadonlyArray<IActorType>,
  loading: boolean
}

class BestContactTime extends React.Component<IProps> {

  onTimeChange = (time: Moment, contactTimeId: number, actor: string) => {
    const updated = _.clone(this.props.bestContactTimes) as Array<IDefaultBestContactTime>;
    if (contactTimeId === -1) {
      updated.push({ actor, time });
    } else {
      updated[contactTimeId].time = time;
    }
    this.props.updateBestContactTime(updated);
  }

  renderTimePicker = (key: string, label: string, contactTimeId: number, defaultTime: Moment, actor: string) => {
    const contactTime = this.props.bestContactTimes[contactTimeId];
    return (
      <div className="time-section" key={key}>
        <span>{label}</span>
        <TimePicker
          value={contactTime ? contactTime.time : defaultTime}
          onChange={date => this.onTimeChange(date, contactTimeId, actor)}
          format="HH:mm" />
      </div>);
  }

  renderTimePickers = () => {
    const { bestContactTimes, actorTypes } = this.props;
    const patientContactTimeId = _.findIndex(bestContactTimes, a => a.actor === 'global');
    let defaultTime = moment();
    if (patientContactTimeId !== -1 && bestContactTimes[patientContactTimeId].time) {
      defaultTime = bestContactTimes[patientContactTimeId].time!;
    }
    return (
      <div className="sections">
        {this.renderTimePicker("patient-time-section", "Patient", patientContactTimeId, defaultTime, 'global')}
        {actorTypes.map((type, i) => {
          let contactTimeId = _.findIndex(bestContactTimes, a => a.actor === type.uuid);
          return (this.renderTimePicker(`time-section-${i}`, type.display, contactTimeId, defaultTime, type.uuid))
        })}
      </div>
    );
  }

  render = () => {
    const { loading } = this.props;
    return (
      <div id="best-contact-time">
        <fieldset>
          <legend><LocalizedMessage id="MESSAGES_BEST_CONTACT_TIME_LABEL" defaultMessage={Default.BEST_CONTACT_TIME_LABEL} /></legend>
          {!loading && this.renderTimePickers()}
        </fieldset>
      </div>
    );
  }
};

const mapDispatchToProps = ({
  updateTemplate,
  updateBestContactTime
});

type DispatchProps = typeof mapDispatchToProps;

export default connect(
  undefined,
  mapDispatchToProps
)(BestContactTime);
