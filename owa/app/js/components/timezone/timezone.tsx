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
import { IRootState } from '../../reducers';
import {
  getTimezone,
} from '../../reducers/timezone.reducer';
import * as Default from '../../shared/utils/messages';
import { LocalizedMessage } from '@openmrs/react-components';

interface ITimezoneProps extends DispatchProps, StateProps {
}

interface ITimezoneState {
}

class Timezone extends React.PureComponent<ITimezoneProps, ITimezoneState> {

  constructor(props: ITimezoneProps) {
    super(props);
  }

  componentDidMount() {
    this.props.getTimezone();
  };

  private isLocalTimezoneDifferent = (): boolean => {
    return !this.props.timezoneLoading && (this.getLocalTimezone() !== this.props.timezone);
  };

  private getLocalTimezone = (): string => {
    return Intl.DateTimeFormat().resolvedOptions().timeZone;
  };

  private renderDifferentTimezoneNotification = () => {
    return (
      <div className="note-container">
        <div className="note warning">
          <div className="note-content">
            <span className="toast-item-image toast-item-image-alert" />
            <div className="message">
              <LocalizedMessage id="MESSAGES_DIFFERENT_TIMEZONE" defaultMessage={Default.DIFFERENT_TIMEZONE} />{this.props.timezone}
            </div>
          </div>
        </div>
      </div>
    );
  };

  render() {
    return (
      <>
        {this.isLocalTimezoneDifferent() && this.renderDifferentTimezoneNotification()}
      </>
    );
  };
}

const mapStateToProps = ({ timezone }: IRootState) => ({
  timezone: timezone.timezone,
  timezoneLoading: timezone.timezoneLoading
});

const mapDispatchToProps = ({
  getTimezone,
});

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Timezone);
