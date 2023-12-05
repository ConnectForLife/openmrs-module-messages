/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import axios from 'axios';

import { FAILURE, REQUEST, SUCCESS } from './action-type.util';

export const ACTION_TYPES = {
  GET_APP: 'settings/GET_APP'
};

const initialState = {
  appLoading: false,
  app: null
};

const reducer = (state = initialState, action) => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.GET_APP):
      return {
        ...state,
        appLoading: true,
        errorMessage: null
      };
    case FAILURE(ACTION_TYPES.GET_APP):
      return {
        ...state,
        appLoading: false,
        errorMessage: action.payload.message
      };
    case SUCCESS(ACTION_TYPES.GET_APP):
      return {
        ...state,
        appLoading: false,
        app: action.payload.data
      };
    default:
      return state;
  }
};

export const getAppById = (appId: string) => {
  const requestUrl = `/openmrs/ws/rest/v1/app/${appId}`;
  return {
    type: ACTION_TYPES.GET_APP,
    payload: axios.get(requestUrl)
  }
};

export default reducer;