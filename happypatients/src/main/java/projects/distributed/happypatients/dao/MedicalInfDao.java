package projects.distributed.happypatients.dao;

import com.datastax.driver.core.ResultSet;

import java.util.UUID;

public class MedicalInfDao extends GenericDAO {

    public MedicalInfDao() {
        super();
    }

    public ResultSet getPatientsMedInf(UUID id) {
        ResultSet results = session.execute("SELECT * FROM medicalinformation where pid=" + id);
        return results;
    }

    public boolean createPatientsMedInf(UUID id, float height, float weight, float bmi, String bloodGrp) {
        try {
            session.execute("INSERT INTO medicalinformation (pid, height, weight, bmi, bloodGrp) VALUES (" + id
                    + ", " + height + ", "
                    + " " + weight + ", "
                    + " " + bmi + ", "
                    + " '" + bloodGrp + "')");
            return true;
        } catch (Exception e) {
            LOG.info("Create treatment error " + e.getMessage());
            return false;
        }
    }

    public boolean updatePatientsMedInf(UUID id, float height, float weight, float bmi, String bloodGrp) {
        try {

            session.execute("UPDATE medicalinformation set height=" + height + ", "
                    + "weight=" + weight + ",bmi=" + bmi + ",bloodgrp='" + bloodGrp + "' where pid=" + id);

            return true;
        } catch (Exception e) {
            LOG.error("Update updatePatientsMedInf error " + e.getMessage());
            return false;
        }
    }

    public boolean deletePatient(UUID id) {
        try {

            session.execute("DELETE from medicalinformation WHERE pid=" + id);

            return true;
        } catch (Exception e) {
            LOG.error("Delete patient error " + e.getMessage());
            return false;
        }
    }
}
