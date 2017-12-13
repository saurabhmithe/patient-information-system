package projects.distributed.happypatients.springboot.service;

import com.datastax.driver.core.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projects.distributed.happypatients.converters.PatientConverter;
import projects.distributed.happypatients.dao.PatientDAO;
import projects.distributed.happypatients.springboot.cache.CacheConnector;
import projects.distributed.happypatients.springboot.model.Patient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PatientServiceImpl implements PatientService {

    static final Logger LOG = LoggerFactory.getLogger(PatientServiceImpl.class);

    @Autowired
    PatientDAO patientDAO;

    @Autowired
    PatientConverter patientConverter;

    @Autowired
    TreatmentInformationService treatmentService;

    @Autowired
    CacheConnector cacheConnector;

    /**
     * Return a list of all the patients present in the database.
     * No need to contact cache server here since the data would be present in the database itself.
     *
     * @return List of all patients
     */
    @Override
    public List<Patient> findAllPatients() {
        LOG.info("Getting all patients from the database.");
        List<Patient> patients = new ArrayList<>();
        for (Row patient : patientDAO.getPatients()) {
            Patient temp = patientConverter.convert(patient);
            temp.setTreatments(treatmentService.getTreatmentInformation(temp.getId()));
            patients.add(temp);
        }
        LOG.info("Returning {} patients.", patients.size());
        return patients;
    }

    /**
     * Return a patient based on patient id.
     * If the patient is present in the cache, return from cache. Else, return from id.
     *
     * @param id
     * @return A patient matching the id.
     */
    @Override
    public Patient findPatientById(String id) {
        LOG.info("Searching for patient with id {} in the cache.", id);
        // Check if patient is in the cache
        Patient patient = cacheConnector.getPatientFromCache(id);

        // Get patient from database if not found in cache
        if (patient == null) {
            LOG.info("Patient not found in the cache. Searching in the database.");
            patient = patientConverter.convert(patientDAO.getPatient(UUID.fromString(id)).one());
            // When we fetch patient, we get just the basic information.
            // We add treatment information to the object before it is returned to the user.
            if(patient != null) {
                LOG.info("Found patient in the database.");
                patient.setTreatments(treatmentService.getTreatmentInformation(id));
            }
        } else {
            LOG.info("Found patient in the cache.");
        }

        return patient;
    }

    /**
     * Check if the patient exists in the database.
     * No need to check with cache server since not all the patients would be present in cache.
     *
     * @param patient
     * @return True if patient exists in database. False otherwise.
     */
    @Override
    public boolean isPatientExist(Patient patient) {
        return patientDAO.getPatient(UUID.fromString(patient.getId())).iterator().hasNext();
    }

    /**
     * Create a record for a new patient.
     *
     * @param patient
     * @return Id of the saved patient if save was successful. null if unsuccessful.
     */
    @Override
    public String savePatient(Patient patient) {
        LOG.info("Creating a new patient in database with basic details.");
        String id = patientDAO.createPatient(patient.getName(), patient.getDateOfBirth(), patient.getPhoneNumber(), patient.getAddress(), patient.getBldGroup(), patient.getHeight(), patient.getWeight());
        return id;
    }

    @Override
    public boolean updatePatient(Patient patient) {
        LOG.info("Updating patient information.");

        boolean isSuccess = patientDAO.updatePatient(UUID.fromString(patient.getId()), patient.getName(), patient.getDateOfBirth(), patient.getPhoneNumber(), patient.getAddress(), patient.getBldGroup(), patient.getHeight(), patient.getWeight());

        if(isSuccess) {
            LOG.info("Update in database successfull.");
            if (cacheConnector.getPatientFromCache(patient.getId()) != null) {
                LOG.info("Patient info found in cache. Updating cache.");
                cacheConnector.savePatientToCache(patient.getId(), patient);
                LOG.info("Update in cache successful.");
            }
        }

        return isSuccess;
    }

    @Override
    public boolean deletePatientById(String id) {
        LOG.info("Deleting patient information.");

        boolean isSuccess = patientDAO.deletePatient(UUID.fromString(id));

        if(isSuccess) {
            LOG.info("Delete patient successful.");
            if (cacheConnector.getPatientFromCache(id) != null) {
                LOG.info("Patient info found in cache. Updating cache.");
                cacheConnector.removePatientFromCache(id);
                LOG.info("Delete from cache successful.");
            }
        }

        return isSuccess;
    }

    @Override
    public boolean deleteAllPatients() {
        LOG.info("Deleting all patients.");
        return false;
    }
}
