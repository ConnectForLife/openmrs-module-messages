package org.openmrs.module.messages.domain.criteria;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public abstract class BaseOpenmrsDataCriteria extends BaseCriteria {

    @Override
    public void initHibernateCriteria(Criteria hibernateCriteria) {
        hibernateCriteria.add(Restrictions.eq("voided", false));
    }
}
