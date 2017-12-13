package projects.distributed.happypatients.springboot.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import projects.distributed.happypatients.server.ServerDetails;
import projects.distributed.happypatients.springboot.model.Patient;
import projects.distributed.happypatients.springboot.service.TreatmentInformationServiceImpl;
import projects.distributed.happypatients.springboot.utilities.JsonConverter;

@Component
public class CacheConnector {

    static final Logger LOG = LoggerFactory.getLogger(TreatmentInformationServiceImpl.class);

    @Autowired
    JsonConverter jsonConverter;

    public Patient getPatientFromCache(String id) {
        LOG.info("Getting patient {} from cache.", id);
        try {
            RestTemplate restTemplate = new RestTemplate();
            String patient = restTemplate.getForObject(ServerDetails.CACHE_SERVER + "/cache/patient/" + id, String.class);
            if (patient != null) {
                Patient patientObject = jsonConverter.getPatientObjectFromJson(patient);
                LOG.info("Get patient {} from cache successful.", id);
                return patientObject;
            }
        } catch (ResourceAccessException e) {
            LOG.info("Get patient {} from cache failed.", id);
            return null;
        }
        return null;
    }

    public boolean savePatientToCache(String id, Patient patient) {
        LOG.info("Saving patient {} to cache.", id);
        try {
            String patientJson = jsonConverter.getJsonFromPatientObject(patient);
            RestTemplate restTemplate = new RestTemplate();
            MultiValueMap<String, String> parametersMap = new LinkedMultiValueMap<String, String>();
            parametersMap.add("patientjson", patientJson);
            restTemplate.postForObject(ServerDetails.CACHE_SERVER + "/cache/patient/" + id, parametersMap, String.class);
            LOG.info("Save patient {} to cache successful.", id);
            return true;
        } catch (ResourceAccessException e) {
            LOG.info("Save patient {} to cache failed.", id);
            return false;
        }
    }

    public boolean removePatientFromCache(String id) {
        LOG.info("Removing patient {} from cache.", id);
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.delete(ServerDetails.CACHE_SERVER + "/cache/patient/" + id, String.class);
            LOG.info("Remove patient {} from cache successful.", id);
            return true;
        } catch (ResourceAccessException e) {
            LOG.info("Remove patient {} from cache failed.", id);
            return false;
        }
    }

    public boolean initializeCacheWithAllRecords(String allRecords) {
        LOG.info("Initializing cache.");
        try {
            RestTemplate restTemplate = new RestTemplate();
            MultiValueMap<String, String> parametersMap = new LinkedMultiValueMap<String, String>();
            parametersMap.add("allrecords", allRecords);
            restTemplate.postForObject(ServerDetails.CACHE_SERVER + "/cache/initialize/", parametersMap, String.class);
            LOG.info("Initialize cache successful.");
            return true;
        } catch (ResourceAccessException e) {
            LOG.info("Initialize cache failed.");
            return false;
        }
    }
}
