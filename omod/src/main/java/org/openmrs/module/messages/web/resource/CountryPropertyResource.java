/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.web.resource;

import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.model.CountryProperty;
import org.openmrs.module.messages.api.service.CountryPropertyService;
import org.openmrs.module.messages.web.resource.countryProperty.CountryPropertyResourceDescriptionFactory;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.AlreadyPaged;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.IllegalPropertyException;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Resource(
    name = RestConstants.VERSION_1 + "/countryProperty",
    supportedClass = CountryProperty.class,
    supportedOpenmrsVersions = {"2.0.*", "2.1.*", "2.2.*", "2.3.*", "2.4.*"})
public class CountryPropertyResource extends DelegatingCrudResource<CountryProperty> {

  @Override
  public List<Representation> getAvailableRepresentations() {
    return Arrays.asList(Representation.DEFAULT, Representation.REF);
  }

  @Override
  public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
    if (representation instanceof RefRepresentation) {
      return CountryPropertyResourceDescriptionFactory.newRefRepresentation();
    } else if (representation instanceof DefaultRepresentation) {
      return CountryPropertyResourceDescriptionFactory.newDefaultRepresentation();
    }

    return null;
  }

  @Override
  public DelegatingResourceDescription getCreatableProperties()
      throws ResourceDoesNotSupportOperationException {
    return CountryPropertyResourceDescriptionFactory.newCreatableProperties();
  }

  @Override
  public DelegatingResourceDescription getUpdatableProperties()
      throws ResourceDoesNotSupportOperationException {
    return CountryPropertyResourceDescriptionFactory.newUpdatableProperties();
  }

  @PropertySetter("country")
  public static void setCountry(CountryProperty countryProperty, Object value) {
    final String countryNameOrUuid = getCountryNameOrUuid(value);

    Concept country = Context.getConceptService().getConceptByUuid(countryNameOrUuid);

    if (country == null) {
      country = Context.getConceptService().getConceptByName(countryNameOrUuid);
    }

    if (country == null) {
      throw new IllegalPropertyException(
          "Could not find Country Concept for: " + countryNameOrUuid);
    }

    countryProperty.setCountry(country);
  }

  private static String getCountryNameOrUuid(Object value) {
    final String valueText;

    if (value instanceof Map) {
      valueText = Objects.toString(((Map<String, Object>) value).get("uuid"));
    } else if (value instanceof String) {
      valueText = (String) value;
    } else {
      valueText = Objects.toString(value);
    }

    return valueText;
  }

  @Override
  public boolean isRetirable() {
    return true;
  }

  @Override
  public CountryProperty getByUniqueId(String uuid) {
    final CountryPropertyService countryPropertyService =
        Context.getService(CountryPropertyService.class);

    return countryPropertyService.getCountryPropertyByUuid(uuid).orElse(null);
  }

  @Override
  protected PageableResult doGetAll(RequestContext context) throws ResponseException {
    final CountryPropertyService countryPropertyService =
        Context.getService(CountryPropertyService.class);

    final List<CountryProperty> all =
        countryPropertyService.getAllCountryProperties(
            context.getIncludeAll(), context.getStartIndex(), context.getLimit());
    final long totalCount =
        countryPropertyService.getCountOfCountryProperties(context.getIncludeAll());
    final boolean hasMoreResults = totalCount > context.getStartIndex() + all.size();

    return new AlreadyPaged<>(context, all, hasMoreResults, totalCount);
  }

  @Override
  protected PageableResult doSearch(RequestContext context) {
    final CountryPropertyService countryPropertyService =
        Context.getService(CountryPropertyService.class);
    final String namePrefix = context.getParameter("q");

    final List<CountryProperty> searchResult =
        countryPropertyService.getAllCountryProperties(
            namePrefix, context.getIncludeAll(), context.getStartIndex(), context.getLimit());
    final long totalSearchCount =
        countryPropertyService.getCountOfCountryProperties(namePrefix, context.getIncludeAll());
    final boolean hasMoreResults = totalSearchCount > context.getStartIndex() + searchResult.size();

    return new AlreadyPaged<>(context, searchResult, hasMoreResults, totalSearchCount);
  }

  @Override
  protected void delete(
      CountryProperty countryProperty, String reason, RequestContext requestContext)
      throws ResponseException {
    final CountryPropertyService countryPropertyService =
        Context.getService(CountryPropertyService.class);

    countryPropertyService.retireCountryProperty(countryProperty, reason);
  }

  @Override
  public CountryProperty newDelegate() {
    return new CountryProperty();
  }

  @Override
  public CountryProperty save(CountryProperty countryProperty) {
    final CountryPropertyService countryPropertyService =
        Context.getService(CountryPropertyService.class);

    return countryPropertyService.saveCountryProperty(countryProperty);
  }

  @Override
  public void purge(CountryProperty countryProperty, RequestContext requestContext)
      throws ResponseException {
    final CountryPropertyService countryPropertyService =
        Context.getService(CountryPropertyService.class);

    countryPropertyService.purgeCountryProperty(countryProperty);
  }
}
