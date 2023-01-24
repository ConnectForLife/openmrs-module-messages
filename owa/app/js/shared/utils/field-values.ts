/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import * as Default from './messages';
import { getIntl } from '@openmrs/react-components/lib/components/localization/withLocalization';

export const getServiceTypeValues = (possibleValues: Array<string>, locale: string | undefined) => {
    return possibleValues.map(v => getIntl(locale).formatMessage({ id: 'MESSAGES_SERVICE_TYPE_' + v.toUpperCase().replace(' ', '_'), defaultMessage: v }));
};

export const getDayOfWeekValues = (locale: string | undefined) => {
    return [getIntl(locale).formatMessage({ id: 'MESSAGES_MONDAY', defaultMessage: Default.MONDAY }),
    getIntl(locale).formatMessage({ id: 'MESSAGES_TUESDAY', defaultMessage: Default.TUESDAY }),
    getIntl(locale).formatMessage({ id: 'MESSAGES_WEDNESDAY', defaultMessage: Default.WEDNESDAY }),
    getIntl(locale).formatMessage({ id: 'MESSAGES_THURSDAY', defaultMessage: Default.THURSDAY }),
    getIntl(locale).formatMessage({ id: 'MESSAGES_FRIDAY', defaultMessage: Default.FRIDAY }),
    getIntl(locale).formatMessage({ id: 'MESSAGES_SATURDAY', defaultMessage: Default.SATURDAY }),
    getIntl(locale).formatMessage({ id: 'MESSAGES_SUNDAY', defaultMessage: Default.SUNDAY })];
};

export const getMessagingFrequencyDailyOrWeeklyOrMonthlyValues = (locale: string | undefined) => {
    return [getIntl(locale).formatMessage({ id: 'MESSAGES_DAILY', defaultMessage: Default.DAILY }),
    getIntl(locale).formatMessage({ id: 'MESSAGES_WEEKLY', defaultMessage: Default.WEEKLY }),
    getIntl(locale).formatMessage({ id: 'MESSAGES_MONTHLY', defaultMessage: Default.MONTHLY })];
};

export const getMessagingFrequencyWeeklyOrMonthlyValues = (locale: string | undefined) => {
    return [getIntl(locale).formatMessage({ id: 'MESSAGES_WEEKLY', defaultMessage: Default.WEEKLY }),
    getIntl(locale).formatMessage({ id: 'MESSAGES_MONTHLY', defaultMessage: Default.MONTHLY })];
};
