package org.openmrs.module.messages.domain.criteria;

import org.hibernate.Criteria;

import java.io.Serializable;

public class TemplateCriteria extends BaseOpenmrsMetadataCriteria implements Serializable {

    private static final long serialVersionUID = -651234477563418680L;

    @Override
    public void loadHibernateCriteria(Criteria hibernateCriteria) {
        // any specific action isn't required
    }
}
