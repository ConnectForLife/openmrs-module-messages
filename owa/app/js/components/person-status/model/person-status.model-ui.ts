/*
 *  This Source Code Form is subject to the terms of the Mozilla Public License,
 *  v. 2.0. If a copy of the MPL was not distributed with this file, You can
 *  obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 *  the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *  <p>
 *  Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 *  graphic logo is a trademark of OpenMRS Inc.
 */

 import { IPersonStatus } from "./person-status.model";
import { ObjectUI } from "../../shared/object-ui";

export class PersonStatusUI extends ObjectUI<IPersonStatus> implements IPersonStatus {
    title?: string;
    value: string;
    reason?: string;
    personId?: string;
    style?: string;
    styleObject?: Object;

    constructor(model: IPersonStatus) {
        super(model);
        this.value = model.value;
        this.styleObject = model.style ? this.parseStyles(model.style) : undefined;
    }

    static fromModel(model: IPersonStatus): PersonStatusUI {
        return new PersonStatusUI(model);
    }

    private parseStyles = (styles) => styles
        .split(';')
        .filter(style => style.split(':')[0] && style.split(':')[1])
        .map(style => [
            style.split(':')[0].trim().replace(/-./g, c => c.substr(1).toUpperCase()),
            style.split(':')[1].trim()
        ])
        .reduce((styleObj, style) => ({
            ...styleObj,
            [style[0]]: style[1],
        }), {})
}
