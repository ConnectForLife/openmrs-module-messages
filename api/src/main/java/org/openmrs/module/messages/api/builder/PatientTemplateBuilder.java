package org.openmrs.module.messages.api.builder;

import java.util.ArrayList;
import java.util.List;
import org.openmrs.Patient;
import org.openmrs.module.messages.api.model.Actor;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.api.util.ActorUtil;

public class PatientTemplateBuilder implements Builder<PatientTemplate> {

    private Template template;
    private Actor actor;
    private Patient patient;

    public PatientTemplateBuilder(Template template, Patient patient) {
        this.template = template;
        this.patient = patient;
        validateFields();
    }

    public PatientTemplateBuilder(Template template, Actor actor, Patient patient) {
        this.template = template;
        this.actor = actor;
        this.patient = patient;
        validateFields();
    }

    @Override
    public PatientTemplate build() {
        return actor == null ? buildForPatient() : buildForActor();
    }

    private PatientTemplate buildForPatient() {
        List<TemplateFieldValue> tfvList = new ArrayList<>();
        PatientTemplate patientTemplate = new PatientTemplate(
            patient.getPerson(),
            null,
            template.getServiceQuery(),
            template.getServiceQueryType(),
            patient,
            tfvList,
            template
        );
        for (TemplateField tf : template.getTemplateFields()) {
            tfvList.add(
                new TemplateFieldValue(tf.getDefaultValue(), tf, patientTemplate)
            );
        }
        return patientTemplate;
    }

    private PatientTemplate buildForActor() {
        List<TemplateFieldValue> tfvList = new ArrayList<>();
        PatientTemplate patientTemplate = new PatientTemplate(
            ActorUtil.getActorPerson(actor),
            actor.getRelationship(),
            template.getServiceQuery(),
            template.getServiceQueryType(),
            patient,
            tfvList,
            template
        );
        for (TemplateField tf : template.getTemplateFields()) {
            if (!ActorUtil.isActorPatient(actor, patient.getId())) {
                tfvList.add(
                    new TemplateFieldValue(
                        tf.getDefaultValueForSpecificActorOrGeneral(actor),
                        tf,
                        patientTemplate
                    )
                );
            }
        }
        return patientTemplate;
    }

    private void validateFields() throws IllegalArgumentException {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }

        if (template == null) {
            throw new IllegalArgumentException("Template cannot be null");
        }
    }
}
