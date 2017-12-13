package projects.distributed.happypatients.springboot.policy;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import projects.distributed.happypatients.server.ServerDetails;

// TODO: Call this engine while bootstrapping and push data from Cassandra to Hazelcast based on policy value
@Component
public class PolicyEngine {

    public static String currentPolicy;

    public String retrievePolicy() {
        RestTemplate restTemplate = new RestTemplate();
        String policy = restTemplate.getForObject(ServerDetails.POLICY_SERVER + "/policy/retrieve/", String.class);
        currentPolicy = policy;
        return policy;
    }

    public void updatePolicy(String policy) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(ServerDetails.POLICY_SERVER + "/policy/provision/" + policy, String.class);
        currentPolicy = retrievePolicy();
    }
}
