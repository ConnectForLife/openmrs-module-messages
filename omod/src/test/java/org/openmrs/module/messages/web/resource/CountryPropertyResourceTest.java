package org.openmrs.module.messages.web.resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.Concept;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.model.CountryProperty;
import org.openmrs.module.messages.api.service.CountryPropertyService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.IllegalPropertyException;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class CountryPropertyResourceTest {

    private final CountryPropertyResource countryPropertyResource = new CountryPropertyResource();

    @Mock
    private ConceptService conceptService;

    @Mock
    private CountryPropertyService countryPropertyService;

    @Mock
    private RequestContext requestContext;

    @Before
    public void setUp() {
        mockStatic(Context.class);

        when(Context.getConceptService()).thenReturn(conceptService);
        when(Context.getService(CountryPropertyService.class)).thenReturn(countryPropertyService);
    }

    @Test
    public void shouldGetAvailableRepresentations() {
        List<Representation> actual = countryPropertyResource.getAvailableRepresentations();

        assertNotNull(actual);
        assertEquals(2, actual.size());
    }

    @Test
    public void shouldGetRefRepresentation() {
        DelegatingResourceDescription actual = countryPropertyResource.getRepresentationDescription(new RefRepresentation());

        assertNotNull(actual);
        assertTrue(actual.getProperties().containsKey("uuid"));
        assertTrue(actual.getProperties().containsKey("value"));
    }

    @Test
    public void shouldGetDefaultRepresentation() {
        DelegatingResourceDescription actual = countryPropertyResource.getRepresentationDescription(new DefaultRepresentation());

        assertNotNull(actual);
        assertTrue(actual.getProperties().containsKey("uuid"));
        assertTrue(actual.getProperties().containsKey("value"));
        assertTrue(actual.getProperties().containsKey("name"));
        assertTrue(actual.getProperties().containsKey("country"));
        assertTrue(actual.getProperties().containsKey("description"));
    }

    @Test
    public void shouldReturnNullWhenRepresentationIsDifferentThanRefAndDefault() {
        DelegatingResourceDescription actual = countryPropertyResource.getRepresentationDescription(new FullRepresentation());

        assertNull(actual);
    }

    @Test
    public void shouldCreateCreatableProperties() {
        DelegatingResourceDescription actual = countryPropertyResource.getCreatableProperties();

        assertNotNull(actual);
        assertTrue(actual.getProperties().containsKey("value"));
        assertTrue(actual.getProperties().containsKey("name"));
        assertTrue(actual.getProperties().containsKey("country"));
        assertTrue(actual.getProperties().containsKey("description"));
    }

    @Test
    public void shouldCreateUpdatableProperties() {
        DelegatingResourceDescription actual = countryPropertyResource.getUpdatableProperties();

        assertNotNull(actual);
        assertTrue(actual.getProperties().containsKey("value"));
    }

    @Test
    public void shouldSetCountryWhenCountryConceptFoundByUuid() {
        when(conceptService.getConceptByUuid(anyString())).thenReturn(new Concept());

        CountryPropertyResource.setCountry(new CountryProperty(), "Spain");

        verify(conceptService).getConceptByUuid("Spain");
    }

    @Test
    public void shouldSetCountryWhenCountryConceptFoundByName() {
        when(conceptService.getConceptByUuid(anyString())).thenReturn(null);
        when(conceptService.getConceptByName(anyString())).thenReturn(new Concept());

        CountryPropertyResource.setCountry(new CountryProperty(), "Spain");

        verify(conceptService).getConceptByUuid("Spain");
        verify(conceptService).getConceptByName("Spain");
    }

    @Test(expected = IllegalPropertyException.class)
    public void shouldThrowExceptionWhenCountryNotFound() {
        when(conceptService.getConceptByUuid(anyString())).thenReturn(null);
        when(conceptService.getConceptByName(anyString())).thenReturn(null);

        CountryPropertyResource.setCountry(new CountryProperty(), "Spain");
    }

    @Test
    public void shouldCheckIsRetirable() {
        assertTrue(countryPropertyResource.isRetirable());
    }

    @Test
    public void shouldGetCountryPropertyByUniqueId() {
        when(countryPropertyService.getCountryPropertyByUuid(anyString())).thenReturn(Optional.empty());

        countryPropertyResource.getByUniqueId(anyString());

        verify(countryPropertyService).getCountryPropertyByUuid(anyString());
    }

    @Test
    public void shouldDoGetAll() {
        countryPropertyResource.doGetAll(requestContext);

        verify(countryPropertyService).getAllCountryProperties(requestContext.getIncludeAll(), requestContext.getStartIndex(), requestContext.getLimit());
    }

    @Test
    public void shouldDoSearch() {
        when(requestContext.getParameter("q")).thenReturn("testPrefix");

        countryPropertyResource.doSearch(requestContext);

        verify(requestContext).getParameter("q");
        verify(countryPropertyService).getAllCountryProperties("testPrefix", requestContext.getIncludeAll(), requestContext.getStartIndex(), requestContext.getLimit());
        verify(countryPropertyService).getCountOfCountryProperties("testPrefix", requestContext.getIncludeAll());
    }

    @Test
    public void shouldDeleteCountryProperty() {
        countryPropertyResource.delete(new CountryProperty(), "testReason", requestContext);

        verify(countryPropertyService).retireCountryProperty(any(CountryProperty.class), anyString());
    }

    @Test
    public void shouldCreateNewDelegate() {
        CountryProperty actual = countryPropertyResource.newDelegate();

        assertNotNull(actual);
    }

    @Test
    public void shouldSaveCountryProperty() {
        countryPropertyResource.save(any(CountryProperty.class));

        verify(countryPropertyService).saveCountryProperty(any(CountryProperty.class));
    }

    @Test
    public void shouldPurgeCountryProperty() {
        countryPropertyResource.purge(any(CountryProperty.class), requestContext);

        verify(countryPropertyService).purgeCountryProperty(any(CountryProperty.class));
    }
}
