package org.openmrs.module.messages.api.helper;

import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.model.TemplateFieldType;

public final class TemplateFieldHelper {
    
    private TemplateFieldHelper() {
    }
    
    public static TemplateField createTestInstace() {
        TemplateField templateField = new TemplateField();
        templateField.setName("example name");
        templateField.setMandatory(true);
        templateField.setDefaultValue("example default value");
        templateField.setTemplateFieldType(TemplateFieldType.SERVICE_TYPE);

        return templateField;
    }
    
}
