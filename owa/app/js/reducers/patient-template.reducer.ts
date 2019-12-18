import _ from 'lodash';

import { REQUEST, SUCCESS, FAILURE } from './action-type.util';
import 'react-toastify/dist/ReactToastify.css';
import { PatientTemplateUI } from '../shared/model/patient-template-ui';
import { TemplateUI } from '../shared/model/template-ui';
import { toModel, mergeWithObjectUIs } from '../shared/model/object-ui';
import { handleRequest } from '@bit/soldevelo-omrs.cfl-components.request-toast-handler';
import * as Msg from '../shared/utils/messages';
import { history } from '../config/redux-store';
import axiosInstance from '../config/axios';
import MessageDetails from '../shared/model/message-details';

export const ACTION_TYPES = {
  GET_TEMPLATES: 'patientTemplateReducer/GET_TEMPLATES',
  GET_PATIENT_TEMPLATES: 'patientTemplateReducer/GET_PATIENT_TEMPLATES',
  PUT_PATIENT_TEMPLATES: 'patientTemplateReducer/PUT_PATIENT_TEMPLATES',
  RESET: 'patientTemplateReducer/RESET',
  UPDATE_PATIENT_TEMPLATE: 'patientTemplateReducer/UPDATE_PATIENT_TEMPLATE',
  UPDATE_PATIENT_TEMPLATES: 'patientTemplateReducer/UPDATE_PATIENT_TEMPLATES',
  SELECT_TEMPLATE: 'patientTemplateReducer/SELECT_TEMPLATE',
  GET_MESSAGE_DETAILS: 'patientTemplateReducer/GET_MESSAGE_DETAILS'
};

const initialState = {
  templates: [] as Array<TemplateUI>,
  templatesLoading: false,
  selectedTemplate: null as unknown as TemplateUI,
  patientTemplates: [] as Array<PatientTemplateUI>,
  patientTemplatesLoading: false,
  messageDetails: null as unknown as MessageDetails,
  messageDetailsPages: 0,
  messageDetailsLoading: false
};

export type PatientTemplateState = Readonly<typeof initialState>;

export default (state = initialState, action) => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.GET_MESSAGE_DETAILS):
      return {
        ...state,
        messageDetailsLoading: true
      };
    case FAILURE(ACTION_TYPES.GET_MESSAGE_DETAILS):
      return {
        ...state,
        messageDetailsLoading: false
      };
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
    case SUCCESS(ACTION_TYPES.GET_MESSAGE_DETAILS):
      return {
        ...state,
        messageDetailsLoading: false,
        // the response is wrapped in a page because the collection inside is paginated,
        // so the true result is always in the only collection element
        messageDetails: action.payload.data.content[0],
        pages: Math.ceil((action.payload.data.totalRecords / action.payload.data.pageSize))
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
        patientTemplates: _.map(action.payload.data.content, PatientTemplateUI.fromModel)
      };
    case REQUEST(ACTION_TYPES.PUT_PATIENT_TEMPLATES):
      return {
        ...state,
        patientTemplatesLoading: true
      };
    case FAILURE(ACTION_TYPES.PUT_PATIENT_TEMPLATES):
      return {
        ...state,
        patientTemplatesLoading: false
      };
    case SUCCESS(ACTION_TYPES.PUT_PATIENT_TEMPLATES):
      return {
        ...state,
        patientTemplatesLoading: false,
        patientTemplates: _.map(action.payload, TemplateUI.fromModel)
      };
    case ACTION_TYPES.RESET:
      return {
        ..._.cloneDeep(initialState)
      };
    case ACTION_TYPES.UPDATE_PATIENT_TEMPLATE: {
      return {
        ...state,
        patientTemplates: mergeWithObjectUIs(state.patientTemplates, action.payload)
      };
    }
    case ACTION_TYPES.UPDATE_PATIENT_TEMPLATES: {
      return {
        ...state,
        patientTemplates: action.payload
      };
    }
    case ACTION_TYPES.SELECT_TEMPLATE:
      return {
        ...state,
        selectedTemplate: action.payload
      };
    default:
      return state;
  }
};

const messagesUrl = "ws/messages";
const templatesUrl = messagesUrl + "/templates";
const messageDetailsUrl = messagesUrl + "/details";
const patientTemplatesUrl = messagesUrl + "/patient-templates";

export const getMessagesPage = (page: number, size: number, patientId: number) => async (dispatch) => {
  await dispatch({
    type: ACTION_TYPES.GET_MESSAGE_DETAILS,
    payload: axiosInstance.get(messageDetailsUrl, {
      params: {
        patientId: patientId,
        page: page + 1,
        rows: size
      }
    })
  });
};

export const getTemplates = () => ({
  type: ACTION_TYPES.GET_TEMPLATES,
  payload: axiosInstance.get(templatesUrl)
});

export const getPatientTemplates = (patientId: number) => async (dispatch) => {
  const requestUrl = `${patientTemplatesUrl}/patient/${patientId}`
  await dispatch({
    type: ACTION_TYPES.GET_PATIENT_TEMPLATES,
    payload: axiosInstance.get(requestUrl)
  });
};

export const selectTemplate = (template: TemplateUI) => ({
  type: ACTION_TYPES.SELECT_TEMPLATE,
  payload: template
});

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

export const putPatientTemplates = (patientTemplates: Array<PatientTemplateUI>,
  templates: Array<TemplateUI>, patientId: number, patientUuid: string) => async (dispatch) => {
    const validated = await validatePatientTemplates(patientTemplates, templates, true);
    const requestUrl = `${patientTemplatesUrl}/patient/${patientId}`
    if (isValid(validated)) {
      const body = {
        type: ACTION_TYPES.PUT_PATIENT_TEMPLATES,
        payload: axiosInstance.post(requestUrl, { patientTemplates: _.map(validated, toModel) })
      }
      await handleRequest(dispatch, body, Msg.GENERIC_SUCCESS, Msg.GENERIC_FAILURE);
      history.push(`/messages/${patientId}&patientuuid=${patientUuid}`);
    } else {
      dispatch(updatePatientTemplates(validated));
    }
  };

export const updatePatientTemplate = (patientTemplate: PatientTemplateUI,
  templates: Array<TemplateUI>) => async (dispatch) => {
    dispatch({
      type: ACTION_TYPES.UPDATE_PATIENT_TEMPLATE,
      payload: await patientTemplate.validate(templates, false)
    })
  };

const updatePatientTemplates = (patientTemplates: Array<PatientTemplateUI>) => ({
  // Note: for only internal usage - it omits validation
  type: ACTION_TYPES.UPDATE_PATIENT_TEMPLATES,
  payload: patientTemplates
});

const validatePatientTemplates = (patientTemplates: Array<PatientTemplateUI>,
  templates: Array<TemplateUI>,
  validateNotTouched: boolean): Promise<Array<PatientTemplateUI>> => {
  return Promise.all(_.map(
    patientTemplates,
    patientTemplate => patientTemplate.validate(templates, validateNotTouched)));
};

const isValid = (patientTemplates: Array<PatientTemplateUI>): boolean => {
  // CFLM-404: Temporarily disabled - it should be reverted after 2019.12.09
  // return !_(patientTemplates)
  //   .some(patientTemplate => !_.isEmpty(patientTemplate.errors));
  return true;
};
