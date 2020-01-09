package org.openmrs.module.messages.api.validate.validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.openmrs.module.messages.BaseTest;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.model.TemplateFieldType;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.api.service.TemplateFieldService;
import org.openmrs.module.messages.api.service.TemplateService;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import validate.validator.PatientTemplateValidator;

import javax.validation.ConstraintValidatorContext;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PatientTemplateValidator.class)
public class PatientTemplateValidatorTest extends BaseTest {

    private static final String DEFAULT_VALUE = "value1";
    private static final int DEFAULT_TEMPLATE_ID = 1;
    private static final int DEFAULT_TEMPLATE_FIELD_ID = 2;
    private static final String DEFAULT_CHANNEL_TYPE_VALUE = "SMS";

    private PatientTemplate patientTemplate;
    private TemplateField templateField;
    private Template template;
    private TemplateFieldValue templateFieldValue;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder validationBuilder;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderDefinedContext nodeBuilder;

    @Mock
    private TemplateFieldService templateFieldService;

    @Mock
    private TemplateService templateService;

    @InjectMocks
    private PatientTemplateValidator validator = new PatientTemplateValidator();

    @Before
    public void setUp() throws Exception {
        given(context.buildConstraintViolationWithTemplate(any())).willReturn(validationBuilder);
        given(validationBuilder.addNode(any())).willReturn(nodeBuilder);
        validator = PowerMockito.spy(new PatientTemplateValidator());
        PowerMockito.doReturn(templateService).when(validator, "getTemplateService");
        PowerMockito.doReturn(templateFieldService).when(validator, "getTemplateFieldService");
        prepareData();
    }

    @Test
    public void shouldBeInvalidWhenRequiredFieldIsEmpty() {
        templateField.setMandatory(true);

        Assert.assertFalse(validator.isValid(patientTemplate, context));
    }

    @Test
    public void shouldBeValidWhenRequiredFieldIsNotEmpty() {
        templateField.setMandatory(true);
        templateFieldValue.setValue(DEFAULT_VALUE);

        Assert.assertTrue(validator.isValid(patientTemplate, context));
    }

    @Test
    public void shouldBeValidWhenNoFieldIsRequired() {
        Assert.assertTrue(validator.isValid(patientTemplate, context));
    }

    @Test
    public void shouldBeInvalidWhenTemplateFieldTypeIsServiceButValueIsNotChannelType() {
        templateField.setTemplateFieldType(TemplateFieldType.SERVICE_TYPE);
        templateFieldValue.setValue(DEFAULT_CHANNEL_TYPE_VALUE);

        Assert.assertTrue(validator.isValid(patientTemplate, context));
    }

    private void prepareData() {
        patientTemplate = new PatientTemplate();
        templateFieldValue = new TemplateFieldValue();
        templateField = new TemplateField();
        templateField.setMandatory(false);
        templateField.setId(DEFAULT_TEMPLATE_FIELD_ID);
        templateField.setTemplateFieldType(TemplateFieldType.CATEGORY_OF_MESSAGE);
        template = new Template();
        template.setId(DEFAULT_TEMPLATE_ID);
        template.setTemplateFields(Collections.singletonList(templateField));
        patientTemplate.setTemplate(template);
        templateFieldValue.setTemplateField(templateField);
        when(templateFieldService.getById(any())).thenReturn(templateField);
        when(templateService.getById(any())).thenReturn(template);
        List<TemplateFieldValue> templateFieldValues = Collections.singletonList(templateFieldValue);
        patientTemplate.setTemplateFieldValues(templateFieldValues);
    }
}
