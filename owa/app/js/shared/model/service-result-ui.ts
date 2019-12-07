import _ from 'lodash'

import { ObjectUI } from './object-ui';
import { IServiceResult } from './service-result.model';
import { ServiceStatus } from '../enums/service-status';
import { Moment } from 'moment';
import moment from 'moment';

export class ServiceResultUI extends ObjectUI<IServiceResult> implements IServiceResult {
  executionDate: Moment | null;;
  messageId: Object;
  channelId: number | null;
  serviceStatus: ServiceStatus;

  constructor(model: IServiceResult) {
    super(model);
    this.executionDate = model.executionDate ? moment(model.executionDate) : null;
  }
}
