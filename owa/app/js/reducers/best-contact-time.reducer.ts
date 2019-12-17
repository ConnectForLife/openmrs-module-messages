import _ from 'lodash';

import { REQUEST, SUCCESS, FAILURE } from './action-type.util'
import 'react-toastify/dist/ReactToastify.css';
import { handleRequest } from '@bit/soldevelo-omrs.cfl-components.request-toast-handler';
import * as Msg from '../shared/utils/messages';
import { IBestContactTime } from '../shared/model/best-contact-time.model'
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
        bestContactTimes: action.payload.data,
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

export const postBestContactTime = (bestContactTimes: Array<IBestContactTime>) => async (dispatch) => {
  const body = {
    type: ACTION_TYPES.POST_BEST_CONTACT_TIME,
    payload: axiosInstance.post(contactTimeUrl, bestContactTimes)
  }
  await handleRequest(dispatch, body, Msg.GENERIC_SUCCESS, Msg.GENERIC_FAILURE);
};

export const updateBestConstactTime = (newValue: Array<IBestContactTime>) => async (dispatch) => {
  dispatch({
    type: ACTION_TYPES.UPDATE_BEST_CONTACT_TIME,
    payload: newValue
  })
};
