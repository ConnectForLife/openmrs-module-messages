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
  if (401 === error.response.status) {
    loginActions.logout();
    window.location.href = getApiBaseUrl() + '/login.htm?redirectUrl=' + window.location.href
  } else {
      return Promise.reject(error);
  }
});

export default axiosInstance;
