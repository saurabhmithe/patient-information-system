package projects.distributed.happypatients.springboot.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import projects.distributed.happypatients.server.ServerDetails;
import projects.distributed.happypatients.springboot.model.Patient;
import projects.distributed.happypatients.springboot.utilities.JsonConverter;

@Component
public class CacheConnector {

    @Autowired
    JsonConverter jsonConverter;

    public Patient getPatientFromCache(String id) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String patient = restTemplate.getForObject(ServerDetails.CACHE_SERVER + "/cache/patient/" + id, String.class);
            if (patient != null) {
                Patient patientObject = jsonConverter.getPatientObjectFromJson(patient);
                return patientObject;
            }
        } catch (ResourceAccessException e) {
            return null;
        }
        return null;
    }

    public boolean savePatientToCache(String id, Patient patient) {
        String patientJson = jsonConverter.getJsonFromPatientObject(patient);
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> parametersMap = new LinkedMultiValueMap<String, String>();
        parametersMap.add("patientjson", patientJson);
        restTemplate.postForObject(ServerDetails.CACHE_SERVER + "/cache/patient/" + id, parametersMap, String.class);
        return true;
    }

    public boolean removePatientFromCache(String id) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(ServerDetails.CACHE_SERVER + "/cache/patient/" + id, String.class);
        return true;
    }

    public boolean initializeCacheWithAllRecords(String allRecords) {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> parametersMap = new LinkedMultiValueMap<String, String>();
        parametersMap.add("allrecords", allRecords);
        restTemplate.postForObject(ServerDetails.CACHE_SERVER + "/cache/initialize/", parametersMap, String.class);
        return true;
    }
}
