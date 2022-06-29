/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.conventer;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.messages.model.GraphConfigurationDTO;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class StringArrayToGraphConfigurationDTOConverterTest {

    private static final String VALID_REPRESENTATION = "{\n" +
            "  \"graphDataSetName\": \"test\",\n" +
            "  \"actorId\": 43\n" +
            "}";

    private static final String INVALID_REPRESENTATION = "{\n" +
            "  \"id\": 4,\n" +
            "  \"actorId\": 43\n" +
            "}";

    private static final String[] VALID_ARRAY = {VALID_REPRESENTATION};

    private static final String[] INVALID_ARRAY = {INVALID_REPRESENTATION};

    private GraphConfigurationDTO expected;

    private StringArrayToGraphConfigurationDTOConverter converter;

    @Before
    public void setUp() throws IOException {
        expected = new ObjectMapper().readValue(VALID_REPRESENTATION, GraphConfigurationDTO.class);
        converter = new StringArrayToGraphConfigurationDTOConverter();
    }

    @Test
    public void convertShouldReturnNullWhenNullArray() {
        GraphConfigurationDTO actual = converter.convert(null);
        assertThat(actual, nullValue());
    }

    @Test
    public void convertShouldReturnNullWhenEmptyArray() {
        GraphConfigurationDTO actual = converter.convert(new String[0]);
        assertThat(actual, nullValue());
    }

    @Test
    public void convertShouldReturnNullWhenParsingError() {
        GraphConfigurationDTO actual = converter.convert(INVALID_ARRAY);
        assertThat(actual, nullValue());
    }

    @Test
    public void convertShouldReturnExpectedResult() {
        GraphConfigurationDTO actual = converter.convert(VALID_ARRAY);
        assertThat(actual, is(expected));
    }
}
