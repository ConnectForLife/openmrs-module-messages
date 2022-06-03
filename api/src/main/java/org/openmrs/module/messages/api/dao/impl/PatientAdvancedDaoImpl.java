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
import org.openmrs.Patient;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.messages.api.dao.PatientAdvancedDao;
import org.openmrs.module.messages.domain.criteria.BaseCriteria;

import java.util.List;

/**
 * The PatientAdvancedDaoImpl is a default implementation of PatientAdvancedDao.
 * <p>
 * See: moduleApplicationContext.xml
 * </p>
 */
public class PatientAdvancedDaoImpl implements PatientAdvancedDao {
    private DbSessionFactory dbSessionFactory;

    @Override
    public List<Patient> getPatients(int firstResult, int maxResults, BaseCriteria criteria) {
        final Criteria patientCriteria = dbSessionFactory.getCurrentSession().createCriteria(Patient.class);

        if (criteria != null) {
            criteria.initHibernateCriteria(patientCriteria);
            criteria.loadHibernateCriteria(patientCriteria);
        }

        patientCriteria.setFirstResult(firstResult);

        if (maxResults > 0) {
            patientCriteria.setMaxResults(maxResults);
        }

        return patientCriteria.list();
    }

    public DbSessionFactory getDbSessionFactory() {
        return dbSessionFactory;
    }

    public void setDbSessionFactory(DbSessionFactory dbSessionFactory) {
        this.dbSessionFactory = dbSessionFactory;
    }
}
