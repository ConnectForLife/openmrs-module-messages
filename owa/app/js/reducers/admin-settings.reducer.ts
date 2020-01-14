import { REQUEST, SUCCESS, FAILURE } from './action-type.util';
import { handleRequest } from '@bit/soldevelo-omrs.cfl-components.request-toast-handler';
import _ from 'lodash';

import 'react-toastify/dist/ReactToastify.css';
import { TemplateUI } from '../shared/model/template-ui';
import axiosInstance from '../config/axios';
import { toModel, mergeWithObjectUIs } from '../shared/model/object-ui';
import * as Msg from '../shared/utils/messages';

export const ACTION_TYPES = {
  GET_TEMPLATES: 'adminSettingsReducer/GET_TEMPLATES',
  UPDATE_TEMPLATES: 'adminSettingsReducer/UPDATE_TEMPLATE',
  PUT_TEMPLATES: 'adminSettingsReducer/PUT_TEMPLATE',
  RESET: 'adminSettingsReducer/RESET'
};

const initialState = {
  defaultTemplates: [] as Array<TemplateUI>,
  loading: false
};

export type AdminSettingsState = Readonly<typeof initialState>;

export default (state = initialState, action): AdminSettingsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.GET_TEMPLATES):
    case REQUEST(ACTION_TYPES.PUT_TEMPLATES):
      return {
        ...state,
        loading: true
      };
    case FAILURE(ACTION_TYPES.GET_TEMPLATES):
    case FAILURE(ACTION_TYPES.PUT_TEMPLATES):
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
    case SUCCESS(ACTION_TYPES.PUT_TEMPLATES):
      return {
        ...state,
        loading: false
      }
    case ACTION_TYPES.UPDATE_TEMPLATES:
      return {
        ...state,
        defaultTemplates: <Array<TemplateUI>> mergeWithObjectUIs(state.defaultTemplates, action.payload as TemplateUI)
      };
    case ACTION_TYPES.RESET:
      return {
        ..._.cloneDeep(initialState)
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
