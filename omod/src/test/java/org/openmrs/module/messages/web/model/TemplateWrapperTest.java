package org.openmrs.module.messages.web.model;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.messages.api.dto.TemplateDTO;
import org.openmrs.module.messages.builder.TemplateDTOBuilder;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TemplateWrapperTest {

    private List<TemplateDTO> templates;

    private List<TemplateDTO> templates2;

    private TemplateWrapper templateWrapper;

    private TemplateWrapper templateWrapper2;

    @Before
    public void setUp() {
        templates = Arrays.asList(new TemplateDTOBuilder().build());
        templates2 = Arrays.asList(new TemplateDTOBuilder().build());
    }

    @Test
    public void shouldCreateInstancesSuccessfully() {
        templateWrapper = new TemplateWrapper(templates);
        assertThat(templateWrapper, is(notNullValue()));
        assertEquals(templates, templateWrapper.getTemplates());

        templateWrapper2 = new TemplateWrapper();
        templateWrapper2.setTemplates(templates2);
        assertThat(templateWrapper2, is(notNullValue()));
        assertEquals(templates2, templateWrapper2.getTemplates());

        assertFalse(templateWrapper.equals(templateWrapper2));
    }
}
