/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.resources;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.service.ActorResponseService;
import org.openmrs.module.messages.web.resource.ActorResponseResource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class ActorResponseResourceTest {

  private final ActorResponseResource actorResponseRestResource = new ActorResponseResource();

  @Mock
  private ActorResponseService actorResponseService;

  @Before
  public void setUp() {
    mockStatic(Context.class);
    when(Context.getService(ActorResponseService.class)).thenReturn(actorResponseService);
  }

  @Test
  public void shouldGetAllAvailableRepresentations() {
    List<Representation> actual = actorResponseRestResource.getAvailableRepresentations();
    assertNotNull(actual);
    assertEquals(2, actual.size());
  }

  @Test
  public void shouldGetRefRepresentation() {
    DelegatingResourceDescription actual = actorResponseRestResource.getRepresentationDescription(new RefRepresentation());

    assertNotNull(actual);
    assertTrue(actual.getProperties().containsKey("id"));
    assertTrue(actual.getProperties().containsKey("patient"));
    assertTrue(actual.getProperties().containsKey("actor"));
    assertTrue(actual.getProperties().containsKey("textQuestion"));
    assertTrue(actual.getProperties().containsKey("textResponse"));
    assertTrue(actual.getProperties().containsKey("question"));
    assertTrue(actual.getProperties().containsKey("response"));
  }

  @Test
  public void shouldGetDefaultRepresentation() {
    DelegatingResourceDescription actual =
        actorResponseRestResource.getRepresentationDescription(new DefaultRepresentation());

    assertNotNull(actual);
    assertTrue(actual.getProperties().containsKey("id"));
    assertTrue(actual.getProperties().containsKey("patient"));
    assertTrue(actual.getProperties().containsKey("actor"));
    assertTrue(actual.getProperties().containsKey("textQuestion"));
    assertTrue(actual.getProperties().containsKey("textResponse"));
    assertTrue(actual.getProperties().containsKey("question"));
    assertTrue(actual.getProperties().containsKey("response"));
    assertTrue(actual.getProperties().containsKey("answeredTime"));
  }

  @Test
  public void shouldCreateCreatableProperties() {
    DelegatingResourceDescription actual = actorResponseRestResource.getCreatableProperties();
    assertNotNull(actual);
    assertTrue(actual.getProperties().containsKey("patient"));
    assertTrue(actual.getProperties().containsKey("actor"));
    assertTrue(actual.getProperties().containsKey("response"));
  }

  @Test
  public void shouldCreateUpdatableProperties() {
    DelegatingResourceDescription actual = actorResponseRestResource.getUpdatableProperties();
    assertNotNull(actual);
    assertTrue(actual.getProperties().containsKey("response"));
  }

  @Test
  public void shouldCheckIsRetirable() {
    assertTrue(actorResponseRestResource.isRetirable());
  }

}
