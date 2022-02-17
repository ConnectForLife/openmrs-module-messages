package org.openmrs.module.messages.web.search;

import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.model.CountryProperty;
import org.openmrs.module.messages.api.service.CountryPropertyService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.api.SearchConfig;
import org.openmrs.module.webservices.rest.web.resource.api.SearchHandler;
import org.openmrs.module.webservices.rest.web.resource.api.SearchQuery;
import org.openmrs.module.webservices.rest.web.resource.impl.AlreadyPaged;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

import java.util.Collections;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

/** Finds CountryProperty by country name and property name. */
public class CountryPropertySearchHandler implements SearchHandler {
  private static final String COUNTRY_NAME_PARAM = "countryName";
  private static final String NAME_PARAM = "name";

  private final SearchConfig searchConfig =
      new SearchConfig(
          "default",
          RestConstants.VERSION_1 + "/countryProperty",
          asList("2.0.*", "2.1.*", "2.2.*", "2.3.*", "2.4.*"),
          singletonList(
              new SearchQuery.Builder(
                      "Allows you to find Country Property by country name and property name.")
                  .withRequiredParameters(COUNTRY_NAME_PARAM, NAME_PARAM)
                  .build()));

  @Override
  public SearchConfig getSearchConfig() {
    return searchConfig;
  }

  @Override
  public PageableResult search(RequestContext requestContext) throws ResponseException {
    final String countryName = requestContext.getParameter(COUNTRY_NAME_PARAM);
    final String name = requestContext.getParameter(NAME_PARAM);

    final Concept countryConcept = Context.getConceptService().getConceptByName(countryName);

    final Optional<CountryProperty> countryProperty =
        Context.getService(CountryPropertyService.class).getCountryProperty(countryConcept, name);

    return new AlreadyPaged<>(
        requestContext, countryProperty.map(Collections::singletonList).orElse(emptyList()), false);
  }
}
