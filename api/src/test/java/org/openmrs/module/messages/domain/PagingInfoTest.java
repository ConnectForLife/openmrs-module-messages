package org.openmrs.module.messages.domain;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class PagingInfoTest {

    private static final int PAGE = 1;

    private static final int PAGE_SIZE = 50;

    private static final Long TOTAL_COUNT = 1000L;

    private static final boolean IS_LOAD_RECORD_COUNT = true;

    private PagingInfo pagingInfo;

    private PagingInfo pagingInfo2;

    @Before
    public void setUp() {
        pagingInfo = new PagingInfo(PAGE, PAGE_SIZE);
        pagingInfo2 = new PagingInfo();
    }

    @Test
    public void shouldCreateInstanceSuccessfully() {
        assertThat(pagingInfo, is(notNullValue()));
        assertEquals(PAGE, pagingInfo.getPage());
        assertEquals(PAGE_SIZE, pagingInfo.getPageSize());
    }

    @Test
    public void shouldCreateInstanceSuccessfully2() {
        pagingInfo2.setPage(PAGE);
        pagingInfo2.setPageSize(PAGE_SIZE);
        pagingInfo2.setTotalRecordCount(TOTAL_COUNT);
        pagingInfo2.setLoadRecordCount(IS_LOAD_RECORD_COUNT);

        assertThat(pagingInfo2, is(notNullValue()));
        assertEquals(PAGE, pagingInfo2.getPage());
        assertEquals(PAGE_SIZE, pagingInfo2.getPageSize());
        assertEquals(TOTAL_COUNT, pagingInfo2.getTotalRecordCount());
        assertEquals(IS_LOAD_RECORD_COUNT, pagingInfo2.shouldLoadRecordCount());
        assertTrue(pagingInfo2.hasMoreResults());
        assertFalse(pagingInfo.equals(pagingInfo2));
    }

}
