package org.openmrs.module.messages.web.search;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.Concept;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.service.CountryPropertyService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.resource.api.SearchConfig;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class CountryPropertySearchHandlerTest {

    @Mock
    private RequestContext requestContext;

    @Mock
    private ConceptService conceptService;

    @Mock
    private CountryPropertyService countryPropertyService;

    private CountryPropertySearchHandler searchHandler = new CountryPropertySearchHandler();

    @Before
    public void setUp() {
        mockStatic(Context.class);

        when(Context.getConceptService()).thenReturn(conceptService);
        when(Context.getService(CountryPropertyService.class)).thenReturn(countryPropertyService);
    }

    @Test
    public void shouldGetSearchConfig() {
        SearchConfig actual = searchHandler.getSearchConfig();

        assertNotNull(actual);
        assertEquals("default", actual.getId());
        assertEquals("v1/countryProperty", actual.getSupportedResource());
    }

    @Test
    public void shouldSearchByRequestContext() {
        when(conceptService.getConceptByName(anyString())).thenReturn(new Concept());
        when(countryPropertyService.getCountryProperty(any(Concept.class), anyString())).thenReturn(Optional.empty());

        searchHandler.search(requestContext);

        verify(requestContext).getParameter("countryName");
        verify(conceptService).getConceptByName(anyString());
        verify(countryPropertyService).getCountryProperty(any(Concept.class), anyString());
    }
}
