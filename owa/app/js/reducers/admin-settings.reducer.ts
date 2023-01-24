/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import {FAILURE, REQUEST, SUCCESS} from './action-type.util';
import {
  continueRequestHandling,
  handleRequestFailure,
  initRequestHandling
} from '../components/request-toast-handler/request-toast-handler';
import _ from 'lodash';

import 'react-toastify/dist/ReactToastify.css';
import {TemplateUI} from '../shared/model/template-ui';
import axiosInstance from '../config/axios';
import {mergeWithObjectUIs, toModel} from '../shared/model/object-ui';
import * as Default from '../shared/utils/messages';
import {getIntl} from '@openmrs/react-components/lib/components/localization/withLocalization';
import {IDefaultBestContactTime} from '../shared/model/default-best-contact-time.model';
import {IActorType} from '../shared/model/actor-type.model';
import {mapFromRequest, mapToRequest} from '../shared/model/contact-time.model';
import {IHealthTipCategory} from '../shared/model/health-tip-category.model';

export const ACTION_TYPES = {
  GET_TEMPLATES: 'adminSettingsReducer/GET_TEMPLATES',
  GET_ACTOR_TYPES: 'adminSettingsReducer/GET_ACTOR_TYPES',
  GET_DEFAULT_CONTACT_TIMES: 'adminSettingsReducer/GET_DEFAULT_CONTACT_TIMES',
  UPDATE_TEMPLATES: 'adminSettingsReducer/UPDATE_TEMPLATE',
  UPDATE_DEFAULT_CONTACT_TIMES: 'adminSettingsReducer/UPDATE_DEFAULT_CONTACT_TIMES',
  PUT_TEMPLATES: 'adminSettingsReducer/PUT_TEMPLATE',
  PUT_DEFAULT_CONTACT_TIMES: 'adminSettingsReducer/PUT_DEFAULT_CONTACT_TIMES',
  GET_HEALTH_TIP_CATEGORIES: 'adminSettingsReducer/GET_HEALTH_TIP_CATEGORIES',
  RESET: 'adminSettingsReducer/RESET'
};

const initialState = {
  defaultTemplates: [] as Array<TemplateUI>,
  defaultBestContactTimes: [] as Array<IDefaultBestContactTime>,
  actorTypes: [] as Array<IActorType>,
  loading: false,
  healthTipCategories: [] as Array<IHealthTipCategory>
};

export type AdminSettingsState = Readonly<typeof initialState>;

export default (state = initialState, action): AdminSettingsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.GET_TEMPLATES):
    case REQUEST(ACTION_TYPES.GET_ACTOR_TYPES):
    case REQUEST(ACTION_TYPES.PUT_TEMPLATES):
    case REQUEST(ACTION_TYPES.GET_DEFAULT_CONTACT_TIMES):
    case REQUEST(ACTION_TYPES.PUT_DEFAULT_CONTACT_TIMES):
    case REQUEST(ACTION_TYPES.GET_HEALTH_TIP_CATEGORIES):
      return {
        ...state,
        loading: true
      };
    case FAILURE(ACTION_TYPES.GET_TEMPLATES):
    case FAILURE(ACTION_TYPES.GET_ACTOR_TYPES):
    case FAILURE(ACTION_TYPES.PUT_TEMPLATES):
    case FAILURE(ACTION_TYPES.GET_DEFAULT_CONTACT_TIMES):
    case FAILURE(ACTION_TYPES.PUT_DEFAULT_CONTACT_TIMES):
    case FAILURE(ACTION_TYPES.GET_HEALTH_TIP_CATEGORIES):
      return {
        ...state,
        loading: false
      };
    case SUCCESS(ACTION_TYPES.GET_TEMPLATES):
      return {
        ...state,
        loading: false,
        defaultTemplates: _.map(action.payload.data.content, template =>
          TemplateUI.fromModelWithActorTypesExcludingStartOfMessagesField(template, state.actorTypes))
      };
    case SUCCESS(ACTION_TYPES.GET_ACTOR_TYPES):
      return {
        ...state,
        loading: false,
        actorTypes: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.GET_DEFAULT_CONTACT_TIMES):
      return {
        ...state,
        loading: false,
        defaultBestContactTimes: _.map(action.payload.data, mapFromRequest)
      };
    case SUCCESS(ACTION_TYPES.PUT_TEMPLATES):
    case SUCCESS(ACTION_TYPES.PUT_DEFAULT_CONTACT_TIMES):
      return {
        ...state,
        loading: false
      }
    case SUCCESS(ACTION_TYPES.GET_HEALTH_TIP_CATEGORIES):
      return {
        ...state,
        loading: false,
        healthTipCategories: action.payload.data as IHealthTipCategory[]
      }
    case ACTION_TYPES.UPDATE_TEMPLATES:
      return {
        ...state,
        defaultTemplates: <Array<TemplateUI>>mergeWithObjectUIs(state.defaultTemplates, action.payload as TemplateUI)
      };
    case ACTION_TYPES.UPDATE_DEFAULT_CONTACT_TIMES:
      return {
        ...state,
        defaultBestContactTimes: action.payload
      };
    case ACTION_TYPES.RESET:
      return {
        ..._.cloneDeep(initialState)
      };
    default:
      return state;
  }
};

