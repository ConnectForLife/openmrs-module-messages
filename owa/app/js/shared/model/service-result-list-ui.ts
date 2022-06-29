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
import { IServiceResultList } from './service-result-list.model';
import { IServiceResult } from './service-result.model';
import { ServiceResultUI } from './service-result-ui';
import { Moment } from 'moment';
import moment from 'moment';

export class ServiceResultListUI extends ObjectUI<IServiceResultList> implements IServiceResultList {
    patientId: number | null;
    actorId: number | null;
    serviceId: number | null;
    startDate: Moment | null;;
    endDate: Moment | null;;
    results: ReadonlyArray<IServiceResult>
    serviceName: string;
    
  constructor(model: IServiceResultList) {
    super(model);
    this.startDate = model.startDate ? moment(model.startDate) : null;
    this.endDate = model.endDate ? moment(model.endDate) : null;
    this.results = _.map(model.results,
      templateField => new ServiceResultUI(templateField));
  }

  static fromModel(model: IServiceResultList) {
    return new ServiceResultListUI(model);
  }
}
