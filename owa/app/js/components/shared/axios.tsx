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
import { loginActions } from '@openmrs/react-components';

// According to OpenMRS convention, setting page to 0 will result in returning all records
export const ALL_RECORDS_PAGE = 0;

const getApiBaseUrl = () => {
  const path = window.location.pathname;
  return path.substring(0, path.indexOf('/owa/')) + '/';
}

const axiosInstance = axios.create({
  baseURL: getApiBaseUrl(),
  headers: {
    accept: 'application/json',
  },
});

axiosInstance.interceptors.response.use(function (response) {
  return response;
}, function (error) {
  if (401 === error.response.status || 403 === error.response.status) {
    redirectToLoginPage();
  } else if (isUnauthorizedRestResponse(error.response)) {
      redirectToLoginPage();
  } else {
    return Promise.reject(error);
  }
});

const redirectToLoginPage = () => {
  loginActions.logout();
  window.location.href = getApiBaseUrl() + '/login.htm?redirectUrl=' + window.location.href;
}

// verified if server response store the unauthorized message
const isUnauthorizedRestResponse = (response: any) => {
  return 500 === response.status && response.data
  && response.data.errorMessages && response.data.errorMessages.length
  && response.data.errorMessages[0].message.includes('Privileges required')
}

export default axiosInstance;