const baseUrl = "ws/messages";
const templatesUrl = `${baseUrl}/templates`;
const actorUrl = `${baseUrl}/actor`;
const defaultContactTimesUrl = `${actorUrl}/contact-times/default`;
const actorTypesUrl = `${actorUrl}/types`;
const healthTipCategoriesUri = "ws/messages/defaults/healthTipCategories";

export const getConfig = () => async (dispatch) => {
  await dispatch(getHealthTipCategories());
  // Actor types must be before templates
  await dispatch(getActorTypes());
  await dispatch(getTemplates());
  await dispatch(getBestContactTimes());
}

export const getTemplates = () => ({
  type: ACTION_TYPES.GET_TEMPLATES,
  payload: axiosInstance.get(templatesUrl)
});

export const getBestContactTimes = () => async (dispatch) =>
  dispatch({
    type: ACTION_TYPES.GET_DEFAULT_CONTACT_TIMES,
    payload: axiosInstance.get(defaultContactTimesUrl)
  });

export const getHealthTipCategories = () => async (dispatch) =>
    dispatch({
      type: ACTION_TYPES.GET_HEALTH_TIP_CATEGORIES,
      payload: axiosInstance.get(healthTipCategoriesUri)
    });

export const getActorTypes = () => ({
  type: ACTION_TYPES.GET_ACTOR_TYPES,
  payload: axiosInstance.get(actorTypesUrl)
});

export const updateTemplate = (template: TemplateUI) => async (dispatch) =>
  dispatch({
    type: ACTION_TYPES.UPDATE_TEMPLATES,
    payload: await template
  });

export const updateBestContactTime = (defaultBestContactTimes: Array<IDefaultBestContactTime>) => async (dispatch) =>
  dispatch({
    type: ACTION_TYPES.UPDATE_DEFAULT_CONTACT_TIMES,
    payload: await defaultBestContactTimes
  });

export const saveConfig = (templates: Array<TemplateUI>, contactTimes: Array<IDefaultBestContactTime>, locale: string | undefined) => async (dispatch) => {
  const toastId = initRequestHandling();
  await dispatch(putDefaultContactTimes(contactTimes)).then(
    response => dispatch(putTemplates(templates)).then(
      response => continueRequestHandling(toastId, dispatch, getConfig(),
        getIntl(locale).formatMessage({ id: 'MESSAGES_SETTINGS_SAVE_SUCCESS', defaultMessage: Default.SETTINGS_SAVE_SUCCESS }),
        getIntl(locale).formatMessage({ id: 'MESSAGES_GENERIC_FAILURE', defaultMessage: Default.GENERIC_FAILURE })),
      error => handleRequestFailure(error, toastId,
        getIntl(locale).formatMessage({ id: 'MESSAGES_GENERIC_FAILURE', defaultMessage: Default.GENERIC_FAILURE }))),
    error => handleRequestFailure(error, toastId,
      getIntl(locale).formatMessage({ id: 'MESSAGES_GENERIC_FAILURE', defaultMessage: Default.GENERIC_FAILURE })))
}

export const putDefaultContactTimes = (contactTimes: Array<IDefaultBestContactTime>) => ({
  type: ACTION_TYPES.PUT_DEFAULT_CONTACT_TIMES,
  payload: axiosInstance.put(defaultContactTimesUrl, { records: _.map(contactTimes, mapToRequest) })
});

export const putTemplates = (templates: Array<TemplateUI>) => ({
  type: ACTION_TYPES.PUT_TEMPLATES,
  payload: axiosInstance.put(templatesUrl, { templates: _.map(templates, toModel) })
});

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
