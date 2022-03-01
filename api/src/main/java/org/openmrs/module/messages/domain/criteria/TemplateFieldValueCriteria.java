package org.openmrs.module.messages.domain.criteria;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.openmrs.module.messages.api.model.PatientTemplate;

import java.io.Serializable;

public class TemplateFieldValueCriteria extends BaseOpenmrsDataCriteria implements Serializable {

  private static final long serialVersionUID = 4992512134818365681L;

  private PatientTemplate patientTemplate;

  private String fieldTypeName;

  @Override
  public void loadHibernateCriteria(Criteria hibernateCriteria) {
    if (patientTemplate != null) {
      hibernateCriteria.add(Restrictions.eq("patientTemplate", patientTemplate));
    }

    if (fieldTypeName != null) {
      hibernateCriteria
          .createAlias("templateField", "templateField")
          .add(Restrictions.eq("templateField.name", fieldTypeName));
    }
  }

  public TemplateFieldValueCriteria setPatientTemplate(PatientTemplate patientTemplate) {
    this.patientTemplate = patientTemplate;
    return this;
  }

  public TemplateFieldValueCriteria setFieldTypeName(String fieldTypeName) {
    this.fieldTypeName = fieldTypeName;
    return this;
  }
}
