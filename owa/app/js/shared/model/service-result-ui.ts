/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import _ from 'lodash'

import { ObjectUI } from './object-ui';
import { IServiceResult } from './service-result.model';
import { ServiceStatus } from '../enums/service-status';
import { Moment } from 'moment';
import moment from 'moment';

export class ServiceResultUI extends ObjectUI<IServiceResult> implements IServiceResult {
  executionDate: Moment | null;
  messageId: Object;
  channelId: number | null;
  serviceStatus: ServiceStatus;
  additionalParams: { [name: string]: string };


  constructor(model: IServiceResult) {
    super(model);
    this.executionDate = model.executionDate ? moment(model.executionDate) : null;
  }
}
