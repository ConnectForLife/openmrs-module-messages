import _ from 'lodash';

import { REQUEST, SUCCESS, FAILURE } from './action-type.util'
import 'react-toastify/dist/ReactToastify.css';
import { getDefaultValue } from '../shared/model/best-contact-time.model'

export const ACTION_TYPES = {
  GET_BEST_CONTACT_TIME: 'bestContactTimeReducer/GET_BEST_CONTACT_TIME',
  PUT_BEST_CONTACT_TIME: 'bestContactTimeReducer/PUT_BEST_CONTACT_TIME',
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
      };

    case REQUEST(ACTION_TYPES.PUT_BEST_CONTACT_TIME):
      return {
        ...state,
      };
    case FAILURE(ACTION_TYPES.PUT_BEST_CONTACT_TIME):
      return {
        ...state,
      };
    case SUCCESS(ACTION_TYPES.PUT_BEST_CONTACT_TIME):
      return {
        ...state,
      };

    case ACTION_TYPES.RESET:
      return {
        ..._.cloneDeep(initialState)
      };

    default:
      return state;
  }
};


export const getBestContactTime = (patientId: string) => async (dispatch) => {
  console.log(patientId);
  alert('Not yet implemented');
};

export const putBestContactTime = (patientId: string) => async (dispatch) => {
  console.log(patientId);
  alert('Not yet implemented');
};
