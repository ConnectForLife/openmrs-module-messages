import { Moment } from "moment";
import { getUtcTimeFromString, getLocalTimeFromString } from '../utils/time-util';
import _ from 'lodash';

export interface IContactTime {
  time?: Moment
}

export const mapFromRequest = (element) => {
  const result = _.clone(element);
  result.time = getLocalTimeFromString(element.time);
  return result;
}

export const mapToRequest = (element) => {
  const result = _.clone(element);
  result.time = getUtcTimeFromString(element.time);
  return result;
}
