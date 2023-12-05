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
import { ILocation, ILocationState } from '../shared/model/location';
import { FAILURE, REQUEST, SUCCESS } from './action-type.util';
import { AnyAction } from 'redux';

export const ACTION_TYPES = {
  SEARCH_LOCATIONS: 'location/SEARCH_LOCATIONS',
  GET_LOCATION: 'location/GET_LOCATION',
  GET_LOCATION_ATTRIBUTE_TYPES: 'location/GET_LOCATION_ATTRIBUTE_TYPES',
  POST_LOCATION: 'location/POST_LOCATION'
};

const initialState: ILocationState = {
  loadingLocations: false,
  locations: [],
  errorMessage: '',
  locationAttributeTypes: [],
  loadingLocationAttributeTypes: false,
  success: false,
  loadingLocation: false,
  location: { 
    name: '',
    description: '',
    address1: '',
    address2: '',
    cityVillage: '',
    stateProvince: '',
    country: '',
    postalCode: '',
    tags: [],
    attributes: []
  }
};

const reducer = (state = initialState, action: AnyAction) => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_LOCATIONS):
      return {
        ...state,
        loadingLocations: true
      };
    case REQUEST(ACTION_TYPES.GET_LOCATION):
      return {
        ...state,
        loadingLocation: true
      };
    case REQUEST(ACTION_TYPES.GET_LOCATION_ATTRIBUTE_TYPES):
      return {
        ...state,
        loadingLocationAttributeTypes: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_LOCATIONS):
      return {
        ...state,
        errorMessage: action.payload.message
      };
    case SUCCESS(ACTION_TYPES.SEARCH_LOCATIONS):
      return {
        ...state,
        loadingLocations: false,
        locations: action.payload.data.results
      };
    case SUCCESS(ACTION_TYPES.GET_LOCATION):
      return {
        ...state,
        loadingLocation: false,
        location: action.payload.data.results[0]
      };
    case SUCCESS(ACTION_TYPES.GET_LOCATION_ATTRIBUTE_TYPES):
      return {
        ...state,
        loadingLocationAttributeTypes: false,
        locationAttributeTypes: action.payload.data.results
      };
    case SUCCESS(ACTION_TYPES.POST_LOCATION):
      return {
        ...state,
        success: true
      };
    default:
      return state;
  }
};

// actions
export const searchLocations = (q?: string) => {
  const requestUrl = `/openmrs/ws/rest/v1/location${!!q ? '?q=' + q : ''}`;
  return {
    type: ACTION_TYPES.SEARCH_LOCATIONS,
    payload: axios.get(requestUrl)
  };
};

export const getLocation = (id: string) => ({
  type: ACTION_TYPES.GET_LOCATION,
  payload: axios.get(`/openmrs/ws/rest/v1/location?s=byId&id=${id}&v=full`)
});

export const getLocationAttributeTypes = () => ({
  type: ACTION_TYPES.GET_LOCATION_ATTRIBUTE_TYPES,
  payload: axios.get('/openmrs/ws/rest/v1/locationattributetype?v=full')
});

export const saveLocation = (location: ILocation) => ({
  type: ACTION_TYPES.POST_LOCATION,
  payload: axios.post(`/openmrs/ws/rest/v1/location${location.uuid ? '/' + location.uuid : ''}`, location)
});

export default reducer;
