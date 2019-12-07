import { IServiceResult } from "./service-result.model";
import { Moment } from 'moment';

export interface IServiceResultList {
  patientId: number | null;
  actorId: number | null;
  serviceId: number | null;
  serviceName: string;
  startDate: Moment | null;
  endDate: Moment | null;
  results: ReadonlyArray<IServiceResult>
}

export const getDefaultValue = (): IServiceResultList => ({
  patientId: null,
  actorId: null,
  serviceId: null,
  startDate: null,
  endDate: null,
  results: [],
  serviceName: ''
});
