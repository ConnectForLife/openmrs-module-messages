/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.util;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.openmrs.module.messages.BaseTest;

import static org.junit.Assert.assertThat;

public class NumericUtilsTest extends BaseTest {

    @Test
    public void shouldReturnFalseWhenNull() {
        boolean actual = NumericUtils.isPositive(null);
        assertThat(false, Matchers.is(actual));
    }

    @Test
    public void shouldReturnFalseWhenZero() {
        boolean actual = NumericUtils.isPositive(0);
        assertThat(false, Matchers.is(actual));
    }

    @Test
    public void shouldReturnFalseWhenNegative() {
        boolean actual = NumericUtils.isPositive(-1);
        assertThat(false, Matchers.is(actual));
    }

    @Test
    public void shouldReturnTrueWhenGreaterThenZero() {
        boolean actual = NumericUtils.isPositive(1);
        assertThat(true, Matchers.is(actual));
    }
}
