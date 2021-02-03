import axios from 'axios';
import { loginActions } from '@openmrs/react-components';

// According to OpenMRS convention, setting page to 0 will result in returning all records
export const ALL_RECORDS_PAGE = 0;
export const OPENMRS_CONTEXT_PATH = '/openmrs';
export const DOMAIN_PATH = window.location.protocol + '//' + window.location.host + OPENMRS_CONTEXT_PATH;
export const REDIRECT_URL = window.location.href;

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
      window.location.href = fullUrlAddress;
  } else {
      return Promise.reject(error);
  }
});

export default axiosInstance;
