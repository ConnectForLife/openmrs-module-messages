/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.domain.criteria;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Represents a criteria for searching service/message in DB.
 */
public class ScheduledServiceCriteria extends BaseCriteria implements Serializable {

    private static final long serialVersionUID = -386120008842837370L;

    private List<Integer> ids;

    private Integer patientTemplatePatientId;
    
    private Integer patientTemplateActorId;
    
    private Integer groupId;
    
    private static final String PERSON_ID_FIELD = "personId";
    
    private static final String ACTOR_FIELD = "actor";
    
    private static final String PATIENT_FIELD = "patient";
    
    private static final String GROUP_FIELD = "group";
    
    private static final String ID_FIELD = "id";
    
    private static final String PATIENT_TEMPLATE_FIELD = "patientTemplate";

    @Override
    public void loadHibernateCriteria(Criteria hibernateCriteria) {
        if (ids != null) {
            hibernateCriteria.add(Restrictions.in(ID_FIELD, ids));
        }
        if (patientTemplatePatientId != null) {
            hibernateCriteria
                    .createAlias(PATIENT_TEMPLATE_FIELD + "." + PATIENT_FIELD, PATIENT_FIELD)
                    .add(Restrictions.eq(PATIENT_FIELD + "." + PERSON_ID_FIELD, patientTemplatePatientId));
        }
        if (patientTemplateActorId != null) {
            hibernateCriteria
                    .createAlias(PATIENT_TEMPLATE_FIELD + "." + ACTOR_FIELD, ACTOR_FIELD)
                    .add(Restrictions.eq(ACTOR_FIELD + "." + PERSON_ID_FIELD, patientTemplateActorId));
        }
        if (groupId != null) {
            hibernateCriteria.add(Restrictions.eq(GROUP_FIELD + "." + ID_FIELD, groupId));
        }
    }

    public static ScheduledServiceCriteria forIds(List<Integer> ids) {
        ScheduledServiceCriteria scheduledServiceCriteria = new ScheduledServiceCriteria();
        scheduledServiceCriteria.ids = ids;
        return scheduledServiceCriteria;
    }

    public static ScheduledServiceCriteria forActorAndPatientIds(Integer actorId, Integer patientId) {
        ScheduledServiceCriteria scheduledServiceCriteria = new ScheduledServiceCriteria();
        scheduledServiceCriteria.patientTemplateActorId = actorId;
        scheduledServiceCriteria.patientTemplatePatientId = patientId;
        return scheduledServiceCriteria;
    }
    
    public static ScheduledServiceCriteria forPatientTemplatePatientId(Integer patientId) {
        ScheduledServiceCriteria scheduledServiceCriteria = new ScheduledServiceCriteria();
        scheduledServiceCriteria.patientTemplatePatientId = patientId;
        return scheduledServiceCriteria;
    }
    
    public static ScheduledServiceCriteria forPatientTemplateActorId(Integer patientTemplateActorId) {
        ScheduledServiceCriteria scheduledServiceCriteria = new ScheduledServiceCriteria();
        scheduledServiceCriteria.patientTemplateActorId = patientTemplateActorId;
        return scheduledServiceCriteria;
    }
    
    public static ScheduledServiceCriteria forGroupId(Integer groupId) {
        ScheduledServiceCriteria scheduledServiceCriteria = new ScheduledServiceCriteria();
        scheduledServiceCriteria.groupId = groupId;
        return scheduledServiceCriteria;
    }
    
    public List<Integer> getIds() {
        return Collections.unmodifiableList(ids);
    }
}
