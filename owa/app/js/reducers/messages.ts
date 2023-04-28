/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

import axios from 'axios';

import {FAILURE, REQUEST, SUCCESS} from './action-type.util';

export const ACTION_TYPES = {
  GET_MESSAGES: 'messages/GET_MESSAGES'
};

const initialState = {
  loading: false,
  errorMessage: null,
  messages: []
};

const reducer = (state = initialState, action) => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.GET_MESSAGES):
      return {
        ...initialState,
        loading: true
      };
    case FAILURE(ACTION_TYPES.GET_MESSAGES):
      return {
        ...initialState,
        errorMessage: action.payload.message,
        loading: false
      };
    case SUCCESS(ACTION_TYPES.GET_MESSAGES):
      const messagesRaw = action.payload.data;
      const messages = typeof messagesRaw == 'object' ? messagesRaw : JSON.parse(messagesRaw);
      return {
        ...initialState,
        messages,
        loading: false
      };
    default:
      return state;
  }
};

export const getMessages = (locale = 'en') => {
  const requestUrl = `/openmrs/module/uicommons/messages/messages.json?localeKey=${locale}`;
  return {
    type: ACTION_TYPES.GET_MESSAGES,
    payload: axios.get(requestUrl)
  };
};

export default reducer;