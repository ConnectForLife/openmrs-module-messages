package org.openmrs.module.messages.handler;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.module.messages.ContextSensitiveTest;
import org.openmrs.module.messages.api.dao.ActorResponseDao;
import org.openmrs.module.messages.api.model.AdherenceFeedback;
import org.openmrs.module.messages.api.util.DateUtil;
import org.openmrs.module.messages.handler.impl.DailyBasedFeedbackCalculationHandlerImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.time.Clock;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DailyBasedFeedbackCalculationHandlerImplITTest extends ContextSensitiveTest {

  private static final ZonedDateTime TEST_NOW = ZonedDateTime.parse("2022-02-27T20:00:00+00:00");
  private static final String DATA_SET_PATH = "datasets/adherenceFeedbackCalculationTestData/";

  @Autowired private PatientService patientService;
  @Autowired private ActorResponseDao actorResponseDao;

  private Patient patient;

  @Before
  public void setup() throws Exception {
    executeDataSet(DATA_SET_PATH + "DailyBasedFeedbackCalculationHandlerImplITTest.xml");

    patient = patientService.getPatient(100);

    mockDateUtilClock();
  }

  @Test
  public void getAdherenceFeedback_shouldCalculateAdherence() {
    final DailyBasedFeedbackCalculationHandlerImpl handler =
        new DailyBasedFeedbackCalculationHandlerImpl(actorResponseDao);

    final AdherenceFeedback dailyFeedback = handler.getAdherenceFeedback(patient, patient);

    assertNotNull(dailyFeedback);
    assertEquals("Adherence report daily", dailyFeedback.getServiceName());
    assertEquals(67, dailyFeedback.getCurrentAdherence());
    assertEquals(100, dailyFeedback.getBenchmarkAdherence());
    assertEquals(6, dailyFeedback.getNumberOfDays());
    assertEquals(4, dailyFeedback.getNumberOfDaysWithPositiveAnswer());
    assertEquals("falling", dailyFeedback.getAdherenceTrend());
    assertEquals("low", dailyFeedback.getAdherenceLevel());
  }

  private void mockDateUtilClock() throws NoSuchFieldException, IllegalAccessException {
    Field clockField = DateUtil.class.getDeclaredField("clock");
    clockField.setAccessible(true);
    clockField.set(null, Clock.fixed(TEST_NOW.toInstant(), TEST_NOW.getZone()));
  }
}
