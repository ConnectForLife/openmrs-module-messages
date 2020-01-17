import { TimeType } from '../enums/time-type';

export interface IOption {
  type: TimeType | null;
  value: number | null;
  label: string;
}

export const getEmptyOption = (): IOption => ({
  type: null,
  value: null,
  label: ''
});