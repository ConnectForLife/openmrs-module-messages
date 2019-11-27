package org.openmrs.module.messages.api.helper;

import org.openmrs.module.messages.api.model.TemplateFieldValue;

public final class TemplateFieldValueHelper {
    
    private TemplateFieldValueHelper() {
    }
    
    public static TemplateFieldValue createTestInstance() {
        TemplateFieldValue templateFieldValue = new TemplateFieldValue();
        templateFieldValue.setValue("example value");
        
        return templateFieldValue;
    }
}
