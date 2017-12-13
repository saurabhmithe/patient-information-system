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
        return new ResponseEntity<String>(patient, HttpStatus.OK);
    }

    // -------------------Store Patient---------------------------------------------

    @RequestMapping(value = "/patient/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> addPatient(@PathVariable("id") String id, @RequestParam(value = "patientjson") String patient) {
        try {
            patientService.addPatient(id, patient);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    // -------------------Delete Patient---------------------------------------------

    @RequestMapping(value = "/patient/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removePatient(@PathVariable("id") String id) {
        try {
            patientService.removePatient(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    // -------------------Initialize Cache---------------------------------------------

    @RequestMapping(value = "/initialize/", method = RequestMethod.POST)
    public ResponseEntity<?> initializeCache(@RequestParam(value = "allrecords") String allRecords) {
        try {
            patientService.initializeCache(allRecords);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
