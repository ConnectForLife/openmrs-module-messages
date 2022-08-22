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
import DOMPurify from 'dompurify';

// According to OpenMRS convention, setting page to 0 will result in returning all records
export const ALL_RECORDS_PAGE = 0;
export const OPENMRS_CONTEXT_PATH = '/openmrs';
export const DOMAIN_PATH = window.location.protocol + '//' + window.location.host + OPENMRS_CONTEXT_PATH;
export const REDIRECT_URL = DOMPurify.sanitize(window.location.href);

const getApiBaseUrl = () => {
  const path = window.location.pathname;
  return path.substring(0, path.indexOf('/owa/'));
}

const axiosInstance = axios.create({
  baseURL: getApiBaseUrl(),
  headers: {
    accept: 'application/json',
  },
});

const isValidUrl = (url: string) => {
  return url.startsWith(DOMAIN_PATH) && url.indexOf(OPENMRS_CONTEXT_PATH) !== -1;
}

axiosInstance.interceptors.response.use(function (response) {
  return response;
}, function (error) {
    const isValidRedirectUrl = isValidUrl(REDIRECT_URL);
    const fullUrlAddress = DOMAIN_PATH + '/login.htm?redirectUrl=' + REDIRECT_URL;
    const isValidFullUrl = isValidUrl(fullUrlAddress);
    if (401 === error.response.status && isValidRedirectUrl && isValidFullUrl) {
      loginActions.logout();
      window.location.href = DOMPurify.sanitize(fullUrlAddress);
  } else {
      return Promise.reject(error);
  }
});

export default axiosInstance;
