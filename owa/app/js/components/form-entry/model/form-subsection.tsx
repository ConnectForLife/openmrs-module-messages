/*
 *  This Source Code Form is subject to the terms of the Mozilla Public License,
 *  v. 2.0. If a copy of the MPL was not distributed with this file, You can
 *  obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 *  the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *  <p>
 *  Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 *  graphic logo is a trademark of OpenMRS Inc.
 */

import { ReactFragment } from 'react';
import { SelectCallback } from 'react-bootstrap';

export default class FormSubSection {
    name: string;
    body: ReactFragment;
    onSelect?: SelectCallback;
    isValid: boolean;
    isTouched: boolean;

    constructor(name: string, isValid: boolean, isTouched: boolean, body: ReactFragment, onSelect?: SelectCallback) {
        this.name = name;
        this.isValid = isValid;
        this.isTouched = isTouched;
        this.body = body;
        this.onSelect = onSelect;
    }
}
