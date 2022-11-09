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

import 'react-toastify/dist/ReactToastify.css';
import { PatientTemplateUI } from '../shared/model/patient-template-ui';
import { TemplateUI } from '../shared/model/template-ui';
import { toModel, mergeWithObjectUIs } from '../shared/model/object-ui';
import { handleRequest } from '../components/request-toast-handler/request-toast-handler';
import * as Default from '../shared/utils/messages';
import { getIntl } from '@openmrs/react-components/lib/components/localization/withLocalization';
import { history } from '../config/redux-store';
import axiosInstance, { ALL_RECORDS_PAGE } from '../config/axios';
import MessageDetails from '../shared/model/message-details';
import {
  getDefaultValue as getDefaultValuesInitialState,
  IDefaultValuesState
} from '../shared/model/default-values-state';
import { REQUEST, SUCCESS, FAILURE } from './action-type.util';

export const ACTION_TYPES = {
  GET_TEMPLATES: 'patientTemplateReducer/GET_TEMPLATES',
  GET_PATIENT_TEMPLATES: 'patientTemplateReducer/GET_PATIENT_TEMPLATES',
  CHECK_DEFAULT_VALUES: 'patientTemplateReducer/CHECK_DEFAULT_VALUES',
  PUT_PATIENT_TEMPLATES: 'patientTemplateReducer/PUT_PATIENT_TEMPLATES',
  RESET: 'patientTemplateReducer/RESET',
  UPDATE_PATIENT_TEMPLATE: 'patientTemplateReducer/UPDATE_PATIENT_TEMPLATE',
  UPDATE_PATIENT_TEMPLATES: 'patientTemplateReducer/UPDATE_PATIENT_TEMPLATES',
  GET_MESSAGE_DETAILS: 'patientTemplateReducer/GET_MESSAGE_DETAILS',
  GENERATE_DEFAULT_PATIENT_TEMPLATES: 'patientTemplateReducer/GENERATE_DEFAULT_PATIENT_TEMPLATES'
};

const initialState = {
  templates: [] as Array<TemplateUI>,
  templatesLoading: false,
  patientTemplates: [] as Array<PatientTemplateUI>,
  patientTemplatesLoading: false,
  messageDetails: null as unknown as MessageDetails,
  messageDetailsLoading: false,
  defaultValuesState: getDefaultValuesInitialState() as IDefaultValuesState
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
        messageDetails: state.defaultValuesState.defaultValuesUsed === true ? //don't overwrite if defaults used
          state.messageDetails : action.payload.data
      };
    case REQUEST(ACTION_TYPES.GET_PATIENT_TEMPLATES):
      return {
        ...state,
        patientTemplatesLoading: true,
        defaultValuesState: initialState.defaultValuesState
      };
    case FAILURE(ACTION_TYPES.GET_PATIENT_TEMPLATES):
      return {
        ...state,
        patientTemplatesLoading: false,
        defaultValuesState: initialState.defaultValuesState
      };
    case SUCCESS(ACTION_TYPES.GET_PATIENT_TEMPLATES):
      return {
        ...state,
        patientTemplatesLoading: false,
        patientTemplates: _.map(action.payload.data.content, PatientTemplateUI.fromModel),
      };
    case REQUEST(ACTION_TYPES.CHECK_DEFAULT_VALUES):
      return {
        ...state,
        messageDetailsLoading: true,
        defaultValuesState: initialState.defaultValuesState
      };
    case FAILURE(ACTION_TYPES.CHECK_DEFAULT_VALUES):
      return {
        ...state,
        messageDetailsLoading: false,
        defaultValuesState: initialState.defaultValuesState
      };
    case SUCCESS(ACTION_TYPES.CHECK_DEFAULT_VALUES):
      const defaultValuesState: IDefaultValuesState = action.payload.data;
      return {
        ...state,
        defaultValuesState,
        messageDetailsLoading: false,
        messageDetails: defaultValuesState.details
      };
    case REQUEST(ACTION_TYPES.GENERATE_DEFAULT_PATIENT_TEMPLATES):
    case FAILURE(ACTION_TYPES.GENERATE_DEFAULT_PATIENT_TEMPLATES):
      return {
        ...state
      };
    case SUCCESS(ACTION_TYPES.GENERATE_DEFAULT_PATIENT_TEMPLATES):
      return {
        ...state,
        defaultValuesState: {
          defaultValuesUsed: false,
          allValuesDefault: false,
          lackingPatientTemplates: [],
          details: state.messageDetails
        }
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
    case SUCCESS(ACTION_TYPES.PUT_PATIENT_TEMPLATES): {
      const ACTION_PAYLOAD_STATUS_TEXT = "statusText";
      const actionPayload = action.payload.statusText ? action.payload : _.omit(action.payload, ACTION_PAYLOAD_STATUS_TEXT);

      return {
        ...state,
        patientTemplatesLoading: false,
        patientTemplates: _.map(actionPayload, TemplateUI.fromModel)
      };
    }
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
    default:
      return state;
  }
};

