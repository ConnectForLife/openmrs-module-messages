/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.domain.criteria;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * Represents a criteria for searching service/message in DB.
 */
public class ScheduledServiceCriteria extends BaseCriteria implements Serializable {

    private static final long serialVersionUID = -386120008842837370L;

    private List<Integer> ids;

    private Integer actorId;

    @Override
    public void loadHibernateCriteria(Criteria hibernateCriteria) {
        if (ids != null) {
            hibernateCriteria.add(Restrictions.in("id", ids));
        }
        if (actorId != null) {
            hibernateCriteria
                    .createAlias("group.actor", "actor")
                    .add(Restrictions.eq("actor.personId", actorId));
        }
    }

    public static ScheduledServiceCriteria forIds(List<Integer> ids) {
        ScheduledServiceCriteria scheduledServiceCriteria = new ScheduledServiceCriteria();
        scheduledServiceCriteria.ids = ids;
        return scheduledServiceCriteria;
    }

    public static ScheduledServiceCriteria forActorId(Integer actorId) {
        ScheduledServiceCriteria scheduledServiceCriteria = new ScheduledServiceCriteria();
        scheduledServiceCriteria.actorId = actorId;
        return scheduledServiceCriteria;
    }

    public List<Integer> getIds() {
        return Collections.unmodifiableList(ids);
    }

    public Integer getActorId() {
        return actorId;
    }
}
