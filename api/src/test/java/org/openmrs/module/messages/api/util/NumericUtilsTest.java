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
