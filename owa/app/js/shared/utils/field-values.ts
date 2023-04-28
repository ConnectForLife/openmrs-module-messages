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

export const getServiceTypeValues = (possibleValues: Array<string>, intl: any) => {
    return possibleValues.map(v => intl.formatMessage({ id: 'messages.serviceType.' + v.toUpperCase().replace(' ', '_') }));
};

export const getDayOfWeekValues = (intl: any) => {
    return [intl.formatMessage({ id: 'cfl.weekDay.Monday.fullName' }),
    intl.formatMessage({ id: 'cfl.weekDay.Tuesday.fullName' }),
    intl.formatMessage({ id: 'cfl.weekDay.Wednesday.fullName' }),
    intl.formatMessage({ id: 'cfl.weekDay.Thursday.fullName' }),
    intl.formatMessage({ id: 'cfl.weekDay.Friday.fullName' }),
    intl.formatMessage({ id: 'cfl.weekDay.Saturday.fullName' }),
    intl.formatMessage({ id: 'cfl.weekDay.Sunday.fullName' })];
};

export const getMessagingFrequencyDailyOrWeeklyOrMonthlyValues = (intl: any) => {
    return [intl.formatMessage({ id: 'messages.daily' }),
    intl.formatMessage({ id: 'messages.weekly' }),
    intl.formatMessage({ id: 'messages.monthly' })];
};

export const getMessagingFrequencyWeeklyOrMonthlyValues = (intl: any) => {
    return [intl.formatMessage({ id: 'messages.weekly' }),
    intl.formatMessage({ id: 'messages.monthly' })];
};
