package org.openmrs.module.messages.api.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents default patient template state DTO
 */
public class DefaultPatientTemplateStateDTO extends BaseDTO {

    private static final long serialVersionUID = -2795995672353929494L;

    private boolean defaultValuesUsed = true;

    private boolean allValuesDefault = true;

    private List<PatientTemplateDTO> lackingPatientTemplates = new ArrayList<>();

    private MessageDetailsDTO details;

    public DefaultPatientTemplateStateDTO() {
    }

    /**
     * Constructor of a DefaultPatientTemplateState DTO object
     *
     * @param lacking list of not yet saved patient templates
     * @param details DTO object containing detailed information about patient templates
     * @param anyPatientTemplateSaved identifies whether any patient template is saved
     */
    public DefaultPatientTemplateStateDTO(List<PatientTemplateDTO> lacking,
                                          MessageDetailsDTO details,
                                          boolean anyPatientTemplateSaved) {
        this.lackingPatientTemplates = lacking;
        this.defaultValuesUsed = lacking.size() > 0;
        this.allValuesDefault = defaultValuesUsed && !anyPatientTemplateSaved;
        this.details = details;
    }

    public boolean isDefaultValuesUsed() {
        return defaultValuesUsed;
    }

    public DefaultPatientTemplateStateDTO setDefaultValuesUsed(boolean defaultValuesUsed) {
        this.defaultValuesUsed = defaultValuesUsed;
        return this;
    }

    public boolean isAllValuesDefault() {
        return allValuesDefault;
    }

    public DefaultPatientTemplateStateDTO setAllValuesDefault(boolean allValuesDefault) {
        this.allValuesDefault = allValuesDefault;
        return this;
    }

    public List<PatientTemplateDTO> getLackingPatientTemplates() {
        return lackingPatientTemplates;
    }

    public MessageDetailsDTO getDetails() {
        return details;
    }

    public DefaultPatientTemplateStateDTO setDetails(MessageDetailsDTO details) {
        this.details = details;
        return this;
    }
}
