package projects.distributed.happypatients.springboot.service;

import projects.distributed.happypatients.springboot.model.TreatmentInformation;

import java.util.List;

public interface TreatmentInformationService {

    public boolean addTreatmentInformation(TreatmentInformation treatmentInformation);

    public List<TreatmentInformation> getTreatmentInformation(String id);

    public boolean deletePatientTreatment(String id);

    public boolean deletePatientTreatment(String id, String medicalCond);

    public boolean updateTreatment(TreatmentInformation treatmentInformation);

}
