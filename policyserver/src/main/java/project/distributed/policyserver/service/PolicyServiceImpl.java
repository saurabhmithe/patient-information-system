package project.distributed.policyserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.distributed.policyserver.manager.PropertiesManager;

@Service
public class PolicyServiceImpl implements PolicyService {

    @Autowired
    PropertiesManager propertiesManager;

    @Override
    public String getPolicy() {
        return propertiesManager.readFromPropertyFile();
    }

    @Override
    public boolean updatePolicy(String policy) {
        return propertiesManager.writeToPropertyFile(policy);
    }
}
