package project.distributed.cacheserver.springboot.service;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IMap;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import project.distributed.cacheserver.configuration.Constants;

@Service
public class PatientService {

    public void addPatient(String id, String patient) throws Exception {
        IMap<String, String> map = Hazelcast.getHazelcastInstanceByName(Constants.INSTANCE_NAME).getMap(Constants.MAP);
        map.put(id, patient);
    }

    public String readPatient(String id) {
        IMap<String, String> map = Hazelcast.getHazelcastInstanceByName(Constants.INSTANCE_NAME).getMap(Constants.MAP);
        return map.get(id);
    }

    public String removePatient(String id) {
        IMap<String, String> map = Hazelcast.getHazelcastInstanceByName(Constants.INSTANCE_NAME).getMap(Constants.MAP);
        return map.remove(map.get(id));
    }

    public boolean initializeCache(String allRecords) {
        IMap<String, String> map = Hazelcast.getHazelcastInstanceByName(Constants.INSTANCE_NAME).getMap(Constants.MAP);

        JSONObject jsonObject = new JSONObject(allRecords);
        for (String key : jsonObject.keySet()) {
            map.put(key, jsonObject.getString(key));
        }
        return true;
    }

}
