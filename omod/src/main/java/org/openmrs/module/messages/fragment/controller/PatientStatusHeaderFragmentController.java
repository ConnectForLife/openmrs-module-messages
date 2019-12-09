package org.openmrs.module.messages.fragment.controller;

import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.module.messages.api.model.PatientStatus;
import org.openmrs.module.messages.api.util.ConfigConstants;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

public class PatientStatusHeaderFragmentController {

    public void controller(FragmentModel model,
            @RequestParam(value = "patientId", required = false) Patient patient) {
        PersonAttribute attribute = getPatientStatusAttribute(patient);
        if (attribute == null) {
            model.addAttribute("shouldBeDisplayed", false);
        } else {
            model.addAttribute("shouldBeDisplayed", true);
            model.addAttribute("attributeLabel", attribute.getAttributeType().getName());
            model.addAttribute("attributeValue", getValue(attribute));
        }
    }

    private PersonAttribute getPatientStatusAttribute(Patient patient) {
        if (patient != null) {
            return patient.getAttribute(ConfigConstants.PATIENT_STATUS_ATTRIBUTE_TYPE_NAME);
        }
        return null;
    }

    private String getValue(PersonAttribute attribute) {
        String result = null;
        try {
            PatientStatus status = PatientStatus.valueOf(attribute.getValue());
            result = status.getTitleKey();
        } catch (IllegalArgumentException ex) {
            result = attribute.getValue();
        }
        return result;
    }
}
