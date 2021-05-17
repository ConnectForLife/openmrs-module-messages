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
