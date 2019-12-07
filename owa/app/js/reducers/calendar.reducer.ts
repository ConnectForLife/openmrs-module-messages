import _ from 'lodash';

import { REQUEST, SUCCESS, FAILURE } from './action-type.util'
import 'react-toastify/dist/ReactToastify.css';
import { IPatientTemplate } from '../shared/model/patient-template.model';
import axiosInstance from '../config/axios';
import { ServiceResultListUI } from '../shared/model/service-result-list-ui';

export const ACTION_TYPES = {
  GET_SERVICE_RESULT_LISTS: 'calendarReducer/GET_SERVICE_RESULT_LISTS',
  RESET: 'patientTemplateReducer/RESET'
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
        serviceResultLists: _.map(action.payload, ServiceResultListUI.fromModel) //action.payload.data
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

export const getServiceResultLists = (startDate: Date, endDate: Date, patientId: number) => async (dispatch) => {
  console.log(startDate, endDate, patientId);
  var date1 = new Date(2019, 11, 3);
  var date2 = new Date(2019, 11, 3);
  date2.setHours(15);
  var date3 = new Date(2019, 11, 7);
  var date4 = new Date(2019, 11, 17);
  var date5 = new Date(2019, 11, 17);
  await dispatch({
    type: ACTION_TYPES.GET_SERVICE_RESULT_LISTS,
    //payload: axiosInstance.get(`${templatesUrl}?startDate=${startDate.valueOf()}&endDate=${endDate.valueOf()}&patientId=${patientId}`)
    payload: Promise.resolve([
      {
        patientId: 7,
        actorId: 7,
        serviceId: 1,
        serviceName: 'Adherance Reports',
        startDate: new Date(),
        endDate: new Date(),
        results: [
          {
            executionDate: date1,
            messageId: 1,
            channelId: 1,
            serviceStatus: 'DELIVERED'
          }
        ]
      },
      {
        patientId: 7,
        actorId: 7,
        serviceId: 1,
        serviceName: 'Adherance Reports',
        startDate: new Date(),
        endDate: new Date(),
        results: [
          {
            executionDate: date2,
            messageId: 1,
            channelId: 1,
            serviceStatus: 'DELIVERED'
          }
        ]
      },
      {
        patientId: 7,
        actorId: 7,
        serviceId: 1,
        serviceName: 'Health Tip',
        startDate: new Date(),
        endDate: new Date(),
        results: [
          {
            executionDate: date3,
            messageId: 1,
            channelId: 1,
            serviceStatus: 'FAILED'
          }
        ]
      },
      {
        patientId: 7,
        actorId: 7,
        serviceId: 1,
        serviceName: 'Health Tip',
        startDate: new Date(),
        endDate: new Date(),
        results: [
          {
            executionDate: date4,
            messageId: 1,
            channelId: 1,
            serviceStatus: 'PENDING'
          }
        ]
      }
      ,
      {
        patientId: 7,
        actorId: 7,
        serviceId: 1,
        serviceName: 'Adherance Reports',
        startDate: new Date(),
        endDate: new Date(),
        results: [
          {
            executionDate: date5,
            messageId: 1,
            channelId: 1,
            serviceStatus: 'PENDING'
          }
        ]
      },
      {
        patientId: 7,
        actorId: 8,
        serviceId: 1,
        serviceName: 'Adherance Reports',
        startDate: new Date(),
        endDate: new Date(),
        results: [
          {
            executionDate: date5,
            messageId: 1,
            channelId: 1,
            serviceStatus: 'PENDING'
          }
        ]
      }
    ])
  });
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
