
import { startOfDay, format, parse } from 'date-fns';
import { ISO_DATE_FORMAT } from '@bit/soldevelo-omrs.cfl-components.date-util/constants';

const defaultValues = {
  START_OF_MESSAGES: format(startOfDay(Date.now()), ISO_DATE_FORMAT)
}

const isValueAvailable = (value: string) => {
  return !!value;
}

const prepareDefaultValue = (currentDefault: string, fieldType: string): string => {
  if (!isValueAvailable(currentDefault) && isValueAvailable(defaultValues[fieldType])) {
    return defaultValues[fieldType];
  } else {
    return currentDefault;
  }
}

export default prepareDefaultValue;
