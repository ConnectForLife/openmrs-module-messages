/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import { REQUEST, SUCCESS, FAILURE } from './action-type.util';
import axios from 'axios';

export const ACTION_TYPES = {
  GET_SHOW_GENDER_PERSON_HEADER: 'globalProperty/GET_SHOW_GENDER_PERSON_HEADER',
  GET_SHOW_AGE_PERSON_HEADER: 'globalProperty/GET_SHOW_AGE_PERSON_HEADER'
}

const initialState = {
  isShowGenderPersonHeader: null,
  isShowAgePersonHeader: null
};

export type GlobalPropertyState = Readonly<typeof initialState>

export default (state = initialState, action) => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.GET_SHOW_GENDER_PERSON_HEADER):
      return {
        ...state,
        loading: true,
        success: false
      };
    case FAILURE(ACTION_TYPES.GET_SHOW_GENDER_PERSON_HEADER):
      return {
        ...state,
        loading: false,
        success: false
      };
    case SUCCESS(ACTION_TYPES.GET_SHOW_GENDER_PERSON_HEADER):
      const showGenderPersonHeaderResult = action.payload.data.results;
      return {
        ...state,
        isShowGenderPersonHeader: showGenderPersonHeaderResult?.length ? showGenderPersonHeaderResult[0] : state.isShowGenderPersonHeader,
        loading: false,
        success: true
      };
    case REQUEST(ACTION_TYPES.GET_SHOW_AGE_PERSON_HEADER):
      return {
        ...state,
        loading: true,
        success: false
      };
    case FAILURE(ACTION_TYPES.GET_SHOW_AGE_PERSON_HEADER):
      return {
        ...state,
        loading: false,
        success: false
      };
    case SUCCESS(ACTION_TYPES.GET_SHOW_AGE_PERSON_HEADER):
      const showAgePersonHeaderResult = action.payload.data.results;
      return {
        ...state,
        isShowAgePersonHeader: showAgePersonHeaderResult?.length ? showAgePersonHeaderResult[0] : state.isShowAgePersonHeader,
        loading: false,
        success: true
      };
    default:
      return state;
  }
};

const baseUrl = "/openmrs/ws/rest/v1/systemsetting"

//actions
export const getShowGenderPersonHeader = () => {
  const requestUrl = `${baseUrl}?q=cfl.showGenderPersonHeader&v=default`;
  return {
    type: ACTION_TYPES.GET_SHOW_GENDER_PERSON_HEADER,
    payload: axios.get(requestUrl)
  };
};

export const getShowAgePersonHeader = () => {
  const requestUrl = `${baseUrl}?q=cfl.showAgePersonHeader&v=default`;
  return {
    type: ACTION_TYPES.GET_SHOW_AGE_PERSON_HEADER,
    payload: axios.get(requestUrl)
  };
};
