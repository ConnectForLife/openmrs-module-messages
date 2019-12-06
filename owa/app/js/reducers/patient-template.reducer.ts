import _ from 'lodash';

import { REQUEST, SUCCESS, FAILURE } from './action-type.util'
import 'react-toastify/dist/ReactToastify.css';
import { PatientTemplateUI } from '../shared/model/patient-template-ui';
import { IPatientTemplate } from '../shared/model/patient-template.model';
import { TemplateUI } from '../shared/model/template-ui';
import { toModel, mergeWithObjectUIs } from '../shared/model/object-ui';
import { handleRequest } from '@bit/soldevelo-omrs.cfl-components.request-toast-handler';
import * as Msg from '../shared/utils/messages';
import { history } from '../config/redux-store';
import axiosInstance from '../config/axios';

export const ACTION_TYPES = {
  GET_TEMPLATES: 'patientTemplateReducer/GET_TEMPLATES',
  GET_PATIENT_TEMPLATES: 'patientTemplateReducer/GET_PATIENT_TEMPLATES',
  PUT_PATIENT_TEMPLATES: 'patientTemplateReducer/PUT_PATIENT_TEMPLATES',
  RESET: 'patientTemplateReducer/RESET',
  UPDATE_PATIENT_TEMPLATE: 'patientTemplateReducer/UPDATE_PATIENT_TEMPLATE',
  UPDATE_PATIENT_TEMPLATES: 'patientTemplateReducer/UPDATE_PATIENT_TEMPLATES',
  SELECT_TEMPLATE: 'patientTemplateReducer/SELECT_TEMPLATE'
};

const initialState = {
  templates: [] as Array<TemplateUI>,
  templatesLoading: false,
  selectedTemplate: TemplateUI,
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

const templatesUrl = "ws/messages/templates";

export const getTemplates = () => ({
  type: ACTION_TYPES.GET_TEMPLATES,
  payload: axiosInstance.get(templatesUrl)
});

export const getPatientTemplates = (patientId: number) => ({
  // TODO: use API instead of resolved promise
  type: ACTION_TYPES.GET_PATIENT_TEMPLATES,
  payload: Promise.resolve({
    data: {
      content: [
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
        }] as Array<IPatientTemplate>
    }
  })
});

export const selectTemplate = (template) => ({
  type: ACTION_TYPES.SELECT_TEMPLATE,
  payload: template
});

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

export const putPatientTemplates = (patientTemplates: Array<PatientTemplateUI>, templates: Array<TemplateUI>) => async (dispatch) => {
  const validated = await validatePatientTemplates(patientTemplates, templates, true);
  if (isValid(validated)) {
    const body = {
      type: ACTION_TYPES.PUT_PATIENT_TEMPLATES,
      payload: Promise.resolve(_.map(validated, toModel)) // TODO: use API instead of resolved promise
    }
    await handleRequest(dispatch, body, Msg.GENERIC_SUCCESS, Msg.GENERIC_FAILURE);
    history.push('/messages/patient-template'); //TODO: CFLM-255: Return to 'messages manage'
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
  return !_(patientTemplates)
    .some(patientTemplate => !_.isEmpty(patientTemplate.errors));
};
