import _ from 'lodash';

import {REQUEST, SUCCESS, FAILURE} from './action-type.util'
import 'react-toastify/dist/ReactToastify.css';
import axiosInstance from '../config/axios';
import {ServiceResultListUI} from '../shared/model/service-result-list-ui';

export const ACTION_TYPES = {
  GET_SERVICE_RESULT_LISTS: 'calendarReducer/GET_SERVICE_RESULT_LISTS',
  RESET: 'calendarReducer/RESET'
};

const initialState = {
  serviceResultLists: [] as Array<ServiceResultListUI>,
  serviceResultListsLoading: false
};

export type CalendarState = Readonly<typeof initialState>;

export default (state = initialState, action) => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.GET_SERVICE_RESULT_LISTS):
      return {
        ...state,
        serviceResultListsLoading: true
      };
    case FAILURE(ACTION_TYPES.GET_SERVICE_RESULT_LISTS):
      return {
        ...state,
        serviceResultListsLoading: false
      };
    case SUCCESS(ACTION_TYPES.GET_SERVICE_RESULT_LISTS):
      return {
        ...state,
        serviceResultListsLoading: false,
        serviceResultLists: _.map(action.payload.data, ServiceResultListUI.fromModel)
      };
    case ACTION_TYPES.RESET:
      return {
        ..._.cloneDeep(initialState)
      };
    default:
      return state;
  }
};

const templatesUrl = "ws/messages/";

export const getServiceResultLists = (startDate: Date, endDate: Date, patientId: number, isPatient: boolean = true) => async (dispatch) => {
  await dispatch({
    type: ACTION_TYPES.GET_SERVICE_RESULT_LISTS,
    payload: axiosInstance.get(templatesUrl, {
      params: {
        startDate: startDate.valueOf(),
        endDate: endDate.valueOf(),
        personId: patientId,
        isPatient
      },
    })
  });
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
