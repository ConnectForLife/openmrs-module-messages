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
import { handleRequest } from '../components/request-toast-handler/request-toast-handler';
import * as Default from '../shared/utils/messages';
import { getIntl } from '@openmrs/react-components/lib/components/localization/withLocalization';
import { IBestContactTime } from '../shared/model/best-contact-time.model';
import { mapFromRequest, mapToRequest } from '../shared/model/contact-time.model';
import axiosInstance from '../config/axios';

export const ACTION_TYPES = {
  GET_BEST_CONTACT_TIME: 'bestContactTimeReducer/GET_BEST_CONTACT_TIME',
  POST_BEST_CONTACT_TIME: 'bestContactTimeReducer/POST_BEST_CONTACT_TIME',
  UPDATE_BEST_CONTACT_TIME: 'bestContactTimeReducer/UPDATE_BEST_CONTACT_TIME',
  RESET: 'bestContactTimeReducer/RESET',
};

const initialState = {
  bestContactTimes: [] as Array<IBestContactTime>,
  bestContactTimesLoading: false
};

export type BestContactTimeState = Readonly<typeof initialState>;

export default (state = initialState, action) => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.GET_BEST_CONTACT_TIME):
      return {
        ...state,
        bestContactTimesLoading: true
      };
    case FAILURE(ACTION_TYPES.GET_BEST_CONTACT_TIME):
      return {
        ...state,
        bestContactTimesLoading: true
      };
    case SUCCESS(ACTION_TYPES.GET_BEST_CONTACT_TIME):
      return {
        ...state,
        bestContactTimes: _.map(action.payload.data, mapFromRequest),
        bestContactTimesLoading: false
      };

    case REQUEST(ACTION_TYPES.POST_BEST_CONTACT_TIME):
      return {
        ...state,
      };
    case FAILURE(ACTION_TYPES.POST_BEST_CONTACT_TIME):
      return {
        ...state,
      };
    case SUCCESS(ACTION_TYPES.POST_BEST_CONTACT_TIME):
      return {
        ...state,
      };
    case ACTION_TYPES.UPDATE_BEST_CONTACT_TIME:
      return {
        ...state,
        bestContactTimes: action.payload
      };
    case ACTION_TYPES.RESET:
      return {
        ..._.cloneDeep(initialState)
      };

    default:
      return state;
  }
};

const contactTimeUrl = "ws/messages/actor/contact-times";

export const getBestContactTime = (personIds: Array<number>) => async (dispatch) => {
  await dispatch({
    type: ACTION_TYPES.GET_BEST_CONTACT_TIME,
    payload: axiosInstance.get(contactTimeUrl, {
      params: {
        personIds
      }
    })
  });
};

export const postBestContactTime = (bestContactTimes: Array<IBestContactTime>, locale: string | undefined) => async (dispatch) => {
  const body = {
    type: ACTION_TYPES.POST_BEST_CONTACT_TIME,
    payload: axiosInstance.post(contactTimeUrl, _.map(bestContactTimes, mapToRequest))
  };
  await handleRequest(dispatch, body,
    getIntl(locale).formatMessage({ id: 'MESSAGES_BEST_CONTACT_TIME_SAVE_SUCCESS', defaultMessage: Default.BEST_CONTACT_TIME_SAVE_SUCCESS }),
    getIntl(locale).formatMessage({ id: 'MESSAGES_BEST_CONTACT_TIME_SAVE_FAILURE', defaultMessage: Default.BEST_CONTACT_TIME_SAVE_FAILURE }));
};

export const updateBestConstactTime = (newValue: Array<IBestContactTime>) => async (dispatch) => {
  dispatch({
    type: ACTION_TYPES.UPDATE_BEST_CONTACT_TIME,
    payload: newValue
  })
};