const messagesUrl = "ws/messages";
const templatesUrl = messagesUrl + "/templates";
const messageDetailsUrl = messagesUrl + "/details";
const patientTemplatesUrl = messagesUrl + "/patient-templates";

export const getMessages = (patientId: number, isPatient: boolean = true) => async (dispatch) => {
  await dispatch({
    type: ACTION_TYPES.GET_MESSAGE_DETAILS,
    payload: axiosInstance.get(messageDetailsUrl, {
      params: {
        personId: patientId,
        isPatient
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
    payload: axiosInstance.get(requestUrl, {
      params: { page: ALL_RECORDS_PAGE }
    })
  });
};

export const checkIfDefaultValuesUsed = (patientId: number) => async (dispatch) => {
  const requestUrl = `${messagesUrl}/defaults/${patientId}/check`
  await dispatch({
    type: ACTION_TYPES.CHECK_DEFAULT_VALUES,
    payload: axiosInstance.get(requestUrl)
  });
};

export const generateDefaultPatientTemplates = (patientId: number, locale: string | undefined) => async (dispatch) => {
  const requestUrl = `${messagesUrl}/defaults/${patientId}/generate-and-save`;
  const body = {
    type: ACTION_TYPES.GENERATE_DEFAULT_PATIENT_TEMPLATES,
    payload: axiosInstance.post(requestUrl)
  }
  await handleRequest(dispatch, body,
    getIntl(locale).formatMessage({ id: 'MESSAGES_DEFAULT_GENERATION_SUCCESS', defaultMessage: Default.DEFAULT_GENERATION_SUCCESS }),
    getIntl(locale).formatMessage({ id: 'MESSAGES_DEFAULT_GENERATION_FAILURE', defaultMessage: Default.DEFAULT_GENERATION_FAILURE }));
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

export const putPatientTemplates = (patientTemplates: Array<PatientTemplateUI>,
  templates: Array<TemplateUI>, patientId: number, patientUuid: string, dashboardType: string, locale: string | undefined) => async (dispatch) => {
    const validated = await validatePatientTemplates(patientTemplates, templates, true, locale);
    const requestUrl = `${patientTemplatesUrl}/patient/${patientId}`
    if (isValid(validated)) {
      const body = {
        type: ACTION_TYPES.PUT_PATIENT_TEMPLATES,
        payload: axiosInstance.post(requestUrl, { patientTemplates: _.map(validated, toModel) })
      }
      await handleRequest(dispatch, body,
        getIntl(locale).formatMessage({ id: 'MESSAGES_GENERIC_SUCCESS', defaultMessage: Default.GENERIC_SUCCESS }),
        getIntl(locale).formatMessage({ id: 'MESSAGES_GENERIC_FAILURE', defaultMessage: Default.GENERIC_FAILURE }));
      history.push(`/messages/${dashboardType}/${patientId}&patientuuid=${patientUuid}`);
    } else {
      dispatch(updatePatientTemplates(validated));
    }
  };

export const updatePatientTemplate = (patientTemplate: PatientTemplateUI,
  templates: Array<TemplateUI>, locale: string | undefined, persisted: boolean = false) => async (dispatch) => {
    patientTemplate.isPersisted = !persisted;
    dispatch({
      type: ACTION_TYPES.UPDATE_PATIENT_TEMPLATE,
      payload: await patientTemplate.validate(templates, false, locale)
    })
  };

const updatePatientTemplates = (patientTemplates: Array<PatientTemplateUI>) => ({
  // Note: for only internal usage - it omits validation
  type: ACTION_TYPES.UPDATE_PATIENT_TEMPLATES,
  payload: patientTemplates
});

const validatePatientTemplates = (patientTemplates: Array<PatientTemplateUI>,
  templates: Array<TemplateUI>,
  validateNotTouched: boolean, locale: string | undefined): Promise<Array<PatientTemplateUI>> => {
  return Promise.all(_.map(
    patientTemplates,
    patientTemplate => patientTemplate.validate(templates, validateNotTouched, locale)));
};

const isValid = (patientTemplates: Array<PatientTemplateUI>): boolean => {
  return !_(patientTemplates)
    .some(patientTemplate => !_.isEmpty(patientTemplate.errors));
};
