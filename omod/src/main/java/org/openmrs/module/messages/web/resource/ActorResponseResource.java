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

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.model.ActorResponse;
import org.openmrs.module.messages.api.service.ActorResponseService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

import java.util.Arrays;
import java.util.List;

/**
 * {@link Resource} for ActorResponse, supporting standard CRUD operations
 */
@Resource(name = RestConstants.VERSION_1 + "/actorResponse", supportedClass = ActorResponse.class,
    supportedOpenmrsVersions = {"1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.0.*", "2.1.*", "2.2.*", "2.3.*", "2.4.*", "2.5.*",
        "2.6.*"})
public class ActorResponseResource extends DataDelegatingCrudResource<ActorResponse> {

  @Override
  public List<Representation> getAvailableRepresentations() {
    return Arrays.asList(Representation.DEFAULT, Representation.REF);
  }

  @Override
  public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
    if (rep instanceof FullRepresentation) {
      DelegatingResourceDescription description = new DelegatingResourceDescription();
      description.addProperty("id");
      description.addProperty("uuid");
      description.addProperty("display");
      description.addProperty("actor", Representation.FULL);
      description.addProperty("patient", Representation.FULL);
      description.addProperty("response", Representation.FULL);
      description.addProperty("textQuestion");
      description.addProperty("textResponse");
      description.addProperty("question", Representation.FULL);
      description.addProperty("answeredTime");
      description.addProperty("sourceType");
      description.addProperty("sourceId");
      description.addSelfLink();
      return description;
    } else {
      DelegatingResourceDescription description = new DelegatingResourceDescription();
      description.addProperty("id");
      description.addProperty("uuid");
      description.addProperty("display");
      description.addProperty("actor", Representation.REF);
      description.addProperty("patient", Representation.REF);
      description.addProperty("response", Representation.REF);
      description.addProperty("textQuestion");
      description.addProperty("textResponse");
      description.addProperty("question", Representation.REF);
      description.addProperty("answeredTime");
      description.addSelfLink();
      return description;
    }
  }

  /**
   * @see org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingResource#getCreatableProperties()
   */
  @Override
  public DelegatingResourceDescription getCreatableProperties() {
    DelegatingResourceDescription description = new DelegatingResourceDescription();
    description.addProperty("actor", Representation.FULL);
    description.addProperty("patient", Representation.FULL);
    description.addProperty("response", Representation.FULL);
    description.addProperty("textQuestion");
    description.addProperty("textResponse");
    description.addProperty("question", Representation.FULL);
    description.addProperty("answeredTime");
    description.addRequiredProperty("sourceType");
    description.addProperty("sourceId");
    return description;
  }

  /**
   * @throws org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException
   * @see org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingResource#getUpdatableProperties()
   */
  @Override
  public DelegatingResourceDescription getUpdatableProperties() throws ResourceDoesNotSupportOperationException {
    return getCreatableProperties();
  }

  @Override
  public Model getGETModel(Representation rep) {
    ModelImpl model = (ModelImpl) super.getGETModel(rep);
    if (rep instanceof FullRepresentation) {
      model
          .property("id", new StringProperty())
          .property("uuid", new StringProperty())
          .property("display", new StringProperty())
          .property("actor", new RefProperty("#/definitions/PersonFull"))
          .property("patient", new RefProperty("#/definitions/PatientFull"))
          .property("response", new RefProperty("#/definitions/ConceptFull"))
          .property("textQuestion", new StringProperty())
          .property("textResponse", new StringProperty())
          .property("question", new RefProperty("#/definitions/ConceptFull"))
          .property("answeredTime", new StringProperty())
          .property("sourceType", new StringProperty())
          .property("sourceId", new StringProperty());
    } else {
      model
          .property("id", new StringProperty())
          .property("uuid", new StringProperty())
          .property("display", new StringProperty())
          .property("actor", new RefProperty("#/definitions/ActorRef"))
          .property("patient", new RefProperty("#/definitions/PatientRef"))
          .property("response", new RefProperty("#/definitions/ConceptRef"))
          .property("textQuestion", new StringProperty())
          .property("textResponse", new StringProperty())
          .property("question", new RefProperty("#/definitions/ConceptRef"))
          .property("answeredTime", new StringProperty());
    }

    return model;
  }

  @Override
  public Model getCREATEModel(Representation representation) {
    return new ModelImpl()
        .property("actor", new RefProperty("#/definitions/ActorRef"))
        .property("patient", new RefProperty("#/definitions/PatientRef"))
        .property("response", new RefProperty("#/definitions/ConceptRef"))
        .property("textQuestion", new StringProperty())
        .property("textResponse", new StringProperty())
        .property("question", new RefProperty("#/definitions/ConceptRef"))
        .property("answeredTime", new StringProperty())
        .property("sourceType", new StringProperty())
        .property("sourceId", new StringProperty());
  }

  @Override
  public Model getUPDATEModel(Representation representation) {
    return getCREATEModel(representation);
  }

  @Override
  public ActorResponse newDelegate() {
    return new ActorResponse();
  }

  @Override
  public boolean isRetirable() {
    return true;
  }

  @Override
  public ActorResponse save(ActorResponse delegate) {
    return Context.getService(ActorResponseService.class).saveOrUpdate(delegate);
  }

  @PropertyGetter("display")
  public String getDisplayString(ActorResponse response) {
    return response.getResponse() != null ? response.getResponse().getName(Context.getLocale()).getName() :
        response.getTextResponse();
  }

  @Override
  protected void delete(ActorResponse response, String reason, RequestContext context) throws ResponseException {
    if (response != null) {
      Context.getService(ActorResponseService.class).delete(response);
    }
  }

  @Override
  public ActorResponse getByUniqueId(String uuid) {
    return Context.getService(ActorResponseService.class).getByUuid(uuid);
  }

  @Override
  public void purge(ActorResponse delegate, RequestContext context) throws ResponseException {
    if (delegate != null) {
      Context.getService(ActorResponseService.class).delete(delegate);
    }
  }

  @Override
  protected PageableResult doGetAll(RequestContext context) throws ResponseException {
    throw new ResourceDoesNotSupportOperationException();
  }
}
