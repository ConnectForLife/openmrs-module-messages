/*
 *  This Source Code Form is subject to the terms of the Mozilla Public License,
 *  v. 2.0. If a copy of the MPL was not distributed with this file, You can
 *  obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 *  the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *  <p>
 *  Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 *  graphic logo is a trademark of OpenMRS Inc.
 */

import _ from 'lodash';

import { REQUEST, SUCCESS, FAILURE } from '../shared/action-type.util';
import axiosInstance from '../shared/axios';

export const ACTION_TYPES = {
  GET_PERSON: 'personReducer/GET_PERSON',
  RESET: 'personReducer/RESET'
};

const initialState = {
  person: {} as any,
  personLoading: false
};

export type PersonState = Readonly<typeof initialState>;

export default (state = initialState, action) => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.GET_PERSON):
      return {
        ...state,
        personLoading: true
      };
    case FAILURE(ACTION_TYPES.GET_PERSON):
      return {
        ...state,
        personLoading: false
      };
    case SUCCESS(ACTION_TYPES.GET_PERSON):
      return {
        ...state,
        personLoading: false,
        person: action.payload.data
      };
    case ACTION_TYPES.RESET:
      return {
        ..._.cloneDeep(initialState)
      };
    default:
      return state;
  }
};

const personUrl = "/ws/rest/v1/person/";

export const getPerson = (personId) => async (dispatch) => {
  await dispatch({
    type: ACTION_TYPES.GET_PERSON,
    payload: axiosInstance.get(`${personUrl}${personId}?v=full`)
  });
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
