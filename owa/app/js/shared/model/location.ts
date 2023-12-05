/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

export interface ILocation {
  uuid?: string;
  name: string;
  description: string;
  address1: string;
  address2: string;
  cityVillage: string;
  stateProvince: string;
  country: string;
  postalCode: string;
  tags: Array<string>;
  attributes: Array<ILocationAttributeTypeValue>;
}

export interface ILocationAttributeType {
  datatypeClassname: string;
  datatypeConfig: string;
  description: string;
  display: string;
  handlerConfig: string;
  maxOccurs: number;
  minOccurs: number;
  name: string;
  preferredHandlerClassname: string;
  retired: boolean;
  uuid: string;
}

export interface ILocationAttributeTypeValue {
  attributeType: { uuid: string };
  value: string;
}

export interface ILocationListItem {
  uuid: string;
  display: string;
}

export interface ILocationState {
  loadingLocations: boolean;
  locations: Array<ILocationListItem>;
  errorMessage: string;
  locationAttributeTypes: Array<ILocationAttributeType>;
  loadingLocationAttributeTypes: boolean;
  success: boolean;
  loadingLocation: boolean;
  location: ILocation;
}
