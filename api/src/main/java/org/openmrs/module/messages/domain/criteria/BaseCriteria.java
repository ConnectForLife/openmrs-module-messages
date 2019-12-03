package org.openmrs.module.messages.domain.criteria;

import org.hibernate.Criteria;

import java.io.Serializable;

public abstract class BaseCriteria implements Serializable {

    private static final long serialVersionUID = 3288835048263351202L;

    public abstract void loadHibernateCriteria(Criteria hibernateCriteria);
}
