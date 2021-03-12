package org.openmrs.module.messages.domain.criteria;

import org.hibernate.Criteria;

import java.io.Serializable;

public abstract class BaseCriteria implements Serializable {

    private static final long serialVersionUID = 3288835048263351202L;

    /**
     * Loads the specific criteria conditions and configurations.
     *
     * @param hibernateCriteria related hibernate criteria
     */
    public abstract void loadHibernateCriteria(Criteria hibernateCriteria);

    /**
     * Inits the base restriction shared by all criteria.
     *
     * @param hibernateCriteria related hibernate criteria
     */
    public abstract void initHibernateCriteria(Criteria hibernateCriteria);
}
