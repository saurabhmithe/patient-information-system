package project.distributed.cacheserver.springboot.service;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IMap;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import project.distributed.cacheserver.configuration.Constants;

import java.util.HashMap;
import java.util.Map;

@Service
public class PatientService {

    public static final Logger logger = LoggerFactory.getLogger(PatientService.class);

    public void addPatient(String id, String patient) throws Exception {
        IMap<String, String> map = Hazelcast.getHazelcastInstanceByName(Constants.INSTANCE_NAME).getMap(Constants.MAP);
        map.put(id, patient);
    }

    public String readPatient(String id) {
        IMap<String, String> map = Hazelcast.getHazelcastInstanceByName(Constants.INSTANCE_NAME).getMap(Constants.MAP);
        return map.get(id);
    }

    public boolean removePatient(String id) {
        IMap<String, String> map = Hazelcast.getHazelcastInstanceByName(Constants.INSTANCE_NAME).getMap(Constants.MAP);
        String obj;
        Map<String, String> tmap = new HashMap<>();
        int originalSize = map.size();
        if(map.containsKey(id)) {
            logger.info("Patient found in cache.");
            for(String key : map.keySet()) {
                if (!key.equals(id)) {
                    tmap.put(key, map.get(key));
                }
            }
            map.clear();
            map.putAll(tmap);
        } else {
            logger.info("Patient not found in cache.");
        }
        if (map.size() < originalSize) {
            return true;
        } else {
            return false;
        }
    }

    public boolean initializeCache(String allRecords) {
        IMap<String, String> map = Hazelcast.getHazelcastInstanceByName(Constants.INSTANCE_NAME).getMap(Constants.MAP);

        JSONObject jsonObject = new JSONObject(allRecords);
        for (String key : jsonObject.keySet()) {
            map.put(key, jsonObject.getString(key));
        }
        return true;
    }

    public void clearCache() {
        IMap<String, String> map = Hazelcast.getHazelcastInstanceByName(Constants.INSTANCE_NAME).getMap(Constants.MAP);
        map.clear();
    }

}
