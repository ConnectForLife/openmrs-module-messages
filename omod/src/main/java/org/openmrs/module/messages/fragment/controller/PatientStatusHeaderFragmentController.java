package org.openmrs.module.messages.fragment.controller;

import org.openmrs.Patient;
import org.openmrs.module.messages.api.model.PersonStatus;
import org.openmrs.module.messages.api.util.PersonAttributeUtil;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

public class PatientStatusHeaderFragmentController {

    public void controller(FragmentModel model,
            @RequestParam(value = "patientId", required = false) Patient patient) {
        PersonStatus personStatus = PersonAttributeUtil.getPersonStatus(patient);
        if (personStatus == null) {
            model.addAttribute("shouldBeDisplayed", false);
        } else {
            model.addAttribute("shouldBeDisplayed", true);
            model.addAttribute("attributeLabel", "Patient Status");
            model.addAttribute("attributeValue", personStatus.getTitleKey());
        }
    }

}
