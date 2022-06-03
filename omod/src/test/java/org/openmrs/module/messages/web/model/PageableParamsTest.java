/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.web.model;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.messages.domain.PagingInfo;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PageableParamsTest {

    private static final Integer ROWS = 10;

    private static final Integer ROWS_2 = 30;

    private static final Integer PAGE = 5;

    private static final Integer PAGE_2 = 15;

    private PageableParams pageableParams;

    private PageableParams pageableParams2;

    private PagingInfo pagingInfo;

    @Before
    public void setUp() {
        pageableParams = new PageableParams();
        pageableParams.setPage(PAGE);
        pageableParams.setRows(ROWS);
        pageableParams2 = new PageableParams();
        pageableParams2.setPage(PAGE_2);
        pageableParams2.setRows(ROWS_2);
        pagingInfo = new PagingInfo(PAGE, ROWS);
    }

    @Test
    public void shouldCreateInstanceSuccessfully() {
        assertThat(pageableParams, is(notNullValue()));
        assertEquals(PAGE, pageableParams.getPage());
        assertEquals(ROWS, pageableParams.getRows());

        assertThat(pagingInfo, is(notNullValue()));
        assertThat(pagingInfo, is(pageableParams.getPagingInfo()));

        assertThat(pageableParams2, is(notNullValue()));
        assertEquals((PAGE_2), pageableParams2.getPage());
        assertEquals((ROWS_2), pageableParams2.getRows());

        assertFalse(pageableParams.equals(pageableParams2));
    }
}
