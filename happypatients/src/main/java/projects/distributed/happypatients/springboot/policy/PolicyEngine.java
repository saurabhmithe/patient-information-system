package projects.distributed.happypatients.springboot.policy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import projects.distributed.happypatients.dao.GenericDAO;
import projects.distributed.happypatients.server.ServerDetails;

// TODO: Call this engine while bootstrapping and push data from Cassandra to Hazelcast based on policy value
@Component
public class PolicyEngine {

    static final Logger LOG = LoggerFactory.getLogger(PolicyEngine.class);

    public static String currentPolicy;

    public String retrievePolicy() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String policy = restTemplate.getForObject(ServerDetails.POLICY_SERVER + "/policy/retrieve/", String.class);
            currentPolicy = policy;
            LOG.info("Successfully retrieved policy {} from Policy Server", currentPolicy);
            return policy;
        } catch (ResourceAccessException e) {
            LOG.error("Unable to retrieve policy from Policy Server");
            return null;
        }
    }

    public void updatePolicy(String policy) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.put(ServerDetails.POLICY_SERVER + "/policy/provision/" + policy, String.class);
            currentPolicy = retrievePolicy();
            LOG.info("Successfully updated policy {} from Policy Server", currentPolicy);
        } catch (ResourceAccessException e) {
            LOG.error("Unable to update policy on Policy Server");
        }
    }
}
