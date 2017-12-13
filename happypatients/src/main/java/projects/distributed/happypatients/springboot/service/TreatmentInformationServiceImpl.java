package projects.distributed.happypatients.springboot.service;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projects.distributed.happypatients.converters.PatientConverter;
import projects.distributed.happypatients.converters.TreatmentConverter;
import projects.distributed.happypatients.dao.PatientDAO;
import projects.distributed.happypatients.dao.TreatmentDAO;
import projects.distributed.happypatients.springboot.cache.CacheConnector;
import projects.distributed.happypatients.springboot.model.Patient;
import projects.distributed.happypatients.springboot.model.TreatmentInformation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TreatmentInformationServiceImpl implements TreatmentInformationService {

    @Autowired
    TreatmentDAO treatmentDAO;

    @Autowired
    PatientDAO patientDAO;

    @Autowired
    PatientConverter patientConverter;

    @Autowired
    TreatmentConverter treatmentConverter;

    @Autowired
    CacheConnector cacheConnector;

    @Override
    public boolean addTreatmentInformation(TreatmentInformation treatmentInformation) {
        boolean isSuccess = treatmentDAO.createTreatment(UUID.fromString(treatmentInformation.getPatientId()),
                treatmentInformation.getMedicalCondition(), treatmentInformation.getDiagnosis(),
                treatmentInformation.getDoctorName(), treatmentInformation.getEndDate(),
                treatmentInformation.getReport(), treatmentInformation.getStartDate(),
                treatmentInformation.getTreatmentStatus().toString());

        if (isSuccess) {
            Patient patient = patientConverter.convert(patientDAO.getPatient(UUID.fromString(treatmentInformation.getPatientId())).one());
            patient.setTreatments(getTreatmentInformation(treatmentInformation.getPatientId()));

            cacheConnector.savePatientToCache(patient.getId(), patient);
        }
        return isSuccess;
    }

    @Override
    public List<TreatmentInformation> getTreatmentInformation(String id) {
        List<TreatmentInformation> treatmentInformations = new ArrayList<>();
        ResultSet results = treatmentDAO.getPatientsTreatment(UUID.fromString(id));
        for (Row row : results) {
            treatmentInformations.add(treatmentConverter.convert(row));
        }
        return treatmentInformations;
    }

    @Override
    public boolean deletePatientTreatment(String id) {
        boolean isSuccess = treatmentDAO.deletePatientTreatment(UUID.fromString(id));

        if (isSuccess) {
            if (cacheConnector.getPatientFromCache(id) != null) {
                // This means patient information is present in cache
                Patient patient = patientConverter.convert(patientDAO.getPatient(UUID.fromString(id)).one());
                patient.setTreatments(getTreatmentInformation(id));
                cacheConnector.savePatientToCache(id, patient);
            }
        }

        return isSuccess;
    }

    @Override
    public boolean deletePatientTreatment(String id, String medicalCond) {
        boolean isSuccess = treatmentDAO.deletePatientTreatment(UUID.fromString(id), medicalCond);

        if (isSuccess) {
            if (cacheConnector.getPatientFromCache(id) != null) {
                // This means patient information is present in cache
                Patient patient = patientConverter.convert(patientDAO.getPatient(UUID.fromString(id)).one());
                patient.setTreatments(getTreatmentInformation(id));
                cacheConnector.savePatientToCache(id, patient);
            }
        }

        return isSuccess;
    }

    @Override
    public boolean updateTreatment(TreatmentInformation treatmentInformation) {
        boolean isSuccess = treatmentDAO.updatePatient(UUID.fromString(treatmentInformation.getPatientId()),
                treatmentInformation.getMedicalCondition(), treatmentInformation.getDiagnosis(),
                treatmentInformation.getDoctorName(), treatmentInformation.getEndDate(),
                treatmentInformation.getReport(), treatmentInformation.getEndDate(),
                treatmentInformation.getTreatmentStatus().toString());

        if (isSuccess) {
            if (cacheConnector.getPatientFromCache(treatmentInformation.getPatientId()) != null) {
                // This means patient information is present in cache
                Patient patient = patientConverter.convert(patientDAO.getPatient(UUID.fromString(treatmentInformation.getPatientId())).one());
                patient.setTreatments(getTreatmentInformation(treatmentInformation.getPatientId()));
                cacheConnector.savePatientToCache(treatmentInformation.getPatientId(), patient);
            }
        }

        return isSuccess;
    }

}
