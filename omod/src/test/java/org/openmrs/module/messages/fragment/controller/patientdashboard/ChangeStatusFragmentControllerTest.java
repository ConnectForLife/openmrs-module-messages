package org.openmrs.module.messages.fragment.controller.patientdashboard;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.module.messages.Constant;
import org.openmrs.module.messages.api.dto.PersonStatusDTO;
import org.openmrs.module.messages.api.model.PersonStatus;
import org.openmrs.module.messages.api.util.PersonStatusHelper;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class ChangeStatusFragmentControllerTest {

    private static final String PERSON_ID = "7";

    @Mock
    private FragmentModel fragmentModel;

    @Mock
    private PersonStatusHelper personStatusHelper;

    private ChangeStatusFragmentController controller = new ChangeStatusFragmentController();

    private List<String> possibleReasons;

    private List<PersonStatus> possibleStatuses;

    private PersonStatusDTO status;

    @Before
    public void setUp() {
        status = buildPersonStatus();
        when(personStatusHelper.getStatus(PERSON_ID)).thenReturn(status);

        possibleReasons = new ArrayList<>();
        possibleReasons.add(Constant.STATUS_REASON_DECEASED);
        possibleReasons.add(Constant.STATUS_REASON_LOST_FOLLOW_UP);
        possibleReasons.add(Constant.STATUS_REASON_PAUSE);
        possibleReasons.add(Constant.STATUS_REASON_VACATION);
        possibleReasons.add(Constant.STATUS_REASON_TRANSFERRED);
        possibleReasons.add(Constant.STATUS_REASON_OTHER);

        possibleStatuses = new ArrayList<>();
        possibleStatuses.add(PersonStatus.ACTIVATED);
        possibleStatuses.add(PersonStatus.DEACTIVATED);

        when(personStatusHelper.getPossibleReasons()).thenReturn(possibleReasons);
    }

    @Test
    public void shouldProperlyAddAttributesWhenPersonStatusIsNull() {
        controller.controller(fragmentModel, personStatusHelper, PERSON_ID);

        verify(fragmentModel).addAttribute("personStatusValues", possibleStatuses);
        verify(fragmentModel).addAttribute("possibleReasons", possibleReasons);
    }

    @Test
    public void shouldProperlyAddAttributesWhenPersonStatusIsNotNull() {
        controller.controller(fragmentModel, personStatusHelper, PERSON_ID);

        verify(fragmentModel).addAttribute("personStatusValues", possibleStatuses);
        verify(fragmentModel).addAttribute("possibleReasons", possibleReasons);
        verify(fragmentModel).addAttribute("personStatusValue", status.getValue());
        verify(fragmentModel).addAttribute("personStatusReason", status.getReason());
    }

    @Test
    public void shouldProperlyUpdatePersonStatus() {
        PersonStatusDTO updatedStatus = new PersonStatusDTO()
                .setPersonId(PERSON_ID)
                .setValue(PersonStatus.DEACTIVATED.name())
                .setReason(Constant.STATUS_REASON_VACATION);

        controller.update(PERSON_ID, updatedStatus.getValue(), updatedStatus.getReason(), personStatusHelper);
        personStatusHelper.saveStatus(updatedStatus);

        assertEquals(PersonStatus.DEACTIVATED.name(), updatedStatus.getValue());
        assertEquals(Constant.STATUS_REASON_VACATION, updatedStatus.getReason());
    }

    private PersonStatusDTO buildPersonStatus() {
         PersonStatusDTO personStatus = new PersonStatusDTO()
                .setPersonId(PERSON_ID)
                .setValue(PersonStatus.ACTIVATED.name())
                .setReason(Constant.STATUS_REASON_DECEASED);
        return personStatus;
    }
}
