package org.openmrs.module.messages.api.util;

import static org.openmrs.module.messages.api.util.ConfigConstants.DEACTIVATED_SCHEDULE_MESSAGE;

import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.openmrs.module.messages.api.dto.ActorScheduleDTO;
import org.openmrs.module.messages.api.model.ChannelType;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.TemplateFieldType;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.builder.PatientTemplateBuilder;
import org.openmrs.module.messages.builder.TemplateFieldBuilder;
import org.openmrs.module.messages.builder.TemplateFieldValueBuilder;

public class ActorScheduleBuildingUtilTest {

    private static final String TEST_DATE_1 = "2019-12-07";
    private static final String TEST_DATE_2 = "2019-12-11";
    private static final String TEST_OUTPUT_DATE_1 = "07 Dec 2019";
    private static final String TEST_OUTPUT_DATE_2 = "11 Dec 2019";
    public static final String SMS_TYPE = "SMS";

    @Test
    public void shouldBuildActorScheduleWithNotNullScheduleString() {
        PatientTemplate patientTemplate = new PatientTemplateBuilder().build();

        ActorScheduleDTO schedule = ActorScheduleBuildingUtil.build(patientTemplate);

        Assert.assertNotNull(schedule);
        Assert.assertNotNull(schedule.getSchedule());
    }

    @Test
    public void shouldBuildActorScheduleWithActorTypeAndId() {
        int personId = 0;
        PatientTemplate patientTemplate = new PatientTemplateBuilder().build();
        patientTemplate.getActorType().getPersonB().setId(patientTemplate.getPatient().getId());
        patientTemplate.getActorType().getPersonA().setId(personId);

        ActorScheduleDTO schedule = ActorScheduleBuildingUtil.build(patientTemplate);

        Assert.assertEquals(new Integer(personId), schedule.getActorId());
        Assert.assertEquals("Caregiver", schedule.getActorType());
    }

    @Test
    public void shouldBuildStartDateSchedule() {
        TemplateFieldValue tfv = buildTemplateFieldWithValue(TemplateFieldType.START_OF_MESSAGES,
            TEST_DATE_1);
        TemplateFieldValue tfv2 = buildTemplateFieldWithValue(TemplateFieldType.SERVICE_TYPE,
            SMS_TYPE);
        PatientTemplate patientTemplate = new PatientTemplateBuilder()
            .withTemplateFieldValues(Arrays.asList(tfv, tfv2))
            .build();

        ActorScheduleDTO schedule = ActorScheduleBuildingUtil.build(patientTemplate);

        Assert.assertEquals(String.format("Starts: %s", TEST_OUTPUT_DATE_1), schedule.getSchedule());
    }

    @Test
    public void shouldBuildDeactivated() {
        TemplateFieldValue tfv = buildTemplateFieldWithValue(TemplateFieldType.START_OF_MESSAGES,
            TEST_DATE_1);
        TemplateFieldValue tfv2 = buildTemplateFieldWithValue(TemplateFieldType.SERVICE_TYPE,
            ChannelType.DEACTIVATED.getName());
        PatientTemplate patientTemplate = new PatientTemplateBuilder()
            .withTemplateFieldValues(Arrays.asList(tfv, tfv2))
            .build();

        ActorScheduleDTO schedule = ActorScheduleBuildingUtil.build(patientTemplate);

        Assert.assertEquals(DEACTIVATED_SCHEDULE_MESSAGE, schedule.getSchedule());
    }

    @Test
    public void shouldBuildEndDateSchedule() {
        TemplateFieldValue tfv = buildTemplateFieldWithValue(TemplateFieldType.END_OF_MESSAGES,
            TEST_DATE_2);
        TemplateFieldValue tfv2 = buildTemplateFieldWithValue(TemplateFieldType.SERVICE_TYPE,
            SMS_TYPE);
        PatientTemplate patientTemplate = new PatientTemplateBuilder()
            .withTemplateFieldValues(Arrays.asList(tfv, tfv2))
            .build();

        ActorScheduleDTO schedule = ActorScheduleBuildingUtil.build(patientTemplate);

        Assert.assertEquals(String.format("Ends: %s", TEST_OUTPUT_DATE_2), schedule.getSchedule());
    }

    @Test
    public void shouldBuildBothDatesSchedule() {
        TemplateFieldValue tfv1 = buildTemplateFieldWithValue(TemplateFieldType.START_OF_MESSAGES,
            TEST_DATE_1);
        TemplateFieldValue tfv2 = buildTemplateFieldWithValue(TemplateFieldType.END_OF_MESSAGES,
            TEST_DATE_2);
        TemplateFieldValue tfv3 = buildTemplateFieldWithValue(TemplateFieldType.SERVICE_TYPE,
            SMS_TYPE);
        PatientTemplate patientTemplate = new PatientTemplateBuilder()
            .withTemplateFieldValues(Arrays.asList(tfv2, tfv1, tfv3))
            .build();

        ActorScheduleDTO schedule = ActorScheduleBuildingUtil.build(patientTemplate);

        Assert.assertEquals(String.format("Starts: %s, Ends: %s",
            TEST_OUTPUT_DATE_1, TEST_OUTPUT_DATE_2),
            schedule.getSchedule());
    }

    private TemplateFieldValue buildTemplateFieldWithValue(TemplateFieldType type, String value) {
        return new TemplateFieldValueBuilder()
            .withTemplateField(
                new TemplateFieldBuilder()
                    .withTemplateFieldType(type)
                    .build())
            .withValue(value)
            .build();
    }
}
