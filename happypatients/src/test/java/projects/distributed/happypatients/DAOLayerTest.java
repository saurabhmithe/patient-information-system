package projects.distributed.happypatients;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import projects.distributed.happypatients.dao.MedicalInfDao;
import projects.distributed.happypatients.dao.PatientDAO;
import projects.distributed.happypatients.dao.TreatmentDAO;
import projects.distributed.happypatients.springboot.utilities.CassandraConnector;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.*;

public class DAOLayerTest {

    private static PatientDAO patientDAO;
    private static TreatmentDAO treatmentDAO;
    private static MedicalInfDao medicalInfDAO;
    private static CassandraConnector cc;
    @Value("${cassandra.server.url}")
    private static String serverUrl = "10.0.0.12";
    @Value("${cassandra.server.port}")
    private static Integer serverPort = 9042;
    UUID id;
    String name, phoneNumber;
    Date dob;

    @BeforeClass
    public static void setUpBeforeClass() {
        cc = new CassandraConnector();
        cc.connect(serverUrl, serverPort);
        patientDAO = new PatientDAO();
        treatmentDAO = new TreatmentDAO();
        medicalInfDAO = new MedicalInfDao();
    }

    @AfterClass
    public static void tearDownAfterClass() {
        cc.close();
    }

    @Test
    public void testCreatePatient() {
        assertNotNull(patientDAO.createPatient("TOMTEST", new Date(), "+19029641947", "SAN JOSE", "AB", 5.8f, 58f));
        ResultSet results = patientDAO.getPatients();
        for (Row row : results) {
            this.name = row.getString("pname");
            this.id = row.getUUID("pid");
            this.phoneNumber = row.getString("phonenumber");
            this.dob = row.getTimestamp("dob");
            if (name.equals("TOMTEST")) {
                break;
            }
        }
        assertEquals("TOMTEST", name);
    }

    @Test
    public void testGetPatients() {
        ResultSet results = patientDAO.getPatients();
        for (Row row : results) {
            this.id = row.getUUID("pid");
            this.name = row.getString("pname");
            this.phoneNumber = row.getString("phonenumber");
            this.dob = row.getTimestamp("dob");
        }
        assertNotNull(id);
    }

    @Test
    public void testGetPatient() {
        ResultSet results = patientDAO.getPatients();
        for (Row row : results) {
            this.id = row.getUUID("pid");
            this.name = row.getString("pname");
            this.phoneNumber = row.getString("phonenumber");
            this.dob = row.getTimestamp("dob");
        }
        results = patientDAO.getPatient(id);
        assertEquals(name, results.one().getString("pname"));
    }

    @Test
    public void testUpdatePatient() {
        ResultSet results = patientDAO.getPatients();
        for (Row row : results) {
            this.name = row.getString("pname");
            this.id = row.getUUID("pid");
            this.phoneNumber = row.getString("phonenumber");
            this.dob = row.getTimestamp("dob");
            if (name.equals("TOMTEST")) {
                break;
            }
        }
        assertTrue(patientDAO.updatePatient(id, "TOMTEST1", dob, phoneNumber, "SAN JOSE", "AB", 5.8f, 58));
        results = patientDAO.getPatient(id);
        assertEquals("TOMTEST1", results.one().getString("pname"));
    }

    @Test
    public void testCreateTreatment() {
        ResultSet results = patientDAO.getPatients();
        for (Row row : results) {
            this.name = row.getString("pname");
            this.id = row.getUUID("pid");
            this.phoneNumber = row.getString("phonenumber");
            this.dob = row.getTimestamp("dob");
            if (name.equals("TOMTEST")) {
                break;
            }
        }

        //Create Treatement
        assertTrue(treatmentDAO.createTreatment(id, "FLU", "normal flu", "Dr. House", new Date(), "MRI", new Date(), "Normal"));
        //Get treatment by id
        assertNotNull(treatmentDAO.getPatientsTreatment(id).one());
        //Get treatment by id and medical cond
        assertNotNull(treatmentDAO.getPatientsTreatment(id, "FLU").one());
        //Update existing treatment
        assertTrue(treatmentDAO.updatePatient(id, "FLU", "normal flu", "Dr. House", new Date(), "xray", new Date(), "Normal1"));

        results = treatmentDAO.getPatientsTreatment(id);
        assertEquals("Normal1", results.one().getString("status"));

        //Delete treatment
        treatmentDAO.deletePatientTreatment(id);
        results = treatmentDAO.getPatientsTreatment(id);
        assertNull(results.one());
    }

    @Test
    public void testMedicalInf() {
        ResultSet results = patientDAO.getPatients();
        for (Row row : results) {
            this.name = row.getString("pname");
            this.id = row.getUUID("pid");
            this.phoneNumber = row.getString("phonenumber");
            this.dob = row.getTimestamp("dob");
            if (name.equals("TOMTEST")) {
                break;
            }
        }

        assertTrue(medicalInfDAO.createPatientsMedInf(id, 5.8f, 70, 21.3f, "AB-"));
        assertNotNull(medicalInfDAO.getPatientsMedInf(id).one());
        assertTrue(medicalInfDAO.updatePatientsMedInf(id, 5.0f, 70, 22.4f, "A+"));

        results = medicalInfDAO.getPatientsMedInf(id);
        assertEquals("A+", results.one().getString("bloodgrp"));

        //Delete treatment
        medicalInfDAO.deletePatient(id);
        results = medicalInfDAO.getPatientsMedInf(id);
        assertNull(results.one());
    }

    @Test
    public void testDeletePatient() {
        ResultSet results = patientDAO.getPatients();
        for (Row row : results) {
            this.name = row.getString("pname");
            this.id = row.getUUID("pid");
            this.phoneNumber = row.getString("phonenumber");
            this.dob = row.getTimestamp("dob");
            if (name.equals("TOMTEST")) {
                break;
            }
        }
        assertTrue(patientDAO.deletePatient(id));
        results = patientDAO.getPatient(id);
        assertNull(results.one());
    }

}
