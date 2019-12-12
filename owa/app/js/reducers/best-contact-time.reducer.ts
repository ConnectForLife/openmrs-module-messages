import _ from 'lodash';

import { REQUEST, SUCCESS, FAILURE } from './action-type.util'
import 'react-toastify/dist/ReactToastify.css';
import { getDefaultValue, IBestContactTime } from '../shared/model/best-contact-time.model'
import axiosInstance from '../config/axios';

export const ACTION_TYPES = {
  GET_BEST_CONTACT_TIME: 'bestContactTimeReducer/GET_BEST_CONTACT_TIME',
  POST_BEST_CONTACT_TIME: 'bestContactTimeReducer/POST_BEST_CONTACT_TIME',
  UPDATE_BEST_CONTACT_TIME: 'bestContactTimeReducer/UPDATE_BEST_CONTACT_TIME',
  RESET: 'bestContactTimeReducer/RESET',
};

const initialState = {
  bestContactTime: getDefaultValue()
};

export type BestContactTimeState = Readonly<typeof initialState>;

export default (state = initialState, action) => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.GET_BEST_CONTACT_TIME):
      return {
        ...state,
      };
    case FAILURE(ACTION_TYPES.GET_BEST_CONTACT_TIME):
      return {
        ...state,
      };
    case SUCCESS(ACTION_TYPES.GET_BEST_CONTACT_TIME):
      return {
        ...state,
        bestContactTime: { ...state.bestContactTime, patientTime: action.payload.data}
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
        bestContactTime: action.payload
      };
    case ACTION_TYPES.RESET:
      return {
        ..._.cloneDeep(initialState)
      };

    default:
      return state;
  }
};

const urlPrefix = "ws/messages/actor/";
const urlSufix = "/contact-time";

export const getBestContactTime = (patientId: string) => async (dispatch) => {
  await dispatch({
    type: ACTION_TYPES.GET_BEST_CONTACT_TIME,
    payload: axiosInstance.get(urlPrefix + patientId + urlSufix)
  });
};

export const postBestContactTime = (patientId: string, time: string) => async (dispatch) => {
  var bodyFormData = new FormData();
  bodyFormData.set('time', time);
  await dispatch({
    type: ACTION_TYPES.POST_BEST_CONTACT_TIME,
    payload: axiosInstance.post(urlPrefix + patientId + urlSufix, bodyFormData, {
      headers: { 'Content-Type': 'multipart/form-data' }
     })
  });
};

export const updateBestConstactTime = (newValue: IBestContactTime) => async (dispatch) => {
  dispatch({
    type: ACTION_TYPES.UPDATE_BEST_CONTACT_TIME,
    payload: newValue
  })
};
