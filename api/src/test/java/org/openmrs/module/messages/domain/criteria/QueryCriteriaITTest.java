/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.domain.criteria;

import org.codehaus.jackson.map.util.ISO8601DateFormat;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.db.PersonDAO;
import org.openmrs.module.messages.ContextSensitiveTest;
import org.openmrs.module.messages.api.dao.PatientAdvancedDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class QueryCriteriaITTest extends ContextSensitiveTest {

    private static final int DATASET_TOTAL_PATIENT_COUNT = 3;
    private static final int DATASET_TOTAL_MALE_PATIENT_COUNT = 1;
    private static final int DATASET_TOTAL_FEMALE_PATIENT_COUNT = 1;
    private static final int DATASET_TOTAL_OTHER_PATIENT_COUNT = 1;
    private static final int DATASET_MALE_PATIENT_ID = 1001;
    private static final int DATASET_FEMALE_PATIENT_ID = 1002;
    private static final int DATASET_OTHER_PATIENT_ID = 1003;
    private static final String DATASET_OTHER_PATIENT_ATTR1_VALUE = "Third";
    private static final String DATASET_COUNTRY_A_VALUE = "CountryA";
    private static final int DATASET_TOTAL_COUNTRY_A_PATIENT_COUNT = 2;

    @Autowired
    private PersonDAO personDAO;

    @Autowired
    private PatientAdvancedDao patientAdvancedDao;

    private PersonAttributeType myAttribute1Type;

    public QueryCriteriaITTest() throws Exception {
        // Skip because it imports some Patients
        skipBaseSetup();
    }

    @Before
    public void beforeTest() throws Exception {
        executeDataSet("org/openmrs/include/initialInMemoryTestDataSet.xml");
        executeDataSet("datasets/QueryCriteriaITTest.xml");

        myAttribute1Type = personDAO.getPersonAttributeTypeByUuid("0b114f1ca-1000-0000-0000-000000002001");
    }

    @Test
    public void emptyCriteria_shouldReturnAllPatients() {
        // Given

        // When
        final List<Patient> patients = patientAdvancedDao.getPatients(0, -1, null);

        // Then
        assertNotNull(patients);
        assertThat(patients.size(), is(DATASET_TOTAL_PATIENT_COUNT));
    }

    @Test
    public void criteriaOnGender_shouldReturnCorrectPatients() {
        // Given
        final EntityFieldCondition genderCondition = new EntityFieldCondition("gender", "=", "F");
        final QueryCriteria queryCriteria = QueryCriteria.fromConditions(Patient.class, genderCondition);

        // When
        final List<Patient> patients = patientAdvancedDao.getPatients(0, -1, queryCriteria);

        // Then
        assertNotNull(patients);
        assertThat(patients.size(), is(DATASET_TOTAL_FEMALE_PATIENT_COUNT));
        assertThat(patients.get(0).getId(), is(DATASET_FEMALE_PATIENT_ID));
    }

    @Test
    public void criteriaOnBirthdate_shouldReturnCorrectPatients() throws ParseException {
        // Given
        final Date secondOfMay = new ISO8601DateFormat().parse("2000-05-01T12:00:00Z");
        final EntityFieldCondition genderCondition = new EntityFieldCondition("birthdate", "<", secondOfMay);
        final QueryCriteria queryCriteria = QueryCriteria.fromConditions(Patient.class, genderCondition);

        // When
        final List<Patient> patients = patientAdvancedDao.getPatients(0, -1, queryCriteria);

        // Then
        assertNotNull(patients);
        assertThat(patients.size(), is(DATASET_TOTAL_MALE_PATIENT_COUNT));
        assertThat(patients.get(0).getId(), is(DATASET_MALE_PATIENT_ID));
    }

    @Test
    public void criteriaOnStringAttribute_shouldReturnCorrectPatients() {
        // Given
        final PersonAttributeCondition personAttributeCondition =
                new PersonAttributeCondition(myAttribute1Type, "=", DATASET_OTHER_PATIENT_ATTR1_VALUE);
        final QueryCriteria queryCriteria = QueryCriteria.fromConditions(Patient.class, personAttributeCondition);

        // When
        final List<Patient> patients = patientAdvancedDao.getPatients(0, -1, queryCriteria);

        // Then
        assertNotNull(patients);
        assertThat(patients.size(), is(DATASET_TOTAL_OTHER_PATIENT_COUNT));
        assertThat(patients.get(0).getId(), is(DATASET_OTHER_PATIENT_ID));
    }

    @Test
    public void complexQueryCriteria_shouldReturnCorrectPatients() {
        // Given
        final PersonAttributeCondition personAttributeCondition =
                new PersonAttributeCondition(myAttribute1Type, "=", DATASET_OTHER_PATIENT_ATTR1_VALUE);
        final EntityFieldCondition genderCondition = new EntityFieldCondition("gender", "<>", "M");

        final QueryCriteria queryCriteria =
                QueryCriteria.fromConditions(Patient.class, personAttributeCondition, genderCondition);

        // When
        final List<Patient> patients = patientAdvancedDao.getPatients(0, -1, queryCriteria);

        // Then
        assertNotNull(patients);
        assertThat(patients.size(), is(DATASET_TOTAL_OTHER_PATIENT_COUNT));
        assertThat(patients.get(0).getId(), is(DATASET_OTHER_PATIENT_ID));
    }

    @Test
    public void complexQueryCriteriaWithExcludingConditions_shouldReturnCorrectPatients() {
        // Given
        final PersonAttributeCondition personAttributeCondition =
                new PersonAttributeCondition(myAttribute1Type, "=", DATASET_OTHER_PATIENT_ATTR1_VALUE);
        final EntityFieldCondition genderCondition = new EntityFieldCondition("gender", "=", "M");

        final QueryCriteria queryCriteria =
                QueryCriteria.fromConditions(Patient.class, personAttributeCondition, genderCondition);

        // When
        final List<Patient> patients = patientAdvancedDao.getPatients(0, -1, queryCriteria);

        // Then
        assertNotNull(patients);
        assertThat(patients.size(), is(0));
    }

    @Test
    public void complexQueryCriteriaWithInCollectionCondition_shouldReturnCorrectPatients() {
        // Given
        final EntityFieldCondition genderInCondition = new EntityFieldCondition("gender", "in", Arrays.asList("M", "F"));

        final QueryCriteria queryCriteria = QueryCriteria.fromConditions(Patient.class, genderInCondition);

        // When
        final List<Patient> patients = patientAdvancedDao.getPatients(0, -1, queryCriteria);

        // Then
        assertNotNull(patients);
        assertThat(patients.size(), is(2));
        assertThat(patients, contains(hasProperty("id", is(1001)), hasProperty("id", is(1002))));
    }

    @Test
    public void criteriaOnPersonAddress_shouldReturnCorrectPatients() {
        // Given
        final PersonAddressCondition personAttributeCondition =
                new PersonAddressCondition("country", "=", DATASET_COUNTRY_A_VALUE);
        final QueryCriteria queryCriteria = QueryCriteria.fromConditions(Patient.class, personAttributeCondition);

        // When
        final List<Patient> patients = patientAdvancedDao.getPatients(0, -1, queryCriteria);

        // Then
        assertNotNull(patients);
        assertThat(patients.size(), is(DATASET_TOTAL_COUNTRY_A_PATIENT_COUNT));
        assertThat(patients,
                contains(hasProperty("id", is(DATASET_MALE_PATIENT_ID)), hasProperty("id", is(DATASET_OTHER_PATIENT_ID))));
    }
}
