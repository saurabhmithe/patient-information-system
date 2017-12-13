package projects.distributed.happypatients.springboot.service;

import com.datastax.driver.core.Row;
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
        List<Patient> patients = new ArrayList<>();
        for (Row patient : patientDAO.getPatients()) {
            Patient temp = patientConverter.convert(patient);
            temp.setTreatments(treatmentService.getTreatmentInformation(temp.getId()));
            patients.add(temp);
        }
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

        // Check if patient is in the cache
        Patient patient = cacheConnector.getPatientFromCache(id);

        // Get patient from database if not found in cache
        if (patient == null) {
            patient = patientConverter.convert(patientDAO.getPatient(UUID.fromString(id)).one());
            // When we fetch patient, we get just the basic information.
            // We add treatment information to the object before it is returned to the user.
            patient.setTreatments(treatmentService.getTreatmentInformation(id));
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
        String id = patientDAO.createPatient(patient.getName(), patient.getDateOfBirth(), patient.getPhoneNumber(), patient.getAddress(), patient.getBldGroup(), patient.getHeight(), patient.getWeight());
        return id;
    }

    @Override
    public boolean updatePatient(Patient patient) {
        if (cacheConnector.getPatientFromCache(patient.getId()) != null) {
            cacheConnector.savePatientToCache(patient.getId(), patient);
        }
        return patientDAO.updatePatient(UUID.fromString(patient.getId()), patient.getName(), patient.getDateOfBirth(), patient.getPhoneNumber(), patient.getAddress(), patient.getBldGroup(), patient.getHeight(), patient.getWeight());
    }

    @Override
    public boolean deletePatientById(String id) {
        if (cacheConnector.getPatientFromCache(id) != null) {
            cacheConnector.removePatientFromCache(id);
        }
        return patientDAO.deletePatient(UUID.fromString(id));
    }

    @Override
    public boolean deleteAllPatients() {
        return false;
    }
}
