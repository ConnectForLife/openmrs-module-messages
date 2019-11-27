package org.openmrs.module.messages.api.helper;

import org.openmrs.module.messages.api.model.Template;

public final class TemplateHelper {
    
    private TemplateHelper() {
    }
    
    public static Template createTestInstance() {
        Template template = new Template();
        template.setServiceQuery("example service query");
        template.setServiceQueryType("example service query type");
        
        return template;
    }
}
