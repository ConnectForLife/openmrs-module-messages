package org.openmrs.module.messages;

import org.openmrs.module.messages.api.model.Range;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.PersonName;
import org.openmrs.api.LocationService;
import org.openmrs.api.PatientService;
import org.openmrs.module.messages.api.execution.ExecutionException;
import org.openmrs.module.messages.api.execution.ServiceExecutor;
import org.openmrs.module.messages.api.execution.ServiceResult;
import org.openmrs.module.messages.api.execution.ServiceResultList;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.model.TemplateFieldType;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.openmrs.module.messages.api.service.TemplateService;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class ExecutionEngineContextTest extends BaseModuleContextSensitiveTest {

    private static final Date START_DATE = DateUtils.addYears(new Date(), -2);
    private static final Date END_DATE = DateUtils.addYears(new Date(), 2);
    // drop mili precision for H2 testing purposes
    private static final Date BIRTH_DATE = DateUtils.setMilliseconds(DateUtils.addYears(new Date(), -1), 0);

    @Autowired
    private ServiceExecutor serviceExecutor;

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientTemplateService patientTemplateService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private LocationService locationService;

    @Test
    public void shouldSetupContext() throws ExecutionException {
        PatientTemplate patientTemplate = prepareData();
        Range<Date> dateRange = new Range<>(START_DATE, END_DATE);

        ServiceResultList serviceResultList = serviceExecutor.execute(patientTemplate, dateRange);

        assertEquals(patientTemplate.getPatient().getId(), serviceResultList.getPatientId());
        assertEquals(patientTemplate.getPatient().getId(), serviceResultList.getActorId());
        assertEquals(START_DATE, serviceResultList.getStartDate());
        assertEquals(END_DATE, serviceResultList.getEndDate());
        assertEquals(1, serviceResultList.getResults().size());

        ServiceResult result = serviceResultList.getResults().get(0);
        assertEquals("msg", result.getMessageId());
        assertEquals(1, result.getChannelId().intValue());
        // query adds 1 year to the birth date
        assertEquals(DateUtils.addYears(BIRTH_DATE, 1).getTime(), result.getExecutionDate().getTime());
    }

    private PatientTemplate prepareData() {
        PatientIdentifierType idType = new PatientIdentifierType();
        idType.setName("Test ID");
        idType = patientService.savePatientIdentifierType(idType);

        Patient patient = new Patient();
        patient.addName(new PersonName("Test", "John", "Boe"));
        patient.setGender("M");
        patient.setBirthdate(BIRTH_DATE);

        PatientIdentifier id = new PatientIdentifier("XXX", idType, locationService.getDefaultLocation());
        patient.addIdentifier(id);

        patient = patientService.savePatient(patient);

        Template template = new Template();
        template.setServiceQuery("SELECT 0;");
        template.setServiceQueryType("SQL");
        template.setName("Service name");
        template = templateService.saveOrUpdate(template);

        PatientTemplate patientTemplate = new PatientTemplate();
        patientTemplate.setPatient(patient);
        patientTemplate.setActor(patient);
        patientTemplate.setServiceQueryType("SQL");
        patientTemplate.setServiceQuery("SELECT DATEADD('YEAR', 1, per.birthdate), 'msg', 1 FROM patient p " +
                "JOIN person per ON per.person_id = p.patient_id  " +
                "WHERE per.birthdate > :startDate AND per.birthdate < :endDate");

        // TODO incorrect schema
        TemplateField templateField = new TemplateField();
        templateField.setTemplate(template);
        templateField.setMandatory(false);
        templateField.setName("This relation is wrong");
        templateField.setTemplateFieldType(TemplateFieldType.SERVICE_TYPE);

        TemplateFieldValue templateFieldValue = new TemplateFieldValue();
        templateFieldValue.setTemplateField(templateField);
        patientTemplate.setTemplate(template);

        return patientTemplateService.saveOrUpdate(patientTemplate);
    }
}
