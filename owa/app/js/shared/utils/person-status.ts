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

export const DEACTIVATED_KEY = 'DEACTIVATED';
export const ACTIVATED_KEY = 'ACTIVATED';
export const MISSING_VALUE_KEY = 'MISSING_VALUE';
export const NO_CONSENT_KEY = 'NO_CONSENT';

export const getPersonStatusConfig = (locale: string | undefined) => {
    return {
        NO_CONSENT: {
          value: NO_CONSENT_KEY,
          label: getIntl(locale).formatMessage({ id: 'MESSAGES_PERSON_STATUS_NO_CONSENT', defaultMessage: Default.PERSON_STATUS_NO_CONSENT })
        },
        ACTIVATED: {
          value: ACTIVATED_KEY,
          label: getIntl(locale).formatMessage({ id: 'MESSAGES_PERSON_STATUS_ACTIVATED', defaultMessage: Default.PERSON_STATUS_ACTIVATED })
        },
        DEACTIVATED: {
          value: DEACTIVATED_KEY,
          label: getIntl(locale).formatMessage({ id: 'MESSAGES_PERSON_STATUS_DEACTIVATED', defaultMessage: Default.PERSON_STATUS_DEACTIVATED })
        },
        MISSING_VALUE: {
          value: MISSING_VALUE_KEY,
          label: getIntl(locale).formatMessage({ id: 'MESSAGES_PERSON_STATUS_MISSING_VALUE', defaultMessage: Default.PERSON_STATUS_MISSING_VALUE })
        }
    };
};
