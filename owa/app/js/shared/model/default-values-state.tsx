import { IPatientTemplate } from './patient-template.model';
import IMessageDetailsWrapped from './message-details';

export interface IDefaultValuesState {
  defaultValuesUsed: boolean | undefined;
  allValuesDefault: boolean | undefined;
  lackingPatientTemplates: Array<IPatientTemplate>;
  details: IMessageDetailsWrapped | undefined;
}

export const getDefaultValue = (): IDefaultValuesState => ({
  defaultValuesUsed: undefined,
  allValuesDefault: undefined,
  lackingPatientTemplates: [],
  details: undefined
});
