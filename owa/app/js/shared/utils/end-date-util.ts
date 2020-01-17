import { format, startOfDay, parse, addDays, addMonths, addYears } from 'date-fns';
import _ from 'lodash';

import { TemplateFieldType } from '../model/template-field-type';
import { InputTypeEnum } from '../../components/patient-template/form/radio-wrapper/parsable-input';
import { ISO_DATE_FORMAT } from './messages';
import { TemplateFieldUI } from '../model/template-field-ui';
import { TimeType } from '../enums/time-type';
import { TemplateFieldDefaultValue } from '../model/template-field-default-value.model';

const TIME_TYPE_INDEX = 0;
const UNITS_INDEX = 1;
const SEPARATOR = '|';

export const prepareFieldValues = (templateFieldsOld: Array<TemplateFieldUI>, 
    relationshipTypeId?: number,
    relationshipDirection?: string): Array<TemplateFieldUI> => {
  const templateFields = _.cloneDeep(templateFieldsOld);
  const endOfMessagesField = templateFields.find(tf =>tf.type === TemplateFieldType.END_OF_MESSAGES);
  if (!endOfMessagesField) {
    return templateFields;
  }
  const otherDefaultValue = getDefaultValue(endOfMessagesField, relationshipTypeId, relationshipDirection);
  if (!isOtherValue(otherDefaultValue)) {
    return templateFields
  }
  const defaultValueString = otherDefaultValue.replace(`${InputTypeEnum.OTHER}${SEPARATOR}`, '');

  const endDate: Date = calculateEndDate(getStartDate(templateFields, relationshipTypeId, relationshipDirection), defaultValueString);

  templateFields.forEach(tf => {
    if (tf.type === TemplateFieldType.END_OF_MESSAGES) {
      if (!!relationshipTypeId && !!relationshipDirection) {
        const defaultValue = findDefaultValue(tf.defaultValues, relationshipTypeId, relationshipDirection, tf.id!)
          if (!!defaultValue) {
            defaultValue.defaultValue = formatEndDateValue(endDate);
          }
      } else {
        tf.defaultValue = formatEndDateValue(endDate);
      }
    }
    return tf;
  })

  return templateFields;
}

const findDefaultValue = (defaultValues: ReadonlyArray<TemplateFieldDefaultValue>,
  relationshipTypeId: number, relationshipDirection: string, templateFieldId: number) =>
    defaultValues.find(f => f.direction === relationshipDirection
      && f.relationshipTypeId === relationshipTypeId
      && f.templateFieldId === templateFieldId);


const formatEndDateValue = (endDate: Date): string => `${InputTypeEnum.DATE_PICKER}${SEPARATOR}${format(endDate, ISO_DATE_FORMAT)}`;

const getDefaultValue = (field: TemplateFieldUI, relationshipTypeId: number | undefined, relationshipDirection: string | undefined) => {
  if (isPatient(relationshipTypeId)) {
    return field.defaultValue;
  }
  const defaultValue = findDefaultValue(field.defaultValues, relationshipTypeId!, relationshipDirection!, field.id!);
  return !!defaultValue && !!defaultValue.defaultValue ? defaultValue.defaultValue : '';
}

const isPatient = (relationshipTypeId: number | undefined): boolean => !relationshipTypeId;

const getStartDate = (templateFields: Array<TemplateFieldUI>, relationshipTypeId: number | undefined, relationshipDirection: string | undefined): Date => {
  const startOfMessagesField = templateFields.find(tf => tf.type === TemplateFieldType.START_OF_MESSAGES);
  if (!startOfMessagesField) {
    return startOfDay(Date.now());
  }
  const defaultValue = getDefaultValue(startOfMessagesField, relationshipTypeId, relationshipDirection);
  if (!defaultValue) {
    return startOfDay(Date.now());
  }
  return parse(defaultValue);
}

const calculateEndDate = (startDate: Date, defaultEndDateValue: string): Date => {
  const chain: string[] = defaultEndDateValue.split(SEPARATOR);
  const timeType: TimeType = TimeType[chain[TIME_TYPE_INDEX]];
  const units: number = parseInt(chain[UNITS_INDEX]);
  switch(timeType) {
    case TimeType.DAY:
      return addDays(startDate, units);
    case TimeType.MONTH:
      return addMonths(startDate, units);
    case TimeType.YEAR:
      return addYears(startDate, units);
    default:
      return startOfDay(Date.now());
  }
}

const isOtherValue = (defaultValue: string) => {
  return defaultValue.startsWith(InputTypeEnum.OTHER)
}
