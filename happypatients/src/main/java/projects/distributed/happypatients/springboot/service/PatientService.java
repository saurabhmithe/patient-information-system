package projects.distributed.happypatients.springboot.service;

import projects.distributed.happypatients.springboot.model.Patient;

import java.util.List;

public interface PatientService {
    public List<Patient> findAllPatients();

    public Patient findPatientById(String id);

    public boolean isPatientExist(Patient patient);

    public String savePatient(Patient patient);

    public boolean updatePatient(Patient patient);

    public boolean deletePatientById(String id);

    public boolean deleteAllPatients();

}
