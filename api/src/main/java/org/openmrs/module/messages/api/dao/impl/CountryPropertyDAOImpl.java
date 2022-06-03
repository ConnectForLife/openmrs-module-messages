/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Concept;
import org.openmrs.api.db.hibernate.HibernateOpenmrsMetadataDAO;
import org.openmrs.module.messages.api.dao.CountryPropertyDAO;
import org.openmrs.module.messages.api.model.CountryProperty;

import java.util.List;
import java.util.Optional;

public class CountryPropertyDAOImpl extends HibernateOpenmrsMetadataDAO<CountryProperty>
    implements CountryPropertyDAO {

  public CountryPropertyDAOImpl() {
    super(CountryProperty.class);
  }

  @Override
  public Optional<CountryProperty> getCountryProperty(Concept country, String name) {
    final CountryProperty countryProperty = getPropertyByCountryAndName(country, name);
    return Optional.ofNullable(countryProperty);
  }

  @Override
  public List<CountryProperty> getAll(
      String namePrefix, boolean includeRetired, Integer firstResult, Integer maxResults) {
    final Criteria criteria =
        this.sessionFactory.getCurrentSession().createCriteria(CountryProperty.class);

    if (!includeRetired) {
      criteria.add(Restrictions.eq(CountryProperty.RETIRED_PROP_NAME, Boolean.FALSE));
    }

    criteria.add(Restrictions.like(CountryProperty.NAME_PROP_NAME, namePrefix, MatchMode.START));

    criteria.setFirstResult(firstResult);
    criteria.setMaxResults(maxResults);
    return criteria.list();
  }

  @Override
  public int getAllCount(String namePrefix, boolean includeRetired) {
    final Criteria countCriteria = getCountCriteria(includeRetired);
    countCriteria.add(Restrictions.like(CountryProperty.NAME_PROP_NAME, namePrefix, MatchMode.START));
    return executeCountCriteria(countCriteria);
  }

  @Override
  public int getAllCount(boolean includeRetired) {
    return executeCountCriteria(getCountCriteria(includeRetired));
  }

  private CountryProperty getPropertyByCountryAndName(Concept country, String name) {
    final Criteria criteria =
        sessionFactory
            .getCurrentSession()
            .createCriteria(CountryProperty.class)
            .add(Restrictions.eq(CountryProperty.RETIRED_PROP_NAME, Boolean.FALSE))
            .add(getCountryFieldEqExpression(country))
            .add(Restrictions.eq(CountryProperty.NAME_PROP_NAME, name));
    return (CountryProperty) criteria.uniqueResult();
  }

  private Criterion getCountryFieldEqExpression(Concept country) {
    if (country == null) {
      return Restrictions.isNull(CountryProperty.COUNTRY_PROP_NAME);
    } else {
      return Restrictions.eq(CountryProperty.COUNTRY_PROP_NAME, country);
    }
  }

  private Criteria getCountCriteria(boolean includeRetired) {
    final Criteria countCriteria =
        this.sessionFactory.getCurrentSession().createCriteria(CountryProperty.class);
    countCriteria.setProjection(Projections.rowCount());

    if (!includeRetired) {
      countCriteria.add(Restrictions.eq(CountryProperty.RETIRED_PROP_NAME, Boolean.FALSE));
    }

    return countCriteria;
  }

  private int executeCountCriteria(Criteria countCriteria) {
    final Number count = (Number) countCriteria.uniqueResult();
    return count == null ? 0 : count.intValue();
  }
}
