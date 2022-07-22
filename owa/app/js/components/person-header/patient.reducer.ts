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
  GET_PATIENT: 'patientReducer/GET_PATIENT',
  RESET: 'patientReducer/RESET'
};

const initialState = {
  patient: {} as any,
  patientLoading: false
};

export type PatientState = Readonly<typeof initialState>;

export default (state = initialState, action) => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.GET_PATIENT):
      return {
        ...state,
        patientLoading: true
      };
    case FAILURE(ACTION_TYPES.GET_PATIENT):
      return {
        ...state,
        patientLoading: false
      };
    case SUCCESS(ACTION_TYPES.GET_PATIENT):
      return {
        ...state,
        patientLoading: false,
        patient: action.payload.data
      };
    case ACTION_TYPES.RESET:
      return {
        ..._.cloneDeep(initialState)
      };
    default:
      return state;
  }
};

const patientUrl = "/ws/rest/v1/patient/";

export const getPatient = (patientId) => async (dispatch) => {
  await dispatch({
    type: ACTION_TYPES.GET_PATIENT,
    payload: axiosInstance.get(`${patientUrl}${patientId}?v=full`)
  });
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
