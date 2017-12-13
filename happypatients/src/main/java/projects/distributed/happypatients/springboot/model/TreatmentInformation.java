package projects.distributed.happypatients.springboot.model;

import java.util.Date;

public class TreatmentInformation {
    private String patientId;
    private TreatmentStatus treatmentStatus;
    private String doctorName;
    private String report;
    private String diagnosis;
    private Date startDate;
    private Date endDate;
    private String medicalCondition;

    public TreatmentInformation() {

    }

    public TreatmentInformation(TreatmentInformation treatmentInformation) {
        this.patientId = treatmentInformation.patientId;
        this.treatmentStatus = treatmentInformation.treatmentStatus;
        this.doctorName = treatmentInformation.doctorName;
        this.report = treatmentInformation.report;
        this.diagnosis = treatmentInformation.diagnosis;
        this.startDate = treatmentInformation.startDate;
        this.endDate = treatmentInformation.endDate;
        this.medicalCondition = treatmentInformation.medicalCondition;
    }

    public TreatmentInformation(String patientId, TreatmentStatus treatmentStatus, String doctorName, String report, String diagnosis, Date startDate, Date endDate, String medicalCondition) {
        this.patientId = patientId;
        this.treatmentStatus = treatmentStatus;
        this.doctorName = doctorName;
        this.report = report;
        this.diagnosis = diagnosis;
        this.startDate = startDate;
        this.endDate = endDate;
        this.medicalCondition = medicalCondition;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public TreatmentStatus getTreatmentStatus() {
        return treatmentStatus;
    }

    public void setTreatmentStatus(TreatmentStatus treatmentStatus) {
        this.treatmentStatus = treatmentStatus;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getMedicalCondition() {
        return medicalCondition;
    }

    public void setMedicalCondition(String medicalCondition) {
        this.medicalCondition = medicalCondition;
    }
}
