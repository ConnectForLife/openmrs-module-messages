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

import { REQUEST, SUCCESS, FAILURE } from './action-type.util'
import 'react-toastify/dist/ReactToastify.css';
import axiosInstance from '../config/axios';
import { IActor } from '../shared/model/actor.model';

export const ACTION_TYPES = {
  GET_ACTORS: 'actorReducer/GET_ACTORS',
  RESET: 'actorReducer/RESET'
};

const initialState = {
  actorResultList: [] as Array<IActor>,
  actorResultListLoading: false
};

export type ActorState = Readonly<typeof initialState>;

export default (state = initialState, action) => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.GET_ACTORS):
      return {
        ...state,
        actorResultListLoading: true
      };
    case FAILURE(ACTION_TYPES.GET_ACTORS):
      return {
        ...state,
        actorResultListLoading: false
      };
    case SUCCESS(ACTION_TYPES.GET_ACTORS):
      return {
        ...state,
        actorResultListLoading: false,
        actorResultList: action.payload.data
      };
    case ACTION_TYPES.RESET:
      return {
        ..._.cloneDeep(initialState)
      };
    default:
      return state;
  }
};

const actorUrl = "ws/messages/actor/";

export const getActorList = (personId: number, isPatient: boolean = true) => async (dispatch) => {
  await dispatch({
    type: ACTION_TYPES.GET_ACTORS,
    payload: axiosInstance.get(actorUrl + personId, {
      params: {
        isPatient
      }
    })
  });
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
