/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import * as Default from './messages';
import { getIntl } from '@openmrs/react-components/lib/components/localization/withLocalization';

export const getServiceTypeValues = () => {
    return [getIntl().formatMessage({ id: 'MESSAGES_CALL_LABEL', defaultMessage: Default.CALL_LABEL }),
    getIntl().formatMessage({ id: 'MESSAGES_SMS_LABEL', defaultMessage: Default.SMS_LABEL }),
    getIntl().formatMessage({ id: 'MESSAGES_DEACTIVATE_SERVICE_LABEL', defaultMessage: Default.DEACTIVATE_SERVICE_LABEL })];
};

export const getDayOfWeekValues = () => {
    return [getIntl().formatMessage({ id: 'MESSAGES_MONDAY', defaultMessage: Default.MONDAY }),
    getIntl().formatMessage({ id: 'MESSAGES_TUESDAY', defaultMessage: Default.TUESDAY }),
    getIntl().formatMessage({ id: 'MESSAGES_WEDNESDAY', defaultMessage: Default.WEDNESDAY }),
    getIntl().formatMessage({ id: 'MESSAGES_THURSDAY', defaultMessage: Default.THURSDAY }),
    getIntl().formatMessage({ id: 'MESSAGES_FRIDAY', defaultMessage: Default.FRIDAY }),
    getIntl().formatMessage({ id: 'MESSAGES_SATURDAY', defaultMessage: Default.SATURDAY }),
    getIntl().formatMessage({ id: 'MESSAGES_SUNDAY', defaultMessage: Default.SUNDAY })];
};

export const getMessagingFrequencyDailyOrWeeklyOrMonthlyValues = () => {
    return [getIntl().formatMessage({ id: 'MESSAGES_DAILY', defaultMessage: Default.DAILY }),
    getIntl().formatMessage({ id: 'MESSAGES_WEEKLY', defaultMessage: Default.WEEKLY }),
    getIntl().formatMessage({ id: 'MESSAGES_MONTHLY', defaultMessage: Default.MONTHLY })];
};

export const getMessagingFrequencyWeeklyOrMonthlyValues = () => {
    return [getIntl().formatMessage({ id: 'MESSAGES_WEEKLY', defaultMessage: Default.WEEKLY }),
    getIntl().formatMessage({ id: 'MESSAGES_MONTHLY', defaultMessage: Default.MONTHLY })];
};
