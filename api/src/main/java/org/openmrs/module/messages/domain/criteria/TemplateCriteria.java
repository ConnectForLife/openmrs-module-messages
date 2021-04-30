package org.openmrs.module.messages.domain.criteria;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;

public class TemplateCriteria extends BaseOpenmrsMetadataCriteria implements Serializable {

    private static final long serialVersionUID = -651234477563418680L;

    private final String name;

    TemplateCriteria(final String name) {
        this.name = name;
    }

    public static TemplateCriteria forName(final String name) {
        return new TemplateCriteria(name);
    }

    public static TemplateCriteria nonRetired() {
        return new TemplateCriteria(null);
    }

    @Override
    public void loadHibernateCriteria(Criteria hibernateCriteria) {
        if (name != null) {
            hibernateCriteria.add(Restrictions.eq("name", name));
        }
    }
}
