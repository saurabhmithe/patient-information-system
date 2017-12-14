package projects.distributed.happypatients.springboot.cache;

import org.json.JSONException;
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
    	JSONObject jsonObject = new JSONObject();

    	String currentPolicy = policyEngine.retrievePolicy();
    	
        List<Patient> allPatients = patientService.findAllPatients();

        for (Patient patient : allPatients) {
        	if(isPatientEligibleToBeCached(patient, currentPolicy)) {
        		try {
					jsonObject.put(patient.getId(), jsonConverter.getJsonFromPatientObject(patient));
				} catch (JSONException e) {
					continue;
				}
        	}
        }

        cacheConnector.initializeCacheWithAllRecords(jsonObject.toString());
    }
    
    public boolean isPatientEligibleToBeCached(Patient patient, String currentPolicy) {
    	for(TreatmentInformation treatmentInformation:patient.getTreatmentInformation()) {
    		if (treatmentInformation.getTreatmentStatus().toString().equals(currentPolicy) && GeneralUtilities.getYear(treatmentInformation.getStartDate()) >= 2000) {
    			return true;
    		}
    	}
    	return false;
    }
}