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
