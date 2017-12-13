package projects.distributed.happypatients.springboot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projects.distributed.happypatients.springboot.model.Patient;
import projects.distributed.happypatients.springboot.model.TreatmentInformation;
import projects.distributed.happypatients.springboot.service.TreatmentInformationService;

import java.util.List;

@RestController
@RequestMapping("/treatment")
public class TreatmentInfoController {

    public static final Logger LOG = LoggerFactory.getLogger(TreatmentInfoController.class);
    @Autowired
    TreatmentInformationService treatmentService;

    // -------------------Create a Patient's treatment-------------------------------------------

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<?> createTreatment(@RequestBody TreatmentInformation treatmentInformation) {
        LOG.info("Creating Patient's treatment");

        if (treatmentService.addTreatmentInformation(treatmentInformation))
            return new ResponseEntity<String>(HttpStatus.OK);
        else
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // -------------------Retrieve Patient's treatment------------------------------------------

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getTreatment(@PathVariable("id") String id) {
        LOG.info("Fetching Patient treatment", id);
        List<TreatmentInformation> treatments = treatmentService.getTreatmentInformation(id);
        return new ResponseEntity<List<TreatmentInformation>>(treatments, HttpStatus.OK);
    }

    // ------------------- Delete all Treatments of a patient -----------------------------------------

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAllTreatment(@PathVariable("id") String id) {
        LOG.info("Fetching & Deleting Patient's treatment with id {}", id);

        if (treatmentService.deletePatientTreatment(id))
            return new ResponseEntity<Patient>(HttpStatus.OK);
        else
            return new ResponseEntity<Patient>(HttpStatus.NO_CONTENT);
    }

    // ------------------- Delete specific Treatment of a patient-----------------------------------------

    @RequestMapping(value = "/{id}/{medicalCond}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteTreatment(@PathVariable("id") String id, @PathVariable("medicalCond") String medicalCond) {
        LOG.info("Fetching & Deleting Patient's treatment with id {}", id);

        if (treatmentService.deletePatientTreatment(id, medicalCond))
            return new ResponseEntity<Patient>(HttpStatus.OK);
        else
            return new ResponseEntity<Patient>(HttpStatus.NO_CONTENT);
    }

    // ------------------- Update a Treatment ------------------------------------------------

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public ResponseEntity<?> updatePatient(@RequestBody TreatmentInformation treatmentInformation) {
        LOG.info("Updating Patient Treatment with id {}", treatmentInformation.getPatientId());
        if (treatmentService.updateTreatment(treatmentInformation))
            return new ResponseEntity<Patient>(HttpStatus.OK);
        else
            return new ResponseEntity<Patient>(HttpStatus.NOT_ACCEPTABLE);
    }


}
