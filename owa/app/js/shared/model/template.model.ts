import _ from 'lodash';
import { ITemplateField } from './template-field.model';

export interface ITemplate {
  id: number | null;
  name: string | null;
  templateFields: Array<ITemplateField>;
  // outboxQuery and calendarViewQuery are skipped for now
}

export const getDefaultValue = (): ITemplate => ({
  id: null,
  name: null,
  templateFields: [] as Array<ITemplateField>
});
