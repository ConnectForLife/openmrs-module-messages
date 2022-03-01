package org.openmrs.module.messages.domain.criteria;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;

import java.time.ZonedDateTime;
import java.util.Date;

public class LastResponseCriteria extends ActorResponseCriteria {

  private static final long serialVersionUID = 7829140952383391585L;

  private Integer patientId;
  private Integer actorId;
  private Integer conceptQuestionId;
  private String textQuestion;
  private Integer limit;
  private String serviceType;
  private ZonedDateTime answeredTimeFrom;
  private ZonedDateTime answeredTimeTo;

  @Override
  public void loadHibernateCriteria(Criteria hibernateCriteria) {
    loadWhereStatements(hibernateCriteria);
    loadOrderBy(hibernateCriteria);
  }

  @Override
  protected void loadWhereStatements(Criteria hibernateCriteria) {
    if (patientId != null) {
      hibernateCriteria.add(Restrictions.eq("patient.personId", patientId));
    }
    if (actorId != null) {
      hibernateCriteria.add(Restrictions.eq("actor.personId", actorId));
    }
    if (conceptQuestionId != null) {
      hibernateCriteria.add(Restrictions.eq("question.conceptId", conceptQuestionId));
    }
    if (textQuestion != null) {
      hibernateCriteria.add(Restrictions.eq("textQuestion", textQuestion));
    }
    if (limit != null) {
      hibernateCriteria.setFirstResult(0);
      hibernateCriteria.setMaxResults(limit);
    }
    if (serviceType != null) {
      DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ScheduledServiceGroup.class);
      detachedCriteria.setProjection(Property.forName("id"));
      detachedCriteria
          .createAlias("scheduledServices", "mss")
          .createAlias("mss.patientTemplate", "mpt")
          .createAlias("mpt.template", "mt")
          .add(Restrictions.eq("mt.name", serviceType));

      hibernateCriteria.add(Property.forName("sourceId").in(detachedCriteria));
    }
    if (answeredTimeFrom != null) {
      hibernateCriteria.add(
          Restrictions.ge("answeredTime", Date.from(answeredTimeFrom.toInstant())));
    }
    if (answeredTimeTo != null) {
      hibernateCriteria.add(Restrictions.lt("answeredTime", Date.from(answeredTimeTo.toInstant())));
    }
  }

  public LastResponseCriteria setPatientId(Integer patientId) {
    this.patientId = patientId;
    return this;
  }

  public LastResponseCriteria setActorId(Integer actorId) {
    this.actorId = actorId;
    return this;
  }

  public LastResponseCriteria setConceptQuestionId(Integer conceptQuestionId) {
    this.conceptQuestionId = conceptQuestionId;
    return this;
  }

  public LastResponseCriteria setTextQuestion(String textQuestion) {
    this.textQuestion = textQuestion;
    return this;
  }

  public LastResponseCriteria setLimit(Integer limit) {
    this.limit = limit;
    return this;
  }

  public LastResponseCriteria setServiceType(String serviceType) {
    this.serviceType = serviceType;
    return this;
  }

  public LastResponseCriteria setAnsweredTimeFrom(ZonedDateTime answeredTimeFrom) {
    this.answeredTimeFrom = answeredTimeFrom;
    return this;
  }

  public LastResponseCriteria setAnsweredTimeTo(ZonedDateTime answeredTimeTo) {
    this.answeredTimeTo = answeredTimeTo;
    return this;
  }
}
