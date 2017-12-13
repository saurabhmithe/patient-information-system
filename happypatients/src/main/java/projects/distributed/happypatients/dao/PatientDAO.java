package projects.distributed.happypatients.dao;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.utils.UUIDs;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class PatientDAO extends GenericDAO {


    public PatientDAO() {
        super();
    }

    public String createPatient(String name, Date dob, String phoneNumber, String address, String bloodgrp, float height, float weight) {
        try {
            UUID id = UUIDs.timeBased();
            String query = "INSERT INTO patient (pid, pname, dob, phonenumber, address, bloodgrp, height, weight) VALUES (" + id + ",'" + name + "'," + dob.getTime() + ",'" + phoneNumber + "','" + address + "','" + bloodgrp + "'," + height + "," + weight + ")";
            LOG.info(query);
            // Insert record into the patient table
            session.execute(query);

            return id.toString();
        } catch (Exception e) {
            LOG.info("Create patient error " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean updatePatient(UUID id, String name, Date dob, String phoneNumber, String address, String bloodgrp, float height, float weight) {
        try {

            // Update patient record
            session.execute("UPDATE patient set pname='" + name + "', dob=" + dob.getTime() + ", phonenumber='" + phoneNumber + "', address='" + address + "', bloodgrp='" + bloodgrp + "', height=" + height + ",weight=" + weight + " where pid=" + id);

            return true;
        } catch (Exception e) {
            LOG.error("Update patient error " + e.getMessage());
            return false;
        }
    }

    public boolean deletePatient(UUID id) {
        try {
            String query = "DELETE from patient WHERE pid=" + id;
            LOG.info(query);
            // Delete patient record
            session.execute(query);

            return true;
        } catch (Exception e) {
            LOG.error("Delete patient error " + e.getMessage());
            return false;
        }
    }

    public ResultSet getPatient(UUID id) {
        // Use select to get the user we just entered
        ResultSet results = session.execute("SELECT * FROM patient where pid=" + id);

        return results;
    }

    public ResultSet getPatients() {
        // Use select to get the user we just entered
        ResultSet results = session.execute("SELECT * FROM patient");

        return results;
    }

}
