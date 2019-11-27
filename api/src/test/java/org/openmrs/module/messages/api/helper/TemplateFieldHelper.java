package org.openmrs.module.messages.api.helper;

import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.util.TestConstants;

public final class TemplateFieldHelper {
    
    private TemplateFieldHelper() {
    }
    
    public static TemplateField createTestInstace() {
        TemplateField templateField = new TemplateField();
        templateField.setName("example name");
        templateField.setMandatory(true);
        templateField.setDefaultValue("example default value");
        templateField.setValueConcept(Context.getConceptService().getConcept(TestConstants.TEST_CONCEPT_ID));
        
        return templateField;
    }
    
}
