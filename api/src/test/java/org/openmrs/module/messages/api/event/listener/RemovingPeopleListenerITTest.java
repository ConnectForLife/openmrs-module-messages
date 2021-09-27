package org.openmrs.module.messages.api.event.listener;

import org.apache.activemq.command.ActiveMQMapMessage;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.PatientService;
import org.openmrs.module.messages.Constant;
import org.openmrs.module.messages.ContextSensitiveWithActivatorTest;
import org.openmrs.module.messages.api.event.listener.subscribable.RemovingPeopleListener;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.service.DatasetConstants;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.openmrs.module.messages.domain.criteria.PatientTemplateCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.List;

public class RemovingPeopleListenerITTest extends ContextSensitiveWithActivatorTest {

    private static final String DATASET_NAME = "MessageDataSet.xml";
    private static final int INITIAL_TEMPLATE_COUNT_FOR_PATIENT_AS_ACTOR = 2;
    private static final int INITIAL_TEMPLATE_COUNT_FOR_PATIENT_AS_PATIENT = 1;
    private static final String GENERAL_VOIDING_REASON = "General voiding reason";
    private static final int EXPECTED_TEMPLATE_COUNT = 0;

    private Patient patient;

    @Autowired
    @Qualifier("messages.patientTemplateService")
    private PatientTemplateService patientTemplateService;

    @Autowired
    @Qualifier("patientService")
    private PatientService patientService;

    @Autowired
    @Qualifier("messages.removingPeopleListener")
    private RemovingPeopleListener listener;

    @Before
    public void setUp() throws Exception {
        executeDataSet(DatasetConstants.XML_DATA_SET_PATH + DATASET_NAME);
        patient = patientService.getPatient(DatasetConstants.DEFAULT_PERSON_ID);
    }

    @Test
    public void shouldVoidPatientTemplateAfterVoidingPatientWhichIsActor() throws JMSException {
        List<PatientTemplate> templateBefore = patientTemplateService.findAllByCriteria(
                PatientTemplateCriteria.forActorId(DatasetConstants.DEFAULT_PERSON_ID));
        patientService.voidPatient(patient, GENERAL_VOIDING_REASON);
        mockFireEvent(buildMessage(patient));
        List<PatientTemplate> templateAfter = patientTemplateService.findAllByCriteria(
                PatientTemplateCriteria.forActorId(DatasetConstants.DEFAULT_PERSON_ID));
        Assert.assertThat(templateBefore.size(), CoreMatchers.is(INITIAL_TEMPLATE_COUNT_FOR_PATIENT_AS_ACTOR));
        Assert.assertThat(templateAfter.size(), CoreMatchers.is(EXPECTED_TEMPLATE_COUNT));
    }

    @Test
    public void shouldVoidPatientTemplateAfterVoidingPatientWhichIsPatient() throws JMSException {
        List<PatientTemplate> templateBefore = patientTemplateService.findAllByCriteria(
                PatientTemplateCriteria.forPatientId(DatasetConstants.DEFAULT_PERSON_ID));
        patientService.voidPatient(patient, GENERAL_VOIDING_REASON);
        mockFireEvent(buildMessage(patient));
        List<PatientTemplate> templateAfter = patientTemplateService.findAllByCriteria(
                PatientTemplateCriteria.forPatientId(DatasetConstants.DEFAULT_PERSON_ID));
        Assert.assertThat(templateBefore.size(), CoreMatchers.is(INITIAL_TEMPLATE_COUNT_FOR_PATIENT_AS_PATIENT));
        Assert.assertThat(templateAfter.size(), CoreMatchers.is(EXPECTED_TEMPLATE_COUNT));
    }

    private void mockFireEvent(Message message) {
        listener.performAction(message);
    }

    private Message buildMessage(Person person) throws JMSException {
        ActiveMQMapMessage message = new ActiveMQMapMessage();
        message.setString(Constant.UUID_KEY, person.getUuid());
        return message;
    }
}
