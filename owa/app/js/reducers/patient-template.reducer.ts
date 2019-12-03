import _ from 'lodash';

import { REQUEST, SUCCESS, FAILURE } from './action-type.util'
import 'react-toastify/dist/ReactToastify.css';
import { PatientTemplateUI } from '../shared/model/patient-template-ui';
import { IPatientTemplate } from '../shared/model/patient-template.model';
import { TemplateUI } from '../shared/model/template-ui';
import { ITemplate } from '../shared/model/template.model';
import { TemplateFieldType } from '../shared/model/template-field-type';

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
        templates: _.map(action.payload, TemplateUI.fromModel)
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

export const getTemplates = () => async (dispatch) => {
  // TODO: use API and invoke handleRequest method
  await dispatch({
    type: ACTION_TYPES.GET_TEMPLATES,
    payload: Promise.resolve([
      {
        id: 1,
        templateFields: [{
            id: 1,
            name: 'Test field 1',
            mandatory: true,
            defaultValue: 'mockedValue1',
            type: TemplateFieldType.SERVICE_TYPE
          },
          {
            id: 2,
            name: 'Test field 2',
            mandatory: false,
            defaultValue: 'mockedValue2',
            type: TemplateFieldType.MESSAGING_FREQUENCY
          }
        ]
      },{
        id: 2,
        templateFields: [{
            id: 1,
            name: 'Test field 1',
            mandatory: true,
            defaultValue: 'mockedValue1',
            type: TemplateFieldType.SERVICE_TYPE
          },
          {
            id: 3,
            name: 'Test field 3',
            mandatory: true,
            defaultValue: 'mockedValue3',
            type: TemplateFieldType.DAY_OF_WEEK
          }
        ]
      }] as Array<ITemplate>)
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
      },{
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
      },{
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

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

const validatePatientTemplates = () => ({
  // TODO: CFLM-302 will be implemented later
});
