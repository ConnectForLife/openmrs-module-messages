package org.openmrs.module.messages.api.util;

import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.openmrs.module.messages.api.dto.ActorScheduleDTO;
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

    @Test
    public void shouldBuildActorScheduleWithNotNullScheduleString() {
        PatientTemplate patientTemplate = new PatientTemplateBuilder().build();

        ActorScheduleDTO schedule = ActorScheduleBuildingUtil.build(patientTemplate);

        Assert.assertNotNull(schedule);
        Assert.assertNotNull(schedule.getSchedule());
    }

    @Test
    public void shouldBuildStartDateSchedule() {
        TemplateFieldValue tfv = buildTemplateFieldWithValue(TemplateFieldType.START_OF_MESSAGES,
            TEST_DATE_1);
        PatientTemplate patientTemplate = new PatientTemplateBuilder()
            .withTemplateFieldValues(Arrays.asList(tfv))
            .build();

        ActorScheduleDTO schedule = ActorScheduleBuildingUtil.build(patientTemplate);

        Assert.assertEquals(String.format("Starts: %s", TEST_OUTPUT_DATE_1), schedule.getSchedule());
    }

    @Test
    public void shouldBuildEndDateSchedule() {
        TemplateFieldValue tfv = buildTemplateFieldWithValue(TemplateFieldType.END_OF_MESSAGES,
            TEST_DATE_2);
        PatientTemplate patientTemplate = new PatientTemplateBuilder()
            .withTemplateFieldValues(Arrays.asList(tfv))
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
        PatientTemplate patientTemplate = new PatientTemplateBuilder()
            .withTemplateFieldValues(Arrays.asList(tfv2, tfv1))
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
