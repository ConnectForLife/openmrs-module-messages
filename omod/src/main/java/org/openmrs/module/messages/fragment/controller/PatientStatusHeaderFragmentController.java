package org.openmrs.module.messages.fragment.controller;

import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.module.messages.api.util.PersonAttributeUtil;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

public class PatientStatusHeaderFragmentController {

    public void controller(FragmentModel model,
            @RequestParam(value = "patientId", required = false) Patient patient) {
        PersonAttribute attribute = PersonAttributeUtil.getPersonStatusAttribute(patient);
        if (attribute == null) {
            model.addAttribute("shouldBeDisplayed", false);
        } else {
            model.addAttribute("shouldBeDisplayed", true);
            model.addAttribute("attributeLabel", attribute.getAttributeType().getName());
            model.addAttribute("attributeValue", PersonAttributeUtil.getValue(attribute));
        }
    }

}
