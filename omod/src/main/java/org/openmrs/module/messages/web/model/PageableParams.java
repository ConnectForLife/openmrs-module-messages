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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.openmrs.module.messages.domain.PagingInfo;

/**
 * Models the messaging controller parameters
 */
public class PageableParams {

    /**
     * The default page to display.
     */
    private static final int DEFAULT_PAGE_INDEX = 1;

    /**
     * The default value of number of rows to display per page.
     */
    private static final int DEFAULT_PAGE_SIZE = 100;

    /**
     * The number of rows to display per page.
     */
    private Integer rows;

    /**
     * The page to display, starting from 1.
     */
    private Integer page;

    /**
     * Gets {@link PagingInfo} containing the paging configuration form grid settings
     * @return the newly created paging information
     */
    public PagingInfo getPagingInfo() {
        Integer pageIndex = getPage();
        Integer pageSize = getRows();
        return new PagingInfo(pageIndex != null ? pageIndex : DEFAULT_PAGE_INDEX,
                pageSize != null ? pageSize : DEFAULT_PAGE_SIZE);
    }

    public Integer getRows() {
        return rows;
    }

    public PageableParams setRows(Integer rows) {
        this.rows = rows;
        return this;
    }

    public Integer getPage() {
        return page;
    }

    public PageableParams setPage(Integer page) {
        this.page = page;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
