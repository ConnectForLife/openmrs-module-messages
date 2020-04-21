package org.openmrs.module.messages.api.service;

import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.module.messages.api.dto.ContactTimeDTO;
import org.openmrs.module.messages.api.model.Actor;
import org.openmrs.module.messages.api.model.ActorType;

import java.util.List;

/**
 * Provides methods related to the actor service management
 */
public interface ActorService {

    /**
     * Finds all related actors for patient object
     *
     * @param patient object of patient for whom the actors are sought
     * @return list of actors related to the patient
     */
    List<Actor> getAllActorsForPatient(Patient patient);

    /**
     * Finds all related actors for patientId
     *
     * @param patientId id of patient for whom the actors are sought
     * @return list of actors related to the patient with patientId
     */
    List<Actor> getAllActorsForPatientId(Integer patientId);

    /**
     * Finds all related actors for person object
     *
     * @param person object of person for whom the actors are sought
     * @param isPatient identifies whether the person is a patient
     * @return list of actors related to the person
     */
    List<Actor> getAllActorsForPerson(Person person, boolean isPatient);

    /**
     * Finds all related actors for personId
     *
     * @param personId id of person for whom the actors are sought
     * @param isPatient identifies whether the person is a patient
     * @return list of actors related to the person with personId
     */
    List<Actor> getAllActorsForPersonId(Integer personId, boolean isPatient);

    /**
     * Finds all actor types
     *
     * @return list of available actor types
     */
    List<ActorType> getAllActorTypes();

    /**
     * Finds the best contact time for person
     *
     * @param personId id of person for whom the best contact time is sought
     * @return best contact time for person with personId
     */
    String getContactTime(Integer personId);

    /**
     * Finds the best contact times for list of people
     *
     * @param personIds list of person ids for whom the best contact time are sought
     * @return list of DTO objects containing best contact times
     */
    List<ContactTimeDTO> getContactTimes(List<Integer> personIds);

    /**
     * Saves the best contact time
     *
     * @param contactTimeDTO DTO object containing data to save best contact time (personId, time)
     */
    void saveContactTime(ContactTimeDTO contactTimeDTO);

    /**
     * Saves the list of best contact times
     *
     * @param contactTimeDTOs list of DTO objects containing data to save best contact times (personId, time)
     */
    void saveContactTimes(List<ContactTimeDTO> contactTimeDTOs);
}
