/*
 *  This Source Code Form is subject to the terms of the Mozilla Public License,
 *  v. 2.0. If a copy of the MPL was not distributed with this file, You can
 *  obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 *  the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *  <p>
 *  Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 *  graphic logo is a trademark of OpenMRS Inc.
 */

import React from 'react'
import FullCalendar from '@fullcalendar/react'
import dayGridPlugin from '@fullcalendar/daygrid'
import momentPlugin from '@fullcalendar/moment'
import momentTimezonePlugin from '@fullcalendar/moment-timezone'
import './calendar-overview.scss'

interface ICalendarProps {
  events: ReadonlyArray<{}>;
  timezone?: string
  dateRangeChangedCallback(startDate: Date, endDate: Date): void;
}

class Calendar extends React.Component<ICalendarProps> {

  private createIcon = (status: string) => {
    const newElement = document.createElement('i');
    newElement.classList.add(
      status === 'FAILED'
        ? "icon-ban-circle"
        : status === 'DELIVERED'
          ? "icon-ok-circle"
          : "icon-circle-blank");
    newElement.classList.add(status === 'FAILED' ? "red" : status === 'DELIVERED' ? "green" : "gray");
    newElement.classList.add("status-icon");
    return newElement;
  };

  private buildSubEvent(data: any, eventContainer: ChildNode) {
    const subEvent = document.createElement('span');
    subEvent.innerText = data.name;
    subEvent.classList.add('fc-sub-event');
    subEvent.appendChild(this.createIcon(data.status));
    eventContainer.appendChild(subEvent);
  }

  private removeExistingLabel(eventContainer: ChildNode) {
    eventContainer.removeChild(eventContainer.lastChild!);
  }

  render = () => {
    return (
      <div className="calendar">
        <FullCalendar
          defaultTimedEventDuration='00:00:01'
          forceEventDuration={ true }
          defaultView="dayGridMonth"
          plugins={[dayGridPlugin, momentPlugin, momentTimezonePlugin]}
          timeZone={ !!this.props.timezone ? this.props.timezone : 'local' }
          header={{ left: 'prev title next dayGridMonth,dayGridWeek,dayGridDay', center: '', right: '' }}
          events={this.props.events}
          datesRender={(info) => this.props.dateRangeChangedCallback(info.view.activeStart, info.view.activeEnd)}
          eventRender={(info) => {
            let eventContainer = info.el.firstChild!;
            if (info.event.extendedProps.isDisabled === true) {
              info.el.classList.add("fc-event-disabled");
            }
            this.removeExistingLabel(eventContainer);
            info.event.extendedProps.services.forEach((service: any) => {
              this.buildSubEvent(service, eventContainer);
            });
          }}
          height="auto"
          eventTimeFormat="HH:mm"
        />
      </div>
    );
  };
}

export default Calendar;