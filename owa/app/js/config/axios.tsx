import axios from 'axios';

// According to OpenMRS convention, setting page to 0 will result in returning all records
export const ALL_RECORDS_PAGE = 0;

const apiBaseUrl = '/openmrs/';

const axiosInstance = axios.create({
  baseURL: apiBaseUrl,
  headers: {
    accept: 'application/json',
  },
});

export default axiosInstance;
