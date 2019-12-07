import { ServiceStatus } from "../enums/service-status";
import { Moment } from 'moment';

export interface IServiceResult {
  executionDate: Moment | null;
  messageId: Object;
  channelId: number | null;
  serviceStatus: ServiceStatus
}

export const getDefaultValue = (): IServiceResult => ({
  executionDate: null,
  messageId: {},
  channelId: null,
  serviceStatus: ServiceStatus.FUTURE
});

