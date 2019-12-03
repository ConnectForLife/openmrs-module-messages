import _ from 'lodash';
import { TemplateFieldType } from './template-field-type';

export interface ITemplateField {
  id: number | null;
  name: string;
  mandatory: boolean;
  defaultValue: string;
  type: TemplateFieldType;
  // value_concept is skipped for now
}

export const getDefaultValue = (): ITemplateField => ({
  id: null,
  name: '',
  mandatory: true,
  defaultValue: '',
  type: TemplateFieldType.DAY_OF_WEEK
});
