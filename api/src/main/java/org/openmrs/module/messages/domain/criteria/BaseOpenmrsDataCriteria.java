package org.openmrs.module.messages.domain.criteria;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public abstract class BaseOpenmrsDataCriteria extends BaseCriteria {

    private static final long serialVersionUID = -5751772712181349155L;

    @Override
    public void initHibernateCriteria(Criteria hibernateCriteria) {
        hibernateCriteria.add(Restrictions.eq("voided", Boolean.FALSE));
    }
}
