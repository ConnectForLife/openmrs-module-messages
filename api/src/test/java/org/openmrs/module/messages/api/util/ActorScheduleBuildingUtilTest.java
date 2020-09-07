package org.openmrs.module.messages.api.util;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.dto.ActorScheduleDTO;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.TemplateFieldType;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.builder.PatientTemplateBuilder;
import org.openmrs.module.messages.builder.TemplateFieldBuilder;
import org.openmrs.module.messages.builder.TemplateFieldValueBuilder;

import java.util.Arrays;

import static org.openmrs.module.messages.Constant.CAREGIVER_RELATIONSHIP;

public class ActorScheduleBuildingUtilTest {

    private static final String TEST_DATE_1 = "2019-12-07";
    private static final String TEST_DATE_2 = EndDateType.DATE_PICKER.getName() + "|2019-12-11";
    private static final String TEST_DATE_3 = EndDateType.AFTER_TIMES.getName() + "|8";
    private static final String TEST_CATEGORIES = "category 1,category 2,category 3";
    private static final String TEST_EVERY_WEEK_DAYS = "Monday,Tuesday,Wednesday,Thursday,Friday";
    private static final String TEST_ONE_WEEK_DAY = "Monday";

    private static final String TEST_OUTPUT_DATE_1 = "07 Dec 2019";
    private static final String TEST_OUTPUT_DATE_2 = "11 Dec 2019";
    private static final String TEST_OUTPUT_DATE_3 = "after 8 time(s)";
    private static final String TEST_OUTPUT_CATEGORIES = "category 1, category 2, category 3";
    private static final String SMS_TYPE = "SMS";
    private static final String DAILY_FREQUENCY = "Daily";
    private static final String MONTHLY_FREQUENCY = "Monthly";

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
        Assert.assertEquals(CAREGIVER_RELATIONSHIP, schedule.getActorType());
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
            MessagesConstants.DEACTIVATED_SERVICE);
        PatientTemplate patientTemplate = new PatientTemplateBuilder()
            .withTemplateFieldValues(Arrays.asList(tfv, tfv2))
            .build();

        ActorScheduleDTO schedule = ActorScheduleBuildingUtil.build(patientTemplate);

        Assert.assertEquals(MessagesConstants.DEACTIVATED_SCHEDULE_MESSAGE, schedule.getSchedule());
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

    @Test
    public void shouldBuildDailyFrequencyProperly() {

        TemplateFieldValue tfv1 = buildTemplateFieldWithValue(TemplateFieldType.START_OF_MESSAGES,
            TEST_DATE_1);
        TemplateFieldValue tfv2 = buildTemplateFieldWithValue(TemplateFieldType.END_OF_MESSAGES,
            TEST_DATE_2);
        TemplateFieldValue tfv3 = buildTemplateFieldWithValue(TemplateFieldType.SERVICE_TYPE,
            SMS_TYPE);
        TemplateFieldValue tfv4 = buildTemplateFieldWithValue(
            TemplateFieldType.MESSAGING_FREQUENCY_DAILY_OR_WEEKLY_OR_MONTHLY,
            DAILY_FREQUENCY);

        PatientTemplate patientTemplate = new PatientTemplateBuilder()
            .withTemplateFieldValues(Arrays.asList(tfv1, tfv2, tfv3, tfv4))
            .build();

        ActorScheduleDTO schedule = ActorScheduleBuildingUtil.build(patientTemplate);

        Assert.assertEquals(String.format("%s, Starts: %s, Ends: %s",
            DAILY_FREQUENCY, TEST_OUTPUT_DATE_1, TEST_OUTPUT_DATE_2),
            schedule.getSchedule());
    }

    @Test
    public void shouldBuildMonthlyFrequencyProperly() {

        TemplateFieldValue tfv1 = buildTemplateFieldWithValue(TemplateFieldType.START_OF_MESSAGES,
            TEST_DATE_1);
        TemplateFieldValue tfv2 = buildTemplateFieldWithValue(TemplateFieldType.END_OF_MESSAGES,
            TEST_DATE_2);
        TemplateFieldValue tfv3 = buildTemplateFieldWithValue(TemplateFieldType.SERVICE_TYPE,
            SMS_TYPE);
        TemplateFieldValue tfv4 = buildTemplateFieldWithValue(
            TemplateFieldType.MESSAGING_FREQUENCY_WEEKLY_OR_MONTHLY,
            MONTHLY_FREQUENCY);

        PatientTemplate patientTemplate = new PatientTemplateBuilder()
            .withTemplateFieldValues(Arrays.asList(tfv1, tfv2, tfv3, tfv4))
            .build();

        ActorScheduleDTO schedule = ActorScheduleBuildingUtil.build(patientTemplate);

        Assert.assertEquals(String.format("%s, Starts: %s, Ends: %s",
            MONTHLY_FREQUENCY, TEST_OUTPUT_DATE_1, TEST_OUTPUT_DATE_2),
            schedule.getSchedule());
    }

    @Test
    public void shouldBuildFrequencyProperlyWithDayOfWeekDefined() {

        TemplateFieldValue tfv1 = buildTemplateFieldWithValue(TemplateFieldType.START_OF_MESSAGES,
            TEST_DATE_1);
        TemplateFieldValue tfv2 = buildTemplateFieldWithValue(TemplateFieldType.END_OF_MESSAGES,
            TEST_DATE_2);
        TemplateFieldValue tfv3 = buildTemplateFieldWithValue(TemplateFieldType.SERVICE_TYPE,
            SMS_TYPE);
        TemplateFieldValue tfv4 = buildTemplateFieldWithValue(TemplateFieldType.DAY_OF_WEEK,
            TEST_EVERY_WEEK_DAYS);

        PatientTemplate patientTemplate = new PatientTemplateBuilder()
            .withTemplateFieldValues(Arrays.asList(tfv1, tfv2, tfv3, tfv4))
            .build();

        ActorScheduleDTO schedule = ActorScheduleBuildingUtil.build(patientTemplate);

        Assert.assertEquals(String.format("Every weekday, Starts: %s, Ends: %s",
            TEST_OUTPUT_DATE_1, TEST_OUTPUT_DATE_2),
            schedule.getSchedule());
    }

    @Test
    public void shouldBuildFrequencyProperlyWithDayOfWeekSingleDefined() {

        TemplateFieldValue tfv1 = buildTemplateFieldWithValue(TemplateFieldType.START_OF_MESSAGES,
            TEST_DATE_1);
        TemplateFieldValue tfv2 = buildTemplateFieldWithValue(TemplateFieldType.END_OF_MESSAGES,
            TEST_DATE_2);
        TemplateFieldValue tfv3 = buildTemplateFieldWithValue(TemplateFieldType.SERVICE_TYPE,
            SMS_TYPE);
        TemplateFieldValue tfv4 = buildTemplateFieldWithValue(TemplateFieldType.DAY_OF_WEEK_SINGLE,
            TEST_ONE_WEEK_DAY);

        PatientTemplate patientTemplate = new PatientTemplateBuilder()
            .withTemplateFieldValues(Arrays.asList(tfv1, tfv2, tfv3, tfv4))
            .build();

        ActorScheduleDTO schedule = ActorScheduleBuildingUtil.build(patientTemplate);

        Assert.assertEquals(String.format("%s, Starts: %s, Ends: %s",
            TEST_ONE_WEEK_DAY, TEST_OUTPUT_DATE_1, TEST_OUTPUT_DATE_2),
            schedule.getSchedule());
    }

    @Test
    public void shouldBuildCategoriesProperly() {

        TemplateFieldValue tfv1 = buildTemplateFieldWithValue(TemplateFieldType.CATEGORY_OF_MESSAGE,
            TEST_CATEGORIES);
        TemplateFieldValue tfv2 = buildTemplateFieldWithValue(TemplateFieldType.SERVICE_TYPE,
            SMS_TYPE);

        PatientTemplate patientTemplate = new PatientTemplateBuilder()
            .withTemplateFieldValues(Arrays.asList(tfv1, tfv2))
            .build();

        ActorScheduleDTO schedule = ActorScheduleBuildingUtil.build(patientTemplate);

        Assert.assertEquals(String.format("Categories: %s", TEST_OUTPUT_CATEGORIES), schedule.getSchedule());
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
