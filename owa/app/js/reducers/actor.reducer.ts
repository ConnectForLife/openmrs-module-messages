import _ from 'lodash';

import { REQUEST, SUCCESS, FAILURE } from './action-type.util'
import 'react-toastify/dist/ReactToastify.css';
import axiosInstance from '../config/axios';
import { IActor } from '../shared/model/actor.model';

export const ACTION_TYPES = {
  GET_ACTORS: 'actorReducer/GET_ACTORS',
  RESET: 'actorReducer/RESET'
};

const initialState = {
  actorResultList: [] as Array<IActor>,
  actorResultListLoading: false
};

export type ActorState = Readonly<typeof initialState>;

export default (state = initialState, action) => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.GET_ACTORS):
      return {
        ...state,
        actorResultListLoading: true
      };
    case FAILURE(ACTION_TYPES.GET_ACTORS):
      return {
        ...state,
        actorResultListLoading: false
      };
    case SUCCESS(ACTION_TYPES.GET_ACTORS):
      return {
        ...state,
        actorResultListLoading: false,
        actorResultList: action.payload.data
      };
    case ACTION_TYPES.RESET:
      return {
        ..._.cloneDeep(initialState)
      };
    default:
      return state;
  }
};

const actorUrl = "ws/messages/actor/";

export const getActorList = (patientId: number) => async (dispatch) => {
  await dispatch({
    type: ACTION_TYPES.GET_ACTORS,
    payload: axiosInstance.get(actorUrl + patientId)
  });
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
