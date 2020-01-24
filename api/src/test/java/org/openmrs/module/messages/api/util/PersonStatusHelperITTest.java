package org.openmrs.module.messages.api.util;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.PersonService;
import org.openmrs.api.ValidationException;
import org.openmrs.module.messages.Constant;
import org.openmrs.module.messages.ContextSensitiveWithActivatorTest;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.messages.api.dto.PersonStatusDTO;
import org.openmrs.module.messages.api.model.PersonStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class PersonStatusHelperITTest extends ContextSensitiveWithActivatorTest {

    private static final String XML_DATASET_PATH = "datasets/";

    private static final String XML_PERSON_ATTRIBUTE_DATASET = XML_DATASET_PATH + "PersonAttributeDataset.xml";

    private static final String PERSON_UUID = "d64b7f82-5857-48f5-8694-b1bdb46688c3";

    private static final String PERSON_WITH_WRONG_ATTRIBUTE_VALUE_UUID = "69962c4a-ad29-4e9b-951b-2a27079f2b25";

    private static final String VOIDED_STATUS = PersonStatus.NO_CONSENT.name();

    private static final String ACTUAL_STATUS = PersonStatus.ACTIVATED.name();

    private static final String WRONG_STATUS = "No consent";

    private static final String EXPECTED_ACTIVATED_STYLE =
            "background-color: #51a351; border-color: #51a351; color: #f5f5f5;";

    private static final String EXPECTED_MISSING_STYLE =
            "background-color: #EEA616; border-color: #EEA616; color: #f5f5f5;";

    private static final String EXPECTED_DEACTIVATE_STYLE =
            "background-color: #f23722; border-color: #f23722; color: #f5f5f5;";

    private static final String NOT_EXIST_PERSON = "8124263d-d1ec-4be5-bf62-502ab125d076";

    private static final String PERSON_WITHOUT_ATTRIBUTE = "8c8169be-94f0-42ba-81de-cf3f2c12a7ec";

    private static final String TEST_INVALID_REASON = "The status was changed for test reason";

    private static final String NO_VALID_VALUE = "No valid";

    private static final int EXPECTED_REASON_SIZE = 6;

    private Person person;

    private Person personWithWrongAttributeValue;

    private List<String> expectedReasons;

    @Autowired
    @Qualifier("personService")
    private PersonService personService;

    @Autowired
    @Qualifier("messages.personStatusHelper")
    private PersonStatusHelper personStatusHelper;

    @Before
    public void setUp() throws Exception {
        executeDataSet(XML_PERSON_ATTRIBUTE_DATASET);
        person = personService.getPersonByUuid(PERSON_UUID);
        PersonAttributeType personStatusAttributeType = personService.getPersonAttributeTypeByUuid(
                ConfigConstants.PERSON_STATUS_ATTRIBUTE_TYPE_UUID);

        PersonAttribute voidedStatus = new PersonAttribute(personStatusAttributeType, VOIDED_STATUS);
        person.addAttribute(voidedStatus);
        PersonAttribute actualStatus = new PersonAttribute(personStatusAttributeType, ACTUAL_STATUS);
        person.addAttribute(actualStatus);
        person = personService.savePerson(person);

        personWithWrongAttributeValue = personService.getPersonByUuid(PERSON_WITH_WRONG_ATTRIBUTE_VALUE_UUID);
        PersonAttribute wrongStatus = new PersonAttribute(personStatusAttributeType, WRONG_STATUS);
        personWithWrongAttributeValue.addAttribute(wrongStatus);
        personWithWrongAttributeValue = personService.savePerson(personWithWrongAttributeValue);

        expectedReasons = new ArrayList<>();
        expectedReasons.add(Constant.STATUS_REASON_DECEASED);
        expectedReasons.add(Constant.STATUS_REASON_LOST_FOLLOW_UP);
        expectedReasons.add(Constant.STATUS_REASON_PAUSE);
        expectedReasons.add(Constant.STATUS_REASON_VACATION);
        expectedReasons.add(Constant.STATUS_REASON_TRANSFERRED);
        expectedReasons.add(Constant.STATUS_REASON_OTHER);
    }

    @Test
    public void shouldReturnExpectedStatusByPersonId() {
        PersonStatusDTO actual = personStatusHelper.getStatus(person.getId().toString());
        assertThat(actual, is(notNullValue()));
        assertThat(actual, hasProperty("personId", is(person.getId().toString())));
        assertThat(actual, hasProperty("title", is(PersonStatus.ACTIVATED.getTitleKey())));
        assertThat(actual, hasProperty("value", is(PersonStatus.ACTIVATED.name())));
        assertThat(actual, hasProperty("style", is(EXPECTED_ACTIVATED_STYLE)));
        assertThat(actual, hasProperty("reason", is(nullValue())));
    }

    @Test
    public void shouldReturnExpectedStatusByPersonUuid() {
        PersonStatusDTO actual = personStatusHelper.getStatus(person.getUuid());
        assertThat(actual, is(notNullValue()));
        assertThat(actual, hasProperty("personId", is(person.getUuid())));
        assertThat(actual, hasProperty("title", is(PersonStatus.ACTIVATED.getTitleKey())));
        assertThat(actual, hasProperty("value", is(PersonStatus.ACTIVATED.name())));
        assertThat(actual, hasProperty("style", is(EXPECTED_ACTIVATED_STYLE)));
        assertThat(actual, hasProperty("reason", is(nullValue())));
    }

    @Test
    public void shouldReturnNullIfPersonNotExists() {
        PersonStatusDTO actual = personStatusHelper.getStatus(NOT_EXIST_PERSON);
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void shouldReturnMissingStatusIfPersonHasNotStatusAttribute() {
        PersonStatusDTO actual = personStatusHelper.getStatus(PERSON_WITHOUT_ATTRIBUTE);
        assertThat(actual, is(notNullValue()));
        assertThat(actual, hasProperty("personId", is(PERSON_WITHOUT_ATTRIBUTE)));
        assertThat(actual, hasProperty("title", is(PersonStatus.MISSING_VALUE.getTitleKey())));
        assertThat(actual, hasProperty("value", is(PersonStatus.MISSING_VALUE.name())));
        assertThat(actual, hasProperty("style", is(EXPECTED_MISSING_STYLE)));
        assertThat(actual, hasProperty("reason", is(nullValue())));
    }

    @Test
    public void shouldReturnMissingStatusIfPersonHasWrongValueOfAttribute() {
        PersonStatusDTO actual = personStatusHelper.getStatus(PERSON_WITH_WRONG_ATTRIBUTE_VALUE_UUID);
        assertThat(actual, is(notNullValue()));
        assertThat(actual, hasProperty("personId", is(PERSON_WITH_WRONG_ATTRIBUTE_VALUE_UUID)));
        assertThat(actual, hasProperty("title", is(PersonStatus.MISSING_VALUE.getTitleKey())));
        assertThat(actual, hasProperty("value", is(PersonStatus.MISSING_VALUE.name())));
        assertThat(actual, hasProperty("style", is(EXPECTED_MISSING_STYLE)));
        assertThat(actual, hasProperty("reason", is(nullValue())));
    }

    @Test
    public void shouldCreateNewPersonStatusAttribute() {
        PersonStatusDTO status = new PersonStatusDTO()
                .setPersonId(person.getUuid())
                .setValue(PersonStatus.DEACTIVATED.name())
                .setReason(Constant.STATUS_REASON_OTHER);
        personStatusHelper.saveStatus(status);

        PersonStatusDTO actual = personStatusHelper.getStatus(person.getUuid());
        assertThat(actual, is(notNullValue()));
        assertThat(actual, hasProperty("personId", is(person.getUuid())));
        assertThat(actual, hasProperty("title", is(PersonStatus.DEACTIVATED.getTitleKey())));
        assertThat(actual, hasProperty("value", is(PersonStatus.DEACTIVATED.name())));
        assertThat(actual, hasProperty("style", is(EXPECTED_DEACTIVATE_STYLE)));
        assertThat(actual, hasProperty("reason", is(Constant.STATUS_REASON_OTHER)));
    }

    @Test(expected = ValidationException.class)
    public void shouldThrowExceptionWhenNoValidStatusValue() {
        PersonStatusDTO status = new PersonStatusDTO()
                .setPersonId(person.getUuid())
                .setValue(NO_VALID_VALUE)
                .setReason(Constant.STATUS_REASON_OTHER);
        personStatusHelper.saveStatus(status);
    }

    @Test(expected = ValidationException.class)
    public void shouldThrowExceptionWhenMissingStatusValue() {
        PersonStatusDTO status = new PersonStatusDTO()
                .setPersonId(person.getUuid())
                .setValue(PersonStatus.MISSING_VALUE.name())
                .setReason(Constant.STATUS_REASON_OTHER);
        personStatusHelper.saveStatus(status);
    }

    @Test(expected = ValidationException.class)
    public void shouldThrowExceptionWhenInvalidStatusReason() {
        PersonStatusDTO status = new PersonStatusDTO()
                .setPersonId(person.getUuid())
                .setValue(PersonStatus.DEACTIVATED.name())
                .setReason(TEST_INVALID_REASON);
        personStatusHelper.saveStatus(status);
    }

    @Test
    public void shouldReturnTheDefaultSetOfReason() {
        List<String> actual = personStatusHelper.getPossibleReasons();
        assertThat(actual, is(notNullValue()));
        assertThat(actual.size(), is(EXPECTED_REASON_SIZE));
        assertThat(actual, is(equalTo(expectedReasons)));
    }
}
