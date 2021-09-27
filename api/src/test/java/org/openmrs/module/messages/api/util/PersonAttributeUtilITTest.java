package org.openmrs.module.messages.api.util;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.PersonService;
import org.openmrs.module.messages.ContextSensitiveWithActivatorTest;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.messages.api.model.PersonStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class PersonAttributeUtilITTest extends ContextSensitiveWithActivatorTest {

    private static final String XML_DATASET_PATH = "datasets/";

    private static final String XML_PERSON_ATTRIBUTE_DATASET = XML_DATASET_PATH + "PersonAttributeDataset.xml";

    private static final String PERSON_UUID = "d64b7f82-5857-48f5-8694-b1bdb46688c3";

    private static final String PERSON_WITH_WRONG_ATTRIBUTE_VALUE_UUID = "69962c4a-ad29-4e9b-951b-2a27079f2b25";

    private static final String VOIDED_CONTACT_TIME = "48212932834";

    private static final String ACTUAL_CONTACT_TIME = "48421029382";

    private static final String VOIDED_STATUS = PersonStatus.NO_CONSENT.name();

    private static final String ACTUAL_STATUS = PersonStatus.ACTIVATED.name();

    private static final String WRONG_STATUS = "No consent";

    private Person person;

    private Person personWithWrongAttributeValue;

    @Autowired
    @Qualifier("personService")
    private PersonService personService;

    @Before
    public void setUp() throws Exception {
        executeDataSet(XML_PERSON_ATTRIBUTE_DATASET);
        person = personService.getPersonByUuid(PERSON_UUID);
        PersonAttributeType contactTimeAttributeType = personService.getPersonAttributeTypeByUuid(
                ConfigConstants.PERSON_CONTACT_TIME_TYPE_UUID);
        PersonAttributeType personStatusAttributeType = personService.getPersonAttributeTypeByUuid(
                ConfigConstants.PERSON_STATUS_ATTRIBUTE_TYPE_UUID);

        PersonAttribute voidedContactTime = new PersonAttribute(contactTimeAttributeType, VOIDED_CONTACT_TIME);
        person.addAttribute(voidedContactTime);
        PersonAttribute voidedStatus = new PersonAttribute(personStatusAttributeType, VOIDED_STATUS);
        person.addAttribute(voidedStatus);
        PersonAttribute actualContactTime = new PersonAttribute(contactTimeAttributeType, ACTUAL_CONTACT_TIME);
        person.addAttribute(actualContactTime);
        PersonAttribute actualStatus = new PersonAttribute(personStatusAttributeType, ACTUAL_STATUS);
        person.addAttribute(actualStatus);
        person = personService.savePerson(person);

        personWithWrongAttributeValue = personService.getPersonByUuid(PERSON_WITH_WRONG_ATTRIBUTE_VALUE_UUID);
        PersonAttribute wrongStatus = new PersonAttribute(personStatusAttributeType, WRONG_STATUS);
        personWithWrongAttributeValue.addAttribute(wrongStatus);
        personWithWrongAttributeValue = personService.savePerson(personWithWrongAttributeValue);
    }

    @Test
    public void shouldGetActualPersonContactTimeAttribute() {
        PersonAttribute actual = PersonAttributeUtil.getBestContactTimeAttribute(person);
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getValue(), is(ACTUAL_CONTACT_TIME));
    }

    @Test
    public void shouldGetActualPersonStatus() {
        PersonStatus actual = PersonAttributeUtil.getPersonStatus(person);
        assertThat(actual, is(notNullValue()));
        assertThat(actual.name(), is(ACTUAL_STATUS));
    }

    @Test
    public void shouldGetMissingStatusIfPersonHasWrongValueOfAttribute() {
        PersonStatus actual = PersonAttributeUtil.getPersonStatus(personWithWrongAttributeValue);
        assertThat(actual, is(notNullValue()));
        assertThat(actual, is(PersonStatus.MISSING_VALUE));
    }

    @Test
    public void shouldGetActualPersonStatusAttribute() {
        PersonAttribute actual = PersonAttributeUtil.getPersonStatusAttribute(person);
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getValue(), is(ACTUAL_STATUS));
    }
}
