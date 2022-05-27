package org.openmrs.module.messages.web.resource.countryProperty;

import org.junit.Test;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CountryPropertyResourceDescriptionFactoryTest {

    @Test
    public void shouldCreateNewRefRepresentation() {
        DelegatingResourceDescription actual = CountryPropertyResourceDescriptionFactory.newRefRepresentation();

        assertNotNull(actual);
        assertTrue(actual.getProperties().containsKey("uuid"));
        assertTrue(actual.getProperties().containsKey("value"));
    }

    @Test
    public void shouldCreateNewDefaultRepresentation() {
        DelegatingResourceDescription actual = CountryPropertyResourceDescriptionFactory.newDefaultRepresentation();

        assertNotNull(actual);
        assertTrue(actual.getProperties().containsKey("uuid"));
        assertTrue(actual.getProperties().containsKey("value"));
        assertTrue(actual.getProperties().containsKey("name"));
        assertTrue(actual.getProperties().containsKey("country"));
        assertTrue(actual.getProperties().containsKey("description"));
    }

    @Test
    public void shouldCreateNewCreatableProperties() {
        DelegatingResourceDescription actual = CountryPropertyResourceDescriptionFactory.newCreatableProperties();

        assertNotNull(actual);
        assertTrue(actual.getProperties().containsKey("value"));
        assertTrue(actual.getProperties().containsKey("name"));
        assertTrue(actual.getProperties().containsKey("country"));
        assertTrue(actual.getProperties().containsKey("description"));
    }

    @Test
    public void shouldCreateNewUpdatableProperties() {
        DelegatingResourceDescription actual = CountryPropertyResourceDescriptionFactory.newUpdatableProperties();

        assertNotNull(actual);
        assertTrue(actual.getProperties().containsKey("value"));
    }
}
