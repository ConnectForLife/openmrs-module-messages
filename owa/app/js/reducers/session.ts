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
  GET_SESSION: 'session/GET_SESSION',
  RESET_SESSION: 'session/RESET_SESSION'
};

const initialState = {
  loading: false,
  errorMessage: null,
  session: null,
  userRoles: []
};

const reducer = (state = initialState, action) => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.GET_SESSION):
      return {
        ...state,
        loading: true
      };
    case FAILURE(ACTION_TYPES.GET_SESSION):
      return {
        ...initialState,
        errorMessage: action.payload.message,
        loading: false
      };
    case SUCCESS(ACTION_TYPES.GET_SESSION):
      const sessionObj = action.payload.data;
      return {
        ...initialState,
        session: sessionObj,
        userRoles: sessionObj.user.roles,
        loading: false
      };
    case ACTION_TYPES.RESET_SESSION:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

// actions
export const getSession = () => {
  const requestUrl = `/openmrs/ws/rest/v1/appui/session`;
  return {
    type: ACTION_TYPES.GET_SESSION,
    payload: axios.get(requestUrl)
  };
};

export const reset = () => ({
  type: ACTION_TYPES.RESET_SESSION
});

export default reducer;