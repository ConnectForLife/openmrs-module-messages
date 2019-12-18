import moment, { Moment } from "moment";

export interface IBestContactTime {
  personId: number,
  time?: Moment
}

export const mapUtcStringTimetoLocalTime = (element) => {
  const localTime = !!element.time ? moment.utc(element.time, 'HH:mm').local() : undefined;
  return <IBestContactTime>{
    personId: element.personId,
    time: localTime
  }
}

export const mapLocalTimetoUtcStringTime = (element) => {
  const utcStringTime = !!element.time ? moment.utc(element.time).format('HH:mm') : null;
  return {
    personId: element.personId,
    time: utcStringTime
  }
}