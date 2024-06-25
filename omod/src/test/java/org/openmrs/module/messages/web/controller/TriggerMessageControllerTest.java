package org.openmrs.module.messages.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.VisitAttributeType;
import org.openmrs.VisitType;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.LocationService;
import org.openmrs.api.PatientService;
import org.openmrs.api.VisitService;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.ScheduledExecutionContext;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.service.MessagesDeliveryService;
import org.openmrs.module.messages.api.service.MessagingGroupService;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class TriggerMessageControllerTest {

    private static final String PATIENT_UUID = "6a6c7a01-d16e-11ec-9a21-0242ac140002";

    private Patient patient;

    @InjectMocks
    private TriggerMessageController controller = new TriggerMessageController();

    @Mock
    private PatientService patientService;

    @Mock
    private MessagingGroupService messagingGroupService;

    @Mock
    private PatientTemplateService patientTemplateService;

    @Mock
    private MessagesDeliveryService messagesDeliveryService;

    @Mock
    private VisitService visitService;

    @Mock
    private LocationService locationService;

    @Mock
    private AdministrationService administrationService;

    @Before
    public void setUp() {
        mockStatic(Context.class);
        when(Context.getVisitService()).thenReturn(visitService);
        when(Context.getLocationService()).thenReturn(locationService);
        when(Context.getAdministrationService()).thenReturn(administrationService);
    }

    @Test
    public void shouldTriggerSendingMessage() {
        patient = buildTestPatient();
        when(patientService.getPatientByUuid(PATIENT_UUID)).thenReturn(patient);
        when(visitService.getAllVisitTypes()).thenReturn(createTestVisitTypes());
        when(locationService.getAllLocations(false)).thenReturn(createTestLocations());
        when(visitService.getAllVisitAttributeTypes()).thenReturn(createTestVisitAttributeTypes());
        when(administrationService.getGlobalProperty("visits.visit-statuses")).thenReturn("SCHEDULED");
    when(patientTemplateService.getOrBuildPatientTemplate(any(Patient.class), anyString()))
        .thenReturn(createTestPatientTemplate());
        when(messagingGroupService.saveGroup(any(ScheduledServiceGroup.class))).thenReturn(createTestScheduledServiceGroup());

        controller.triggerSendMessage(PATIENT_UUID, "Visit reminder(Clinical visit-Morning-CFL Clinic),Health tip", "Call");

        verify(patientService).getPatientByUuid(PATIENT_UUID);
    verify(patientTemplateService).getOrBuildPatientTemplate(patient, "Visit reminder");
        verify(messagingGroupService).saveGroup(any(ScheduledServiceGroup.class));
        verify(messagesDeliveryService).scheduleDelivery(any(ScheduledExecutionContext.class));
    }

    private Patient buildTestPatient() {
        Patient patient = new Patient();
        patient.setPatientId(1);
        patient.setUuid(PATIENT_UUID);
        patient.setAttributes(Collections.singleton(createPersonAttribute()));
        return patient;
    }

    private List<VisitType> createTestVisitTypes() {
        VisitType visitType = new VisitType();
        visitType.setName("Clinical visit");
        return Collections.singletonList(visitType);
    }

    private List<Location> createTestLocations() {
        Location location = new Location();
        location.setName("CFL Clinic");
        return Collections.singletonList(location);
    }

    private List<VisitAttributeType> createTestVisitAttributeTypes() {
        VisitAttributeType visitStatusType = new VisitAttributeType();
        visitStatusType.setName("Visit Status");
        VisitAttributeType visitTimeType = new VisitAttributeType();
        visitTimeType.setName("Visit Time");
        return Arrays.asList(visitStatusType, visitTimeType);
    }

    private PatientTemplate createTestPatientTemplate() {
        PatientTemplate patientTemplate = new PatientTemplate();
        patientTemplate.setPatient(patient);
        patientTemplate.setActor(patient);
        patientTemplate.setTemplate(createTestTemplate());
        return patientTemplate;
    }

    private Template createTestTemplate() {
        Template template = new Template();
        template.setName("Visit reminder");
        return template;
    }

    private ScheduledServiceGroup createTestScheduledServiceGroup() {
        ScheduledServiceGroup scheduledServiceGroup = new ScheduledServiceGroup();
        scheduledServiceGroup.setId(100);
        scheduledServiceGroup.setScheduledServices(Collections.emptyList());
        return scheduledServiceGroup;
    }

    private PersonAttribute createPersonAttribute() {
        PersonAttribute personAttribute = new PersonAttribute();
        personAttribute.setAttributeType(createTestPersonAttributeType());
        personAttribute.setValue("111222333");
        return personAttribute;
    }

    private PersonAttributeType createTestPersonAttributeType() {
        PersonAttributeType personAttributeType = new PersonAttributeType();
        personAttributeType.setName("Telephone Number");
        return personAttributeType;
    }
}
