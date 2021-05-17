package org.openmrs.module.messages.api.dao;

import org.openmrs.Patient;
import org.openmrs.module.messages.domain.criteria.BaseCriteria;

import java.util.List;

/**
 * The PatientAdvancedDao Class.
 * <p>
 * The Patient Advanced DAO provides advanced methods to access Patients, among others it provides the ability to read
 * Patients using {@link BaseCriteria} objects.
 * </p>
 */
public interface PatientAdvancedDao {
    List<Patient> getPatients(int firstResult, int maxResults, BaseCriteria criteria);
}
