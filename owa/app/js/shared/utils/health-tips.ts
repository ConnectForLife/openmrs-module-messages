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

export const getHealthTipConfig = () => {
    return {
        'HT_CONTACT_TRACING': getIntl().formatMessage({ id: 'MESSAGES_HT_CONTACT_TRACING_LABEL', defaultMessage: Default.HT_CONTACT_TRACING_LABEL }),
        'HT_CAREGIVER': getIntl().formatMessage({ id: 'MESSAGES_HT_CAREGIVER_LABEL', defaultMessage: Default.HT_CAREGIVER_LABEL }),
        'HT_SIDE_EFFECTS': getIntl().formatMessage({ id: 'MESSAGES_HT_SIDE_EFFECTS_LABEL', defaultMessage: Default.HT_SIDE_EFFECTS_LABEL }),
        'HT_TREATMENT': getIntl().formatMessage({ id: 'MESSAGES_HT_TREATMENT_LABEL', defaultMessage: Default.HT_TREATMENT_LABEL }),
        'HT_PREVENTION': getIntl().formatMessage({ id: 'MESSAGES_HT_PREVENTION_LABEL', defaultMessage: Default.HT_PREVENTION_LABEL }),
        'HT_SPREAD': getIntl().formatMessage({ id: 'MESSAGES_HT_SPREAD_LABEL', defaultMessage: Default.HT_SPREAD_LABEL })
    };
};
