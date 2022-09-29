/*
 *  This Source Code Form is subject to the terms of the Mozilla Public License,
 *  v. 2.0. If a copy of the MPL was not distributed with this file, You can
 *  obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 *  the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *  <p>
 *  Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 *  graphic logo is a trademark of OpenMRS Inc.
 */

 import axios from 'axios';
import _ from 'lodash';

const initialState = {
  styles: [],
  customizeLoading: true
};

const getBaseUrl = () => {
  const path = window.location.pathname;
  return path.substring(0, path.indexOf('/owa/')) + '/';
}

const axiosInstance = axios.create({
  baseURL: getBaseUrl(),
  headers: {
    accept: 'application/json',
  },
});

export const ACTION_TYPES = {
  RESET: 'customizeReducer/RESET',
  GET_CONFIG: 'customizeReducer/GET_CONFIG',
};

export type CustomizeState = Readonly<typeof initialState>;

export default (state = initialState, action: any) => {
  switch (action.type) {
    case `${ACTION_TYPES.GET_CONFIG}_PENDING`:
      return {
        ...state,
        customizeLoading: true
      };
    case `${ACTION_TYPES.GET_CONFIG}_REJECTED`:
      return {
        ...state,
        customizeLoading: false
      };
    case `${ACTION_TYPES.GET_CONFIG}_FULFILLED`:
      return {
        ...state,
        // @ts-ignore
        styles: _.minBy(action.payload.data.results , function(o) { return o.order; }).extensionParams.styles || [],
        customizeLoading: false
      };
    case ACTION_TYPES.RESET: {
      return {
        ..._.cloneDeep(initialState)
      };
    }
    default:
      return state;
  }
};

export const getConfigs = () => async (dispatch: any) => {
  const requestUrl = 'ws/rest/v1/extension?extensionPoint=org.openmrs.module.appui.header.config&v=default';
  await dispatch({
    type: ACTION_TYPES.GET_CONFIG,
    payload: axiosInstance.get(requestUrl)
  });
};
