package org.openmrs.module.messages.domain.criteria;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;

public abstract class BaseCriteria implements Serializable {

    private static final long serialVersionUID = 3288835048263351202L;

    private boolean includeVoided;

    public abstract void loadHibernateCriteria(Criteria hibernateCriteria);

    public void initHibernateCriteria(Criteria hibernateCriteria) {
        if (!includeVoided) {
            hibernateCriteria.add(Restrictions.eq("voided", false));
        }
    }
}
