package org.openmrs.module.messages.api.util;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.ContextSensitiveWithActivatorTest;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.messages.api.dto.PersonStatusDTO;
import org.openmrs.module.messages.api.model.PersonStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class PersonStatusHelperITTest extends ContextSensitiveWithActivatorTest {

    private static final String XML_DATASET_PATH = "datasets/";

    private static final String XML_PERSON_ATTRIBUTE_DATASET = XML_DATASET_PATH + "PersonAttributeDataset.xml";

    private static final String PERSON_UUID = "d64b7f82-5857-48f5-8694-b1bdb46688c3";

    private static final String VOIDED_CONTACT_TIME = "48212932834";

    private static final String ACTUAL_CONTACT_TIME = "48421029382";

    private static final String VOIDED_STATUS = PersonStatus.NO_CONSENT.name();

    private static final String ACTUAL_STATUS = PersonStatus.ACTIVE.name();

    private static final String EXPECTED_ACTIVE_STYLE =
            "style=\"background-color: #51a351; border-color: #51a351; color: #f5f5f5; \"";

    private static final String EXPECTED_MISSING_STYLE =
            "style=\"background-color: #EEA616; border-color: #EEA616; color: #f5f5f5; \"";

    private static final String EXPECTED_DEACTIVATE_STYLE =
            "style=\"background-color: #f23722; border-color: #f23722; color: #f5f5f5; \"";

    private static final String NOT_EXIST_PERSON = "8124263d-d1ec-4be5-bf62-502ab125d076";

    private static final String PERSON_WITHOUT_ATTRIBUTE = "8c8169be-94f0-42ba-81de-cf3f2c12a7ec";

    public static final String TEST_REASON = "The status was changed for test reason";

    private Person person;

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
        PersonAttributeType contactTimeAttributeType = personService.getPersonAttributeTypeByUuid(
                ConfigConstants.PERSON_CONTACT_TIME_TYPE_UUID);
        PersonAttributeType personStatusAttributeType = personService.getPersonAttributeTypeByUuid(
                ConfigConstants.PATIENT_STATUS_ATTRIBUTE_TYPE_UUID);

        PersonAttribute voidedContactTime = new PersonAttribute(contactTimeAttributeType, VOIDED_CONTACT_TIME);
        person.addAttribute(voidedContactTime);
        PersonAttribute voidedStatus = new PersonAttribute(personStatusAttributeType, VOIDED_STATUS);
        person.addAttribute(voidedStatus);
        person = personService.savePerson(person);
        Context.flushSession();
        getConnection().commit();
        PersonAttribute actualContactTime = new PersonAttribute(contactTimeAttributeType, ACTUAL_CONTACT_TIME);
        person.addAttribute(actualContactTime);
        PersonAttribute actualStatus = new PersonAttribute(personStatusAttributeType, ACTUAL_STATUS);
        person.addAttribute(actualStatus);
        person = personService.savePerson(person);
    }

    @Test
    public void shouldReturnExpectedStatusByPersonId() {
        PersonStatusDTO actual = personStatusHelper.getStatus(person.getId().toString());
        assertThat(actual, is(notNullValue()));
        assertThat(actual, hasProperty("personId", is(person.getId().toString())));
        assertThat(actual, hasProperty("title", is(PersonStatus.ACTIVE.getTitleKey())));
        assertThat(actual, hasProperty("value", is(PersonStatus.ACTIVE.name())));
        assertThat(actual, hasProperty("style", is(EXPECTED_ACTIVE_STYLE)));
        assertThat(actual, hasProperty("reason", is(nullValue())));
    }

    @Test
    public void shouldReturnExpectedStatusByPersonUuid() {
        PersonStatusDTO actual = personStatusHelper.getStatus(person.getUuid());
        assertThat(actual, is(notNullValue()));
        assertThat(actual, hasProperty("personId", is(person.getUuid())));
        assertThat(actual, hasProperty("title", is(PersonStatus.ACTIVE.getTitleKey())));
        assertThat(actual, hasProperty("value", is(PersonStatus.ACTIVE.name())));
        assertThat(actual, hasProperty("style", is(EXPECTED_ACTIVE_STYLE)));
        assertThat(actual, hasProperty("reason", is(nullValue())));
    }

    @Test
    public void shouldReturnNullIfPersonNotExists() {
        PersonStatusDTO actual = personStatusHelper.getStatus(NOT_EXIST_PERSON);
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void shouldReturnMissingStatusIfPersonHaveNotStatusAttribute() {
        PersonStatusDTO actual = personStatusHelper.getStatus(PERSON_WITHOUT_ATTRIBUTE);
        assertThat(actual, is(notNullValue()));
        assertThat(actual, hasProperty("personId", is(PERSON_WITHOUT_ATTRIBUTE)));
        assertThat(actual, hasProperty("title", is(PersonStatus.MISSING_VALUE.getTitleKey())));
        assertThat(actual, hasProperty("value", is(PersonStatus.MISSING_VALUE.name())));
        assertThat(actual, hasProperty("style", is(EXPECTED_MISSING_STYLE)));
        assertThat(actual, hasProperty("reason", is(nullValue())));
    }

    @Test
    public void shouldCreateNewPersonStatusAttribute() {
        PersonStatusDTO status = new PersonStatusDTO()
                .setPersonId(person.getUuid())
                .setValue(PersonStatus.DEACTIVATE.name())
                .setReason(TEST_REASON);
        personStatusHelper.saveStatus(status);

        PersonStatusDTO actual = personStatusHelper.getStatus(person.getUuid());
        assertThat(actual, is(notNullValue()));
        assertThat(actual, hasProperty("personId", is(person.getUuid())));
        assertThat(actual, hasProperty("title", is(PersonStatus.DEACTIVATE.getTitleKey())));
        assertThat(actual, hasProperty("value", is(PersonStatus.DEACTIVATE.name())));
        assertThat(actual, hasProperty("style", is(EXPECTED_DEACTIVATE_STYLE)));
        assertThat(actual, hasProperty("reason", is(nullValue())));
    }

}
