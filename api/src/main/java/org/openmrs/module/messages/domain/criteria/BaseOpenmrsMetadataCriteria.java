package org.openmrs.module.messages.domain.criteria;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public abstract class BaseOpenmrsMetadataCriteria extends BaseCriteria {

    private static final long serialVersionUID = -2226357681603039548L;

    @Override
    public void initHibernateCriteria(Criteria hibernateCriteria) {
        hibernateCriteria.add(Restrictions.eq("retired", Boolean.FALSE));
    }
}
