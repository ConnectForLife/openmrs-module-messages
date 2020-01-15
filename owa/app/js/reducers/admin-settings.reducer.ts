import { REQUEST, SUCCESS, FAILURE } from './action-type.util';
import { handleRequest } from '@bit/soldevelo-omrs.cfl-components.request-toast-handler';
import _ from 'lodash';

import 'react-toastify/dist/ReactToastify.css';
import { TemplateUI } from '../shared/model/template-ui';
import axiosInstance from '../config/axios';
import { toModel, mergeWithObjectUIs } from '../shared/model/object-ui';
import * as Msg from '../shared/utils/messages';
import { IDefaultBestContactTime } from '../shared/model/default-best-contact-time.model';
import { IActorType } from '../shared/model/actor-type.model';
import { mapFromRequest, mapToRequest } from '../shared/model/contact-time.model';

export const ACTION_TYPES = {
  GET_TEMPLATES: 'adminSettingsReducer/GET_TEMPLATES',
  GET_ACTOR_TYPES: 'adminSettingsReducer/GET_ACTOR_TYPES',
  GET_DEFAULT_CONTACT_TIMES: 'adminSettingsReducer/GET_DEFAULT_CONTACT_TIMES',
  PUT_DEFAULT_CONTACT_TIMES: 'adminSettingsReducer/PUT_DEFAULT_CONTACT_TIMES',
  UPDATE_TEMPLATES: 'adminSettingsReducer/UPDATE_TEMPLATE',
  PUT_TEMPLATES: 'adminSettingsReducer/PUT_TEMPLATE',
  RESET: 'adminSettingsReducer/RESET'
};

const initialState = {
  defaultTemplates: [] as Array<TemplateUI>,
  defaultBestContactTimes: [] as Array<IDefaultBestContactTime>,
  actorTypes: [] as Array<IActorType>,
  loading: false
};

export type AdminSettingsState = Readonly<typeof initialState>;

export default (state = initialState, action): AdminSettingsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.GET_TEMPLATES):
    case REQUEST(ACTION_TYPES.GET_ACTOR_TYPES):
    case REQUEST(ACTION_TYPES.PUT_TEMPLATES):
    case REQUEST(ACTION_TYPES.GET_DEFAULT_CONTACT_TIMES):
    case REQUEST(ACTION_TYPES.PUT_DEFAULT_CONTACT_TIMES):
      return {
        ...state,
        loading: true
      };
    case FAILURE(ACTION_TYPES.GET_TEMPLATES):
    case FAILURE(ACTION_TYPES.GET_ACTOR_TYPES):
    case FAILURE(ACTION_TYPES.PUT_TEMPLATES):
    case FAILURE(ACTION_TYPES.GET_DEFAULT_CONTACT_TIMES):
    case FAILURE(ACTION_TYPES.PUT_DEFAULT_CONTACT_TIMES):
      return {
        ...state,
        loading: false
      };
    case SUCCESS(ACTION_TYPES.GET_TEMPLATES):
      return {
        ...state,
        loading: false,
        defaultTemplates: _.map(action.payload.data.content, TemplateUI.fromModel)
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
      return {
        ...state,
        loading: false
      }
    case ACTION_TYPES.UPDATE_TEMPLATES:
      return {
        ...state,
        defaultTemplates: <Array<TemplateUI>>mergeWithObjectUIs(state.defaultTemplates, action.payload as TemplateUI)
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

export const getTemplates = () => ({
  type: ACTION_TYPES.GET_TEMPLATES,
  payload: axiosInstance.get(templatesUrl)
});

export const getBestContactTimes = () => ({
  type: ACTION_TYPES.GET_DEFAULT_CONTACT_TIMES,
  payload: axiosInstance.get(defaultContactTimesUrl)
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

export const putTemplates = (templates: Array<TemplateUI>) => async (dispatch) => {
  const body = {
    type: ACTION_TYPES.PUT_TEMPLATES,
    payload: axiosInstance.put(templatesUrl, { templates: _.map(templates, toModel) })
  }
  await handleRequest(dispatch, body, Msg.GENERIC_SUCCESS, Msg.GENERIC_FAILURE);
  dispatch(getTemplates());
}

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
