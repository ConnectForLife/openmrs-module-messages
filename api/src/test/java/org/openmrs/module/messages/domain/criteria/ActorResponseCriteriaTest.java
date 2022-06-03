/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.domain.criteria;

import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.internal.CriteriaImpl;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ActorResponseCriteriaTest {

    public static final int EXPECTED_PROJECTION_SIZE = 4;
    public static final String EXPECTED_PROJECTION = "[WEEK({alias}.answered_time, 1) AS over, count(textResponse) " +
            "as responseCount, response.conceptId as response, response.conceptId]";
    public static final String EXPECTED_CRITERIA = "CriteriaImpl(test:this[][{alias}.answered_time > " +
            "DATE(CURRENT_DATE() - DAYOFWEEK(CURRENT_DATE() - 1) - INTERVAL ? WEEK)])";

    @Test
    public void shouldBuildExpectedProjectionList() {
        ProjectionList projectionList = new ActorResponseCriteria().createProjectionList();
        assertThat(projectionList.getLength(), is(EXPECTED_PROJECTION_SIZE));
        assertThat(projectionList.toString(), is(EXPECTED_PROJECTION));
    }

    @Test
    public void shouldLoadExpectedWhereStatements() {
        Criteria criteria = new CriteriaImpl("test", null);
        new ActorResponseCriteria().loadWhereStatements(criteria);
        assertThat(criteria.toString(), is(EXPECTED_CRITERIA));
    }
}
