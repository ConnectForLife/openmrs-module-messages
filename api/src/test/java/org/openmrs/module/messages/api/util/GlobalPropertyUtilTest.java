package org.openmrs.module.messages.api.util;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.openmrs.module.messages.BaseTest;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class GlobalPropertyUtilTest extends BaseTest {

    private static final String PROPERTY_NAME = "propertyName";

    private static final int EXPECTED_INT_VALUE = 123;

    private static final int EXPECTED_LIST_SIZE = 3;

    private static final String CORRECT_GP_LIST = "value 1,value 2 is very long, next,,";

    @Test
    public void parseBoolShouldParseTrue() {
        boolean actual = GlobalPropertyUtil.parseBool("tRuE");

        assertTrue(actual);
    }

    @Test
    public void parseBoolShouldParseFalse() {
        boolean actual = GlobalPropertyUtil.parseBool("fAlSe");

        assertFalse(actual);
    }

    @Test
    public void parseBoolShouldParseInvalidPropertyAsFalse() {
        boolean actual = GlobalPropertyUtil.parseBool("notBoolString");

        assertFalse(actual);
    }

    @Test
    public void parseBoolShouldParseNotSetPropertyAsFalse() {
        boolean actual = GlobalPropertyUtil.parseBool(null);

        assertFalse(actual);
    }

    @Test
    public void parseIntShouldParseIntegerValue() {
        int actual = GlobalPropertyUtil.parseInt(PROPERTY_NAME, "123");

        assertEquals(EXPECTED_INT_VALUE, actual);
    }

    @Test(expected = MessagesRuntimeException.class)
    public void parseIntShouldThrowExceptionIfPassedFloatValue() {
        GlobalPropertyUtil.parseInt(PROPERTY_NAME, "123.1");
    }

    @Test(expected = MessagesRuntimeException.class)
    public void parseIntShouldThrowExceptionIfPassedString() {
        GlobalPropertyUtil.parseInt(PROPERTY_NAME, "notIntString");
    }

    @Test(expected = MessagesRuntimeException.class)
    public void parseIntShouldThrowExceptionIfPropertyIsNotSet() {
        GlobalPropertyUtil.parseInt(PROPERTY_NAME, null);
    }

    @Test
    public void parseMapShouldParseMapWithTwoEntries() {
        Map<String, String> map = GlobalPropertyUtil.parseMap(
                PROPERTY_NAME, "channelType1:beanName1,channelType2:beanName2");

        assertEquals(2, map.size());
        assertThat(map, Matchers.hasEntry("channelType1".toUpperCase(), "beanName1"));
        assertThat(map, Matchers.hasEntry("channelType2".toUpperCase(), "beanName2"));
    }

    @Test
    public void parseMapShouldParseMapWithZeroEntries() {
        Map<String, String> map = GlobalPropertyUtil.parseMap(PROPERTY_NAME, "");

        assertEquals(0, map.size());
    }

    @Test
    public void parseMapShouldParseMapWithZeroEntriesIfPropertyIsNotSet() {
        Map<String, String> map = GlobalPropertyUtil.parseMap(PROPERTY_NAME, null);

        assertEquals(0, map.size());
    }

    @Test(expected = MessagesRuntimeException.class)
    public void parseMapShouldThrowExceptionIfInvalidEntryPassed() {
        GlobalPropertyUtil.parseMap(PROPERTY_NAME, "channelType1:beanName1,channelType2beanName2");
    }

    @Test
    public void parseListShouldReturnExpectedResults() {
        List<String> actual = GlobalPropertyUtil.parseList(CORRECT_GP_LIST, ",");
        assertThat(actual, Matchers.is(Matchers.notNullValue()));
        assertThat(actual.size(), Matchers.is(EXPECTED_LIST_SIZE));
    }

    @Test
    public void parseListShouldReturnOneString() {
        List<String> actual = GlobalPropertyUtil.parseList(CORRECT_GP_LIST, ".");
        assertThat(actual, Matchers.is(Matchers.notNullValue()));
        assertThat(actual.size(), Matchers.is(1));
        assertThat(actual.get(0), Matchers.is(CORRECT_GP_LIST));
    }

    @Test
    public void parseListShouldReturnInputIfDelimiterIsNull() {
        List<String> actual = GlobalPropertyUtil.parseList(CORRECT_GP_LIST, null);
        assertThat(actual, Matchers.is(Matchers.notNullValue()));
        assertThat(actual.size(), Matchers.is(1));
        assertThat(actual.get(0), Matchers.is(CORRECT_GP_LIST));
    }

    @Test
    public void parseListShouldReturnInputIfDelimiterIsEmpty() {
        List<String> actual = GlobalPropertyUtil.parseList(CORRECT_GP_LIST, "");
        assertThat(actual, Matchers.is(Matchers.notNullValue()));
        assertThat(actual.size(), Matchers.is(1));
        assertThat(actual.get(0), Matchers.is(CORRECT_GP_LIST));
    }
}
