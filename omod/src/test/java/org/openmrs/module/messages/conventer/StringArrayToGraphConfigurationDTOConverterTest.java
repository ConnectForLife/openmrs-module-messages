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
            "  \"dataDateRange\": 4,\n" +
            "  \"textQuestion\": \"Text Question\",\n" +
            "  \"possibleResponses\": [1,4,7,122],\n" +
            "  \"responseMode\": \"TEXT\",\n" +
            "  \"actorId\": 43,\n" +
            "  \"aggregateMode\": \"MONTH\",\n" +
            "  \"patientId\": 2,\n" +
            "  \"possibleTextResponses\": [\"response 1\",\"response 2\", \"response 3\"],\n" +
            "  \"questionId\": 1\n" +
            "}";

    private static final String INVALID_REPRESENTATION = "{\n" +
            "  \"id\": 4,\n" +
            "  \"textQuestion\": \"Text Question\",\n" +
            "  \"possibleResponses\": [1,4,7,122],\n" +
            "  \"responseMode\": \"TEXT\",\n" +
            "  \"actorId\": 43,\n" +
            "  \"aggregateMode\": \"MONTH\",\n" +
            "  \"patientId\": 2,\n" +
            "  \"possibleTextResponses\": [\"response 1\",\"response 2\", \"response 3\"],\n" +
            "  \"questionId\": 1\n" +
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
