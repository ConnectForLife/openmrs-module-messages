package org.openmrs.module.messages.api.dao.impl;

import org.hibernate.criterion.Restrictions;
import org.openmrs.Concept;
import org.openmrs.ConceptAttribute;
import org.openmrs.api.db.hibernate.HibernateOpenmrsObjectDAO;
import org.openmrs.module.messages.api.dao.ExtendedConceptDAO;

import java.util.List;

public class ExtendedConceptDAOImpl extends HibernateOpenmrsObjectDAO<Concept> implements ExtendedConceptDAO {
  @Override
  public List<ConceptAttribute> getConceptAttributesByTypeUuid(String uuid) {
    return this.sessionFactory
        .getCurrentSession()
        .createCriteria(ConceptAttribute.class)
        .add(Restrictions.eq("voided", false))
        .createCriteria("attributeType", "at")
        .add(Restrictions.eq("uuid", uuid))
        .list();
  }
}
