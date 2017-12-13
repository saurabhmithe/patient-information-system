package projects.distributed.happypatients.springboot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import projects.distributed.happypatients.HappypatientsApplication;
import projects.distributed.happypatients.springboot.exception.CustomErrorType;
import projects.distributed.happypatients.springboot.model.Patient;
import projects.distributed.happypatients.springboot.service.PatientService;

import javax.jms.JMSException;
import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {

    public static final Logger logger = LoggerFactory.getLogger(PatientController.class);

    @Autowired
    PatientService patientService;

    // -------------------Retrieve All Patients---------------------------------------------

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<Patient>> listAllPatients() {
        List<Patient> patients = patientService.findAllPatients();
        if (patients.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
            // You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<Patient>>(patients, HttpStatus.OK);
    }

    // -------------------Retrieve Single Patient------------------------------------------

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getPatient(@PathVariable("id") String id) {
        logger.info("Fetching Patient with id {}", id);
        Patient patient = patientService.findPatientById(id);
        if (patient == null) {
            logger.error("Patient with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Patient with id " + id
                    + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Patient>(patient, HttpStatus.OK);
    }

    // -------------------Create a Patient-------------------------------------------

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<?> createPatient(@RequestBody Patient patient, UriComponentsBuilder ucBuilder) {
        logger.info("Creating Patient : {}", patient);

        String id = patientService.savePatient(patient);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/patient/{id}").buildAndExpand(id).toUri());

        try {
            HappypatientsApplication.producer.sendMessage(id, "Creation");
        } catch (JMSException e) {
            logger.error(e.getMessage());
        }

        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    // ------------------- Update a Patient ------------------------------------------------

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updatePatient(@PathVariable("id") String id, @RequestBody Patient patient) {
        logger.info("Updating Patient with id {}", id);

        Patient currentPatient = patientService.findPatientById(id);

        if (currentPatient == null) {
            logger.error("Unable to update. Patient with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Unable to upate. Patient with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        patient.setId(id);
        if (patientService.updatePatient(patient)) {
            try {
                HappypatientsApplication.producer.sendMessage(id, "Updation");
            } catch (JMSException e) {
                logger.error(e.getMessage());
            }
            return new ResponseEntity<Patient>(patient, HttpStatus.OK);
        } else
            return new ResponseEntity<Patient>(HttpStatus.NO_CONTENT);
    }

    // ------------------- Delete a Patient-----------------------------------------

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletePatient(@PathVariable("id") String id) {
        logger.info("Fetching & Deleting Patient with id {}", id);

        if (patientService.deletePatientById(id)) {
            try {
                HappypatientsApplication.producer.sendMessage(id, "Deletion");
            } catch (JMSException e) {
                logger.error(e.getMessage());
            }
            return new ResponseEntity<Patient>(HttpStatus.OK);
        } else
            return new ResponseEntity<Patient>(HttpStatus.NO_CONTENT);
    }

    // ------------------- Delete All Patients-----------------------------

    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public ResponseEntity<Patient> deleteAllPatients() {
        logger.info("Deleting All Patients");

        patientService.deleteAllPatients();
        return new ResponseEntity<Patient>(HttpStatus.NO_CONTENT);
    }

}
