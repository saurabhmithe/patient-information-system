package projects.distributed.happypatients.springboot.cache;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projects.distributed.happypatients.springboot.model.Patient;
import projects.distributed.happypatients.springboot.model.TreatmentInformation;
import projects.distributed.happypatients.springboot.policy.PolicyEngine;
import projects.distributed.happypatients.springboot.service.PatientService;
import projects.distributed.happypatients.springboot.utilities.GeneralUtilities;
import projects.distributed.happypatients.springboot.utilities.JsonConverter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class CacheLoader {
    @Autowired
    PatientService patientService;

    @Autowired
    CacheConnector cacheConnector;

    @Autowired
    JsonConverter jsonConverter;

    @Autowired
    PolicyEngine policyEngine;

    public void initializeCache() {

        String currentPolicy = policyEngine.retrievePolicy();

        JSONObject jsonObject = new JSONObject();

        List<Patient> allPatients = patientService.findAllPatients();

        for (Patient patient : allPatients) {
            List<TreatmentInformation> treatmentInformationList = patient.getTreatmentInformation();
            for (TreatmentInformation information : treatmentInformationList) {
                if (information.getTreatmentStatus().toString().equals(currentPolicy) && GeneralUtilities.getYear(information.getStartDate()) >= 2000) {
                    jsonObject.put(patient.getId(), jsonConverter.getJsonFromPatientObject(patient));
                    break;
                }
            }
        }

        cacheConnector.initializeCacheWithAllRecords(jsonObject.toString());
    }
}