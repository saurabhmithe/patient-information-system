package project.distributed.cacheserver.springboot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.distributed.cacheserver.springboot.service.PatientService;

@RestController
@RequestMapping("/cache")
public class CacheController {

    public static final Logger logger = LoggerFactory.getLogger(CacheController.class);

    @Autowired
    PatientService patientService;

    // -------------------Retrieve Patient---------------------------------------------

    @RequestMapping(value = "/patient/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> readPatient(@PathVariable("id") String id) {
        String patient = patientService.readPatient(id);
        if (patient == null) {
            logger.info("Not found patient {} in cache.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("Found patient {} in cache.", id);
        return new ResponseEntity<String>(patient, HttpStatus.OK);
    }

    // -------------------Store Patient---------------------------------------------

    @RequestMapping(value = "/patient/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> addPatient(@PathVariable("id") String id, @RequestParam(value = "patientjson") String patient) {
        try {
            patientService.addPatient(id, patient);
            logger.info("Added patient {} to cache.", id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Failed to add patient {} to cache.", id);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // -------------------Delete Patient---------------------------------------------

    @RequestMapping(value = "/patient/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removePatient(@PathVariable("id") String id) {
        try {
            if(patientService.removePatient(id)) {
                logger.info("Deleted patient {} from cache.", id);
                return new ResponseEntity(HttpStatus.OK);
            } else {
                logger.info("Delete patient {} from cache failed.", id);
                return new ResponseEntity(HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Failed to delete patient {} from cache.", id);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // -------------------Initialize Cache---------------------------------------------

    @RequestMapping(value = "/initialize/", method = RequestMethod.POST)
    public ResponseEntity<?> initializeCache(@RequestParam(value = "allrecords") String allRecords) {
        try {
            patientService.clearCache();
            patientService.initializeCache(allRecords);
            logger.info("Successfully initialized cache.");
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Failed to initialize cache.");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
