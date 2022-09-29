/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

 import _ from 'lodash';

import { REQUEST, FAILURE, SUCCESS } from "../shared/action-type.util";
import axiosInstance from '../shared/axios';
import { PersonStatusUI } from './model/person-status.model-ui';
import { handleRequest } from '../request-toast-handler/request-toast-handler';
import * as Msg from './constants'

export const ACTION_TYPES = {
    GET_PERSON_STATUS: 'personStatus/GET_PERSON_STATUS',
    GET_POSSIBLE_REASONS: 'personStatus/GET_POSSIBLE_REASONS',
    PUT_PERSON_STATUS: 'personStatus/PUT_PERSON_STATUS',
    OPEN_MODAL: 'personStatus/OPEN_MODAL',
    CLOSE_MODAL: 'personStatus/CLOSE_MODAL',
    RESET: 'personStatus/RESET'
}

const initialState = {
    status: {} as PersonStatusUI,
    personStatusLoading: false,
    showModal: false,
    submitDisabled: false,
    possibleResults: [] as Array<string>
};

export type PersonStatusState = Readonly<typeof initialState>;

export default (state = initialState, action) => {
    switch (action.type) {
        case REQUEST(ACTION_TYPES.GET_PERSON_STATUS):
            return {
                ...state,
                personStatusLoading: true
            };
        case FAILURE(ACTION_TYPES.GET_PERSON_STATUS):
            return {
                ...state,
                personStatusLoading: false
            };
        case SUCCESS(ACTION_TYPES.GET_PERSON_STATUS):
            return {
                ...state,
                status: PersonStatusUI.fromModel(action.payload.data),
                personStatusLoading: false
            };
        case REQUEST(ACTION_TYPES.PUT_PERSON_STATUS):
        case FAILURE(ACTION_TYPES.PUT_PERSON_STATUS):
            return {
                ...state,
                personStatusLoading: false,
                submitDisabled: true
            };
        case SUCCESS(ACTION_TYPES.PUT_PERSON_STATUS):
            return {
                ...state,
                personStatusLoading: false,
                submitDisabled: false,
                status: PersonStatusUI.fromModel(action.payload.data),
            };
        case REQUEST(ACTION_TYPES.GET_POSSIBLE_REASONS):
        case FAILURE(ACTION_TYPES.GET_POSSIBLE_REASONS):
            return {
                ...state
            };
        case SUCCESS(ACTION_TYPES.GET_POSSIBLE_REASONS):
            return {
                ...state,
                possibleResults: action.payload.data,
            };
        case ACTION_TYPES.RESET:
            return {
                ..._.cloneDeep(initialState)
            };
        case ACTION_TYPES.OPEN_MODAL:
            return {
                ...state,
                showModal: true,
                submitDisabled: false
            };
        case ACTION_TYPES.CLOSE_MODAL:
            return {
                ...state,
                showModal: false,
                submitDisabled: false
            };
        default:
            return state;
    }
};

const personStatusUrl = 'ws/messages/person-statuses/';
const personStatusReasonsUrl = personStatusUrl + 'reasons/';

export const getPersonStatus = (personId: string) => async (dispatch) => {
    await dispatch({
        type: ACTION_TYPES.GET_PERSON_STATUS,
        payload: axiosInstance.get(personStatusUrl + personId)
      });
};

export const putPersonStatus = (personStatus: PersonStatusUI) => async (dispatch) => {
    const body = {
        type: ACTION_TYPES.PUT_PERSON_STATUS,
        payload: axiosInstance.put(personStatusUrl + personStatus.personId, personStatus.toModel())
    };
    await handleRequest(dispatch, body, Msg.PERSON_STATUS_CHANGE_SUCCESS, Msg.PERSON_STATUS_CHANGE_FAILURE);
};

export const openModal = (id) => ({
    type: ACTION_TYPES.OPEN_MODAL
  });

export const closeModal = () => ({
    type: ACTION_TYPES.CLOSE_MODAL
  });

export const getPossibleReasons = () => async (dispatch) => {
    await dispatch({
        type: ACTION_TYPES.GET_POSSIBLE_REASONS,
        payload: axiosInstance.get(personStatusReasonsUrl)
      });
};

export const reset = () => ({
    type: ACTION_TYPES.RESET
});
