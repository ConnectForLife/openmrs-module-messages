import _ from 'lodash';

import { REQUEST, SUCCESS, FAILURE } from './action-type.util'
import 'react-toastify/dist/ReactToastify.css';
import { PatientTemplateUI } from '../shared/model/patient-template-ui';
import { IPatientTemplate } from '../shared/model/patient-template.model';
import { TemplateUI } from '../shared/model/template-ui';
import { ITemplate } from '../shared/model/template.model';
import { TemplateFieldType } from '../shared/model/template-field-type';
import { handleRequest } from '@bit/soldevelo-omrs.cfl-components.request-toast-handler';
import * as Msg from '../shared/utils/messages';
import { history } from '../config/redux-store';
import axiosInstance from '../config/axios';

export const ACTION_TYPES = {
  GET_TEMPLATES: 'messagingReducer/GET_TEMPLATES',
  GET_PATIENT_TEMPLATES: 'messagingReducer/GET_PATIENT_TEMPLATES',
  RESET: 'messagingReducer/RESET',
  UPDATE_TEMPLATE_FIELD_VALUE: 'messagingReducer/UPDATE_TEMPLATE_FIELD_VALUE',
};

const initialState = {
  templates: [] as Array<TemplateUI>,
  templatesLoading: false,
  patientTemplates: [] as Array<PatientTemplateUI>,
  patientTemplatesLoading: false
};

export type PatientTemplateState = Readonly<typeof initialState>;

export default (state = initialState, action) => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.GET_TEMPLATES):
      return {
        ...state,
        templatesLoading: true
      };
    case FAILURE(ACTION_TYPES.GET_TEMPLATES):
      return {
        ...state,
        templatesLoading: false
      };
    case SUCCESS(ACTION_TYPES.GET_TEMPLATES):
    return {
        ...state,
        templatesLoading: false,
        templates: _.map(action.payload.data.content, TemplateUI.fromModel)
      };
    case REQUEST(ACTION_TYPES.GET_PATIENT_TEMPLATES):
      return {
        ...state,
        patientTemplatesLoading: true
      };
    case FAILURE(ACTION_TYPES.GET_PATIENT_TEMPLATES):
      return {
        ...state,
        patientTemplatesLoading: false
      };
    case SUCCESS(ACTION_TYPES.GET_PATIENT_TEMPLATES):
      return {
        ...state,
        patientTemplatesLoading: false,
        patientTemplates: _.map(action.payload, PatientTemplateUI.fromModel)
      };
    case ACTION_TYPES.RESET:
      return {
        ..._.cloneDeep(initialState)
      };
    case ACTION_TYPES.UPDATE_TEMPLATE_FIELD_VALUE:
      return {
        ...state
        // TODO: CFLM-302: will be implemented later
      };
    default:
      return state;
  }
};

const templatesUrl = "ws/messages/templates";

export const getTemplates = () => async (dispatch) => {
  await dispatch({
    type: ACTION_TYPES.GET_TEMPLATES,
    payload: axiosInstance.get(templatesUrl)
  });
};

export const getPatientTemplates = (patientId: number) => async (dispatch) => {
  // TODO: use API and invoke handleRequest method
  await dispatch({
    type: ACTION_TYPES.GET_PATIENT_TEMPLATES,
    payload: Promise.resolve([
      {
        id: 1,
        patientId: patientId,
        templateId: 1,
        actorId: 1,
        actorTypeId: 1,
        templateFieldValues: [{
          id: 1,
          templateFieldId: 1,
          value: 'a'
        },
        {
          id: 2,
          templateFieldId: 2,
          value: 'b'
        }
        ]
      }, {
        id: 2,
        patientId: patientId,
        templateId: 1,
        actorId: 2,
        actorTypeId: 2,
        templateFieldValues: [{
          id: 3,
          templateFieldId: 1,
          value: 'a'
        },
        {
          id: 4,
          templateFieldId: 2,
          value: 'b'
        }
        ]
      }, {
        id: 3,
        patientId: patientId,
        templateId: 2,
        actorId: 3,
        actorTypeId: 1,
        templateFieldValues: [{
          id: 5,
          templateFieldId: 1,
          value: 'c'
        },
        {
          id: 6,
          templateFieldId: 3,
          value: 'd'
        }
        ]
      }] as Array<IPatientTemplate>)
  });
};

export const putPatientTemplates = (patientTemplates: Array<PatientTemplateUI>) => async (dispatch) => {
  console.log(patientTemplates);
  // TODO: CFLM-302: Attach validation here

  // const body = {
  //   type: ACTION_TYPES.GET_PATIENT_TEMPLATES,
  //   payload: ...
  // };
  // handleRequest(dispatch, body, Msg.GENERIC_SUCCESS, Msg.GENERIC_FAILURE);
  
  history.push('/messages/patient-template'); //TODO: CFLM-255: Return to 'messages manage'
  alert('Not yet implemented');
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

const validatePatientTemplates = () => ({
  // TODO: CFLM-302 will be implemented later
});
