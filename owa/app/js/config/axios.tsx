import axios from 'axios';

const apiBaseUrl = '/openmrs/';

const axiosInstance = axios.create({
  baseURL: apiBaseUrl,
  headers: {
    accept: 'application/json',
  },
});

export default axiosInstance;
