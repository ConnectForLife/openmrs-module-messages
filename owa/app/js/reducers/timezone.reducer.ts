/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import _ from 'lodash';
import { REQUEST, FAILURE, SUCCESS } from "./action-type.util";
import axiosInstance from '../config/axios';

export const ACTION_TYPES = {
    GET_TIMEZONE: 'timezone/GET_TIMEZONE',
    RESET: 'timezone/RESET'
};

const initialState = {
    timezone: undefined as unknown as string,
    timezoneLoading: false,
};

export type TimezoneState = Readonly<typeof initialState>;

export default (state = initialState, action) => {
    switch (action.type) {
        case REQUEST(ACTION_TYPES.GET_TIMEZONE):
            return {
                ...state,
                timezoneLoading: true
            };
        case FAILURE(ACTION_TYPES.GET_TIMEZONE):
            return {
                ...state,
                timezoneLoading: false
            };
        case SUCCESS(ACTION_TYPES.GET_TIMEZONE):
            return {
                ...state,
                timezone: action.payload.data.results[0].value,
                timezoneLoading: false
            };
        case ACTION_TYPES.RESET:
            return {
                ..._.cloneDeep(initialState)
            };
        default:
            return state;
    }
};

const timezoneUrl = 'ws/rest/v1/systemsetting?limit=1&v=default&q=messages.defaultUserTimezone';


export const getTimezone = () => async (dispatch) => {
    await dispatch({
        type: ACTION_TYPES.GET_TIMEZONE,
        payload: axiosInstance.get(timezoneUrl)
      });
};

export const reset = () => ({
    type: ACTION_TYPES.RESET
});
